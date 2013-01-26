import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Catalog{
	private ArrayList<MAIObject> Catalog = new ArrayList<MAIObject>();
	private ArrayList<Command> CommandCatalog = new ArrayList<Command>();
	private ArrayList<Argument> ArgumentCatalog = new ArrayList<Argument>();
	public Catalog(){
		System.out.println("Creating Catalog...");
		try {
			File fXmlFile = new File("docs/Grammar_EN.mgl");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("GrammarLibrary");
			for(int i = 0; i < nList.getLength(); i++){
				Node gl = nList.item(i);
				NodeList lib = gl.getChildNodes();
				for(int a = 0; a < lib.getLength(); a++){
					Node current = lib.item(a);
					NodeList children = current.getChildNodes();
					for(int x = 0; x < children.getLength(); x++){
						MAIObject obj = new MAIObject();
						String nName = children.item(x).getNodeName();
						String nCon = children.item(x).getTextContent();
						
						switch (nName){
						case "Name":
							obj.setName(nCon);
							break;
						case "Triggers":
							ArrayList<String> trig = new ArrayList<String>();
							for(String str : nCon.split(",")){
								trig.add(str);
							}
							obj.setTriggers(trig);
							break;
						case "Win32":
							obj.setAction("Windows 7 32bit", nCon);
						case "Win64":
							obj.setAction("Windows 7 64bit", nCon);
						}
						
						if(current.getNodeName().equalsIgnoreCase("Command")){
							CommandCatalog.add(new Command(obj));
						}else if(current.getNodeName().equalsIgnoreCase("Argument")){
							ArgumentCatalog.add(new Argument(obj));
						}
					}
				}
			}
		}catch(FileNotFoundException e){
			AICore.error = "File Not Found - " + e.getMessage();
			//e.printStackTrace();
		}catch(Exception e) {
			AICore.error = "Error reading MGL File.";
			//e.printStackTrace();
		}
		System.out.println("Commands: " + CommandCatalog.size());
		System.out.println("Arguments: " + ArgumentCatalog.size());
		Catalog.addAll(CommandCatalog);
		Catalog.addAll(ArgumentCatalog);
	}
	public ArrayList<Command> Match(Input in){
		int numMatches = 0;
		ArrayList<String> inTrig = in.getTriggers();
		ArrayList<Command> comArray = new ArrayList<Command>();
		
		System.out.println("Size of Catalog: " + CommandCatalog.size());
		for(MAIObject one : Catalog){	//ITerate Through catalog, one command at a time
			//System.out.println(one.getName());
			ArrayList<String> oneTrig = one.getTriggers();
			for(String inStr : inTrig){
				if(inStr.equalsIgnoreCase(one.getName())){
					numMatches++;
				}else{
					//System.out.println("Two: " + twoStr);
					for(String oneStr : oneTrig){
						//System.out.println("One: " + oneStr);
						if(oneStr.equalsIgnoreCase(inStr)){	//Compare the command strings in each list
							numMatches++;	//if there is a match, increment the match number
						}
					}					
				}
			}
			if(numMatches > 0){		//If there was a match, store that command 
				//System.out.println("NumMatches: " + numMatches);
				comArray.add(one);	//and the match into an array.
			}
		}
		if(numMatches > 0){
			return comArray;				//Return all of the matched commands
		}else{
			System.out.println("No Match.");
			return null;
		}
	}
	public Command Merge(Command one, Command two){
		if((one.getInput() == null) && (two.getInput() == null)){
			return null;
		}else if(one.getInput() != null){
			Command result = new Command(one.getInput(), one.getStartTime());
			result.setAction(two.getAction("Windows 7 64bit"), "Windows 7 64bit");
			result.setAction(two.getAction("Windows 7 32bit"), "Windows 7 32bit");
			result.setName(two.getName());
			result.setCommandList(two.getCommandList());
			return result;
		}else if(two.getInput() != null){
			Command result = new Command(one.getInput(), one.getStartTime());
			result.setAction(one.getAction("Windows 7 64bit"), "Windows 7 64bit");
			result.setAction(one.getAction("Windows 7 32bit"), "Windows 7 32bit");
			result.setName(one.getName());
			result.setCommandList(one.getCommandList());
			return result;
		}else{
			return null;
		}
	}
	private String[] SplitCommands(String allCommands){
		String[] list = allCommands.split(",");
		return list;
	}
}
