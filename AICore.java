import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class AICore extends Thread{
	public static String error = "";
// Should have input reception, output, and processing
	public static Catalog commands = null;
	public static MemoryBank previous = null;
	public Settings settings = null;
	
	public AICore(){
		settings = new Settings();
		commands = new Catalog();
		previous = new MemoryBank();
		if(error.isEmpty()){
			Print("MAI Online");
		}else{
			Print(error);
			return;
		}		
		return;
	}
	public Command Recieve(String userInput){
		Command current = new Command(userInput, CurrentTime());
		//Print("Received: " + current);
		return current;
	}
	public ArrayList<Action> Process(Command current, String OS){
		String original = current.getInput();
		//Print("Orig: " + current);
		//String[] filtered = Filter(original);
		//Print("Filtered: " + original);
		current.setCommandList(original.split(" "));
		//Print("Modified: " + current);
		ArrayList<Action> actions = commands.Match(current, OS);
		if(actions.size() == 0){
			return null;
		}else{
			return actions;
		}
	}
	@SuppressWarnings("unused")
	private Command CheckMemory(Command input, ArrayList<Command> possible){
		for(Command com : possible){
			Memory last = previous.Get(com);
			if(last != null){
				return last.getPreference();
			}
		}
		return null;
	}
	public boolean Run(ArrayList<Action> actions){
		//System.out.println(current);
		String result = "cmd.exe /C ";
		for(Action act : actions){
			System.out.println(act.getName());
			if((act.getInput().contains("play") && (act.getName().equalsIgnoreCase("Spotify")))){
				System.out.println("Spotify play");
				String searchInfo = act.getInput().replace("play", "");
				if(searchInfo.contains("by")){
					searchInfo = searchInfo.replace("by", "");
				}
				searchInfo = searchInfo.replace(" ", "%20");
				String key = "";
				try {
					URL search = new URL("http://ws.spotify.com/search/1/track?q=" + searchInfo);
					ReadableByteChannel rbc = Channels.newChannel(search.openStream());
				    FileOutputStream fos = new FileOutputStream("docs/temp.xml");
				    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
					File fXmlFile = new File("docs/temp.xml");
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(fXmlFile);
					doc.getDocumentElement().normalize();

					NodeList nList = doc.getElementsByTagName("track");
	 				Node nNode = nList.item(0);
			   		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			   			System.out.println(act.getAction());
			   			String fin = act.getAction();
						String env = fin.substring(fin.indexOf('%')+1, fin.lastIndexOf('%'));
						String bad = fin.substring(fin.indexOf('%'), fin.lastIndexOf('%')+1);
						String value = System.getenv(env);
						if(env.equalsIgnoreCase("AppData")){
							value = value.replace("\\Roaming", "");
						}
						fin = fin.replace(bad, value);
						result = result.concat("\"" + fin + "\"");
			   			Element eElement = (Element) nNode;
			   			key = eElement.getAttribute("href");
			   			Runtime rt = Runtime.getRuntime();
						@SuppressWarnings("unused")
						Process proc = rt.exec(result + " " + key);
					    fos.close();
					    fXmlFile.delete();
					    key = "";
			   		}
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				key = "";
			}else if(act.getAction().matches("http://.*")){
				URI link;
				try {
					if(act.getAction().contains("search")){
						//System.out.println("Action: " + act.getAction());
						String terms = act.getInput();
						//System.out.println("Input: " + act.getInput());
						if(terms.contains("for")){
							terms = terms.replace("for", "");
						}
						if(terms.contains("and")){
							terms = terms.substring(terms.indexOf("search")+7,terms.indexOf("and"));
						}else{
							terms = terms.substring(terms.indexOf("search")+7, terms.length());
						}
						terms = terms.replace(' ', '+');
						//System.out.println("Terms: " + terms);
						link = new URI(act.getAction().replace("*", terms));
					}else{
						link = new URI(act.getAction());
					}
					Desktop desk = Desktop.getDesktop();
					//System.out.println("Final: \"" + link.getPath() + "\"");
					desk.browse(link);
				} catch (URISyntaxException e) {
					//e.printStackTrace();
					return false;
				} catch (IOException e) {
					//e.printStackTrace();
					return false;
				}
			}else if(act.getAction().matches(".*%.+%.*")){
				String fin = act.getAction();
				String env = fin.substring(fin.indexOf('%')+1, fin.lastIndexOf('%'));
				String bad = fin.substring(fin.indexOf('%'), fin.lastIndexOf('%')+1);
				//Print("Env: \"" + env + "\"");
				String value = System.getenv(env);
				if(env.equalsIgnoreCase("AppData")){
					value = value.replace("\\Roaming", "");
				}
				//Print("Rep: " + value);
				fin = fin.replace(bad, value);
				result = result.concat("\"" + fin + "\"");
				//Print("Final: " + com + " - " + result);
				try{
					Print("Final: " + result);
					Runtime rt = Runtime.getRuntime();
					@SuppressWarnings("unused")
					Process proc = rt.exec(result);
				}catch (Exception e){
					//e.printStackTrace();
					Print("Could Not Run Command.");
					return false;
				}
			}else{
				System.out.println("No Wildcards!");
				try{
					Print("Final: " + result);
					Runtime rt = Runtime.getRuntime();
					@SuppressWarnings("unused")
					Process proc = rt.exec(result);
				}catch (Exception e){
					//e.printStackTrace();
					Print("Could Not Run Command.");
					return false;
				}
			}
		}
		return true;
	}
	public void AddMemory(Action act, String input){
		previous.Add(act, input);
	}
	public void SetSourceFolder(String name, String location){
		if(name.equalsIgnoreCase("audio")){
			settings.setDefaultAudioFolder(location);
		}else if(name.equalsIgnoreCase("video")){
			settings.setDefaultVideoFolder(location);
		}
	}
	public long CurrentTime(){
		return System.nanoTime();
    	
	}
	public <T> void Print(T object){
			System.out.println(object.toString());
	}
	public void ShutDown(){
		settings.writeFile("docs/settings.msf");
	}
}