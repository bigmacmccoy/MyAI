import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;


public class AICore extends Thread{
	public static String error = "";
// Should have input reception, output, and processing
	public static Catalog commands = null;
	public static MemoryBank previous = null;
	
	public AICore(){
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
			if(act.getAction().matches(".*%.+%.*")){
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
	public long CurrentTime(){
		return System.nanoTime();
    	
	}
	public <T> void Print(T object){
			System.out.println(object.toString());
	}
	public void ShutDown(){
		
	}
}