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
	public Catalog(){
		try {
			File fXmlFile = new File("/MAI_v3.0/src/Grammar_EN.mgl");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("CommandObject");

			for (int temp = 0; temp < nList.getLength(); temp++) {
 				Node nNode = nList.item(temp);
		   		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
 					Element eElement = (Element) nNode;
 					if(eElement.getAttribute("Name") != null){
 						Command com = new Command();
 						com.setName(eElement.getAttribute("Name"));
 						com.setAction(eElement.getAttribute("Win64"), "Windows 7 64bit");
 						com.setAction(eElement.getAttribute("Win32"), "Windows 7 32bit");
 						com.setCommandList(SplitCommands(eElement.getAttribute("Command")));
 						CommandCatalog.add(com);
 					}
 				}
			}
		}catch(FileNotFoundException e){
			AICore.error = "File Not Found.";
			//e.printStackTrace();
		}catch(Exception e) {
			AICore.error = "Error reading MGL File.";
			//e.printStackTrace();
		}
	}
	public Command Match(Command two){
		int numMatches = 0;
		String[] twoCom = two.getCommandList();
		ArrayList<Integer> numArray = new ArrayList<Integer>();
		ArrayList<Command> comArray = new ArrayList<Command>();
		
		for(Command one : CommandCatalog){	//ITerate Through catalog, one command at a time
			String[] oneCom = one.getCommandList();
			for(String twoStr : twoCom){
				for(String oneStr : oneCom){
					if(oneStr.equalsIgnoreCase(twoStr)){	//Compare the command strings in each list
						numMatches++;	//if there is a match, increment the match number
					}
				}
			}
			if(numMatches > 0){		//If there was a match, store that command 
				comArray.add(one);	//and the number of matches in arrays.
				numArray.add(numMatches);
				numMatches = 0;
			}
		}
		int last = numArray.get(0);
		for(int cur : numArray){	//Iterate through the arrays of match numbers and find the largest
			if(cur > last){
				last = cur;
			}
		}
		int index = numArray.indexOf(last);		//Find the command that had the most matches
		return comArray.get(index);				//Return that command.
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
