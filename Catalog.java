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

			NodeList nList = doc.getElementsByTagName("GrammarObject");

			for (int temp = 0; temp < nList.getLength(); temp++) {
 				Node nNode = nList.item(temp);
		   		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
 					Element eElement = (Element) nNode;
 					if(eElement.getAttribute("Name") != null){
 						//System.out.println(eElement.getAttribute("Name"));
 						Command com = new Command();
 						com.setName(eElement.getAttribute("Name"));
 						com.setAction(eElement.getAttribute("Win64"), "Windows 7 64bit");
 						com.setAction(eElement.getAttribute("Win32"), "Windows 7 32bit");
 						com.setCommandList(SplitCommands(eElement.getAttribute("Command")));
 						//System.out.println("Name: " + com.getName());
 						if(eElement.hasChildNodes()){
 							com.setHasArgument(true);
 	 						NodeList children = eElement.getChildNodes();
 	 						for(int i = 0; i < children.getLength(); i++){
 	 							Node child = children.item(i);
 	 							if(child.getNodeType() == Node.ELEMENT_NODE){
 	 								Element elem = (Element) nNode;
 	 			 					if(elem.getAttribute("Name") != null){
 	 			 						Argument arg = new Argument(com);
 	 			 						arg.setName(elem.getAttribute("Name"));
 	 			 						arg.setAction(elem.getAttribute("Action"));
 	 			 						arg.setName(elem.getAttribute("Name"));
 	 			 						arg.setArgList(SplitCommands(elem.getAttribute("Command")));
 	 			 						ArgumentCatalog.add(arg);
 	 			 					}
 	 							}
 	 						}
 	 					}
 						CommandCatalog.add(com);
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
	}
	public ArrayList<ArrayList<?>> Match(Command two){
		int numMatches = 0;
		int totalMatches = 0;
		int argMatches = 0;
		String[] twoCom = two.getCommandList();
		ArrayList<ArrayList<?>> totalArray = new ArrayList<ArrayList<?>>();
		ArrayList<Integer> numArray = new ArrayList<Integer>();
		ArrayList<Command> comArray = new ArrayList<Command>();
		ArrayList<Argument> argArray = new ArrayList<Argument>();
		
		System.out.println("Size of Catalog: " + CommandCatalog.size());
		for(Command one : CommandCatalog){	//ITerate Through catalog, one command at a time
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
				comArray.add(one);	//and the number of matches in arrays.
				numArray.add(numMatches);
				totalMatches += numMatches; //Keep track of total matches as numMatches gets reset
				numMatches = 0;
				if(one.hasArgument){
					for(Argument arg : ArgumentCatalog){	//ITerate Through catalog, one Argument at a time
						String[] argCom = arg.getArgList();
						if(arg.getAssociated() == one){
							for(String twoStr : twoCom){
								for(String argStr : argCom){
									if(argStr.equalsIgnoreCase(twoStr)){	//Compare the command strings in each list
										numMatches++;	//if there is a match, increment the match number
									}
								}
							}
						}
						if(numMatches > 0){
							if(argMatches < numMatches){	//If there is an argument with more
								argMatches = numMatches;	//matches already added, skip this one
								argArray.add(arg);
								numMatches = 0;
							}
						}
					}
				}
			}
		}
		if(totalMatches > 0){
			totalArray.add(comArray);
			totalArray.add(argArray);
			return totalArray;				//Return EVERYTHING.
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
	public boolean hasArgs(Command com){
		for(Argument arg : ArgumentCatalog){
			if(com == arg.getAssociated()){
				return true;
			}
		}
		return false;
	}
	public Argument getArg(Command com, String argName){
		for(Argument arg : ArgumentCatalog){
			if((com == arg.getAssociated()) && (argName.equalsIgnoreCase(arg.getName()))){
					return arg;
			}
		}
		return null;
	}
}
