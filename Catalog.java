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
			
			nList = doc.getElementsByTagName("Argument");

			for (int temp = 0; temp < nList.getLength(); temp++) {
 				Node nNode = nList.item(temp);
		   		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
 					Element eElement = (Element) nNode;
 					if(eElement.getAttribute("Name") != null){
 						//System.out.println(eElement.getAttribute("Name"));
 						//System.out.println("\"" + eElement.getAttribute("Link") + "\"");
 						if(Find(eElement.getAttribute("Link")) == null){
 							AICore.error = "Argument has no link!";
 						}else{
	 						Argument arg = new Argument(Find(eElement.getAttribute("Link")));
	 						arg.setName(eElement.getAttribute("Name"));
	 						arg.setAction(eElement.getAttribute("Win64"), "Windows 7 64bit");
	 						arg.setAction(eElement.getAttribute("Win32"), "Windows 7 32bit");
	 						arg.setCommandList(SplitCommands(eElement.getAttribute("Command")));
	 						//System.out.println("Name: " + com.getName());
	 						ArgumentCatalog.add(arg);
 						}
 					}else{
 						//System.out.println(eElement.getNodeName());
 					}
 				}else{
 					//System.out.println("Not a node.");
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
			
		}
		return comArray;
	}
	public Command Match(Command two){
		Command com = MatchCommand(two);
		Argument arg = MatchArgument(two);
		if(com == null){
			return null;
		}else if(arg == null){
			com = Merge(com, two);
			return com;
		}else{
			com = Merge(com, two, arg);
			return com;
		}
	}
	public Argument MatchArgument(Command two){
		int numMatches = 0;
		int totalMatches = 0;
		String[] twoCom = two.getCommandList();
		ArrayList<Integer> matchArray = new ArrayList<Integer>();
		ArrayList<Argument> argArray = new ArrayList<Argument>();
		//System.out.println("Size of Catalog: " + CommandCatalog.size());
		for(Argument one : ArgumentCatalog){	//ITerate Through catalog, one command at a time
			//System.out.println(one.getName());
			String[] oneCom = one.getCommandList();
			for(String twoStr : twoCom){
				//System.out.println("Two: " + twoStr);
				for(String oneStr : oneCom){
					//System.out.println("One: " + oneStr);
					if(oneStr.equalsIgnoreCase(twoStr)){	//Compare the command strings in each list
						numMatches++;	//if there is a match, increment the match number
					}
				}
			}
			if(numMatches > 0){		//If there was a match, store that command 
				//System.out.println("NumMatches: " + numMatches);
				argArray.add(one);	//and the match into an array.
				matchArray.add(numMatches);
				totalMatches += numMatches;
			}
		}
		if(totalMatches > 0){
			int last = matchArray.get(0);
			for(int num : matchArray){
				if(num > last){
					last = num;
				}
			}
			int index = matchArray.indexOf(last);
			return argArray.get(index);				//Return the command with the most matches.
		}else{
			return null;
		}
	}
	public Command MatchCommand(Command two){
		int numMatches = 0;
		int totalMatches = 0;
		String[] twoCom = two.getCommandList();
		ArrayList<Command> comArray = new ArrayList<Command>();
		ArrayList<Integer> matchArray = new ArrayList<Integer>();
		//System.out.println("Size of Catalog: " + CommandCatalog.size());
		for(Command one : CommandCatalog){	//ITerate Through catalog, one command at a time
			//System.out.println(one.getName());
			ArrayList<String> oneTrig = one.getTriggers();
			for(String inStr : oneTrig){
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
				matchArray.add(numMatches);
				totalMatches += numMatches;
			}
		}
		if(totalMatches > 0){
			int last = matchArray.get(0);
			for(int num : matchArray){
				if(num > last){
					last = num;
				}
			}
			int index = matchArray.indexOf(last);
			return comArray.get(index);				//Return the command with the most matches.
		}else{
			System.out.println("No Match.");
			return null;
		}
	}
	public Command Merge(Command one, Command two, Argument arg){
		Command result = new Command();
		if((one.getInput() == null) && (two.getInput() == null)){
			return null;
		}else if(one.getInput() != null){
			result.setInput(one.getInput());
			result.setStartTime(one.getStartTime());
			result.setAction(two.getAction("Windows 7 64bit"), "Windows 7 64bit");
			result.setAction(two.getAction("Windows 7 32bit"), "Windows 7 32bit");
			result.setName(two.getName());
			result.setCommandList(two.getCommandList());
		}else if(two.getInput() != null){
			result.setInput(two.getInput());
			result.setStartTime(two.getStartTime());
			result.setAction(one.getAction("Windows 7 64bit"), "Windows 7 64bit");
			result.setAction(one.getAction("Windows 7 32bit"), "Windows 7 32bit");
			result.setName(one.getName());
			result.setCommandList(one.getCommandList());
		}else{
			return null;
		}
		result.hasArgument = true;
		result.setArgLink(arg);
		return null;
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
