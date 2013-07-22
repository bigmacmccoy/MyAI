import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Catalog{
	private ArrayList<Command> CommandCatalog = new ArrayList<Command>();
	private ArrayList<Argument> ArgumentCatalog = new ArrayList<Argument>();
	public Catalog(){
		//System.out.println("Creating Catalog...");
		try {
			File fXmlFile = new File("docs/Grammar_EN.mgl");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("GrammarObject");

			for (int temp = 0; temp < nList.getLength(); temp++) {
 				Node nNode = nList.item(temp);
		   		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		   			//System.out.println();
 					Element eElement = (Element) nNode;
 					if(eElement.getAttribute("Name").length() > 0){
 						//System.out.println(eElement.getAttribute("Name"));
 						Command com = new Command();
 						com.setName(eElement.getAttribute("Name"));
 						com.setAction(eElement.getAttribute("Win64"), "Windows 7 64bit");
 						com.setAction(eElement.getAttribute("Win32"), "Windows 7 32bit");
 						com.setCommandList(SplitCommands(eElement.getAttribute("Command")));
 						//System.out.println("Name: \"" + com.getName() + "\"");
 						CommandCatalog.add(com);
 					}else{
 						//System.out.println(eElement.getNodeName());
 					}
 				}else{
 					//System.out.println("Not a node.");
 				}
			}
			
			nList = doc.getElementsByTagName("Argument");

			for (int temp = 0; temp < nList.getLength(); temp++) {
 				Node nNode = nList.item(temp);
		   		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
 					Element eElement = (Element) nNode;
 					if(eElement.getAttribute("Name") != null){
 						////System.out.println(eElement.getAttribute("Name"));
 						////System.out.println("\"" + eElement.getAttribute("Link") + "\"");
 						if(Find(eElement.getAttribute("Link")) == null){
 							//AICore.error = "Argument has no link!";
 						}
 						Argument arg = new Argument(Find(eElement.getAttribute("Link")));
 						arg.setName(eElement.getAttribute("Name"));
 						arg.setAction(eElement.getAttribute("Win64"), "Windows 7 64bit");
 						arg.setAction(eElement.getAttribute("Win32"), "Windows 7 32bit");
 						arg.setCommandList(SplitCommands(eElement.getAttribute("Command")));
 						////System.out.println("Name: " + com.getName());
 						ArgumentCatalog.add(arg);
 						
 					}else{
 						////System.out.println(eElement.getNodeName());
 					}
 				}else{
 					////System.out.println("Not a node.");
 				}
			}
		}catch(FileNotFoundException e){
			AICore.error = "File Not Found - " + e.getMessage();
			//e.printStackTrace();
		}catch(Exception e) {
			AICore.error = "Error reading MGL File.";
			//e.printStackTrace();
		}
		//System.out.println("Commands: " + CommandCatalog.size());
		//System.out.println("Arguments: " + ArgumentCatalog.size());
	}
	public ArrayList<Action> Match(Command inputCom, String OS){
		ArrayList<Command> CMatched = new ArrayList<Command>();
		ArrayList<Argument> AMatched = new ArrayList<Argument>();
		for(Command com : CommandCatalog){ // Iterate through Commands
			for(String trigger : inputCom.getInput().split(" ")){ // Iterate through input word by word
				for(String action : com.getCommandList()){ // Iterate through actions listed in CommandCatalog entry
					if(action.equalsIgnoreCase(trigger)){
						CMatched.add(com);
					}
				}
			}
		} // Have all Command matches in Matched
		for(Argument arg : ArgumentCatalog){ // Iterate through Commands
			for(String trigger : inputCom.getInput().split(" ")){ // Iterate through input word by word
				for(String action : arg.getCommandList()){ // Iterate through actions listed in CommandCatalog entry
					if(action.equalsIgnoreCase(trigger)){
						AMatched.add(arg);
					}
				}
			}
		} // Have all Command matches in Matched
		ArrayList<Action> actions = new ArrayList<Action>();
		for(Argument arg : AMatched){
			if(CMatched.contains(arg.getAssociated())){
				CMatched.remove(arg.getAssociated());
			}
			actions.add(new Action(arg.getName(), arg.getAction(OS), inputCom.getInput()));
		}
		for(Command com : CMatched){
			actions.add(new Action(com.getName(), com.getAction(OS), inputCom.getInput()));
		}
		return actions;
	}
	public Command Merge(Command two, Argument arg){
		System.out.println("Merging: " + two + ", and " + arg);
		Command result = new Command();
		if(two.getInput() != null){
			result.setInput(two.getInput());
			result.setStartTime(two.getStartTime());
			result.setAction(arg.getAction("Windows 7 64bit"), "Windows 7 64bit");
			result.setAction(arg.getAction("Windows 7 32bit"), "Windows 7 32bit");
			result.setName(arg.getName());
			result.setCommandList(arg.getCommandList());
		}else{
			//System.out.println("Result: Null");
			return null;
		}
		result.hasArgument = true;
		result.setArgLink(arg);
		//System.out.println("Result: " + result);
		return result;
	}
	public Command Merge(Command one, Command two){
		//System.out.println(one.isInputPresent() + " - " + two.isInputPresent());
		//System.out.println(one.getInput() + " - " + two.getInput());
		if(one.isInputPresent()){
			Command result = new Command(one.getInput(), one.getStartTime());
			result.setAction(two.getAction("Windows 7 64bit"), "Windows 7 64bit");
			result.setAction(two.getAction("Windows 7 32bit"), "Windows 7 32bit");
			result.setName(two.getName());
			result.setCommandList(two.getCommandList());
			return result;
		}else if(two.isInputPresent()){
			Command result = new Command(two.getInput(), two.getStartTime());
			result.setAction(one.getAction("Windows 7 64bit"), "Windows 7 64bit");
			result.setAction(one.getAction("Windows 7 32bit"), "Windows 7 32bit");
			result.setName(one.getName());
			result.setCommandList(one.getCommandList());
			return result;
		}else{
			System.out.println("Null!");
			return null;
		}
	}
	public Command Find(String name){
		for(Command com : CommandCatalog){
			if(com.getName().equalsIgnoreCase(name)){
				return com;
			}
		}
		return null;
	}
	private String[] SplitCommands(String allCommands){
		String[] list = allCommands.split(",");
		return list;
	}
}