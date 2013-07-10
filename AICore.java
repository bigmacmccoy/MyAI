import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class AICore {
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
	public Input Recieve(String userInput){
		Input current = new Input(userInput, CurrentTime());
		//Print("Recieved: " + current);
		return current;
	}
	public Command Process(Input in){
		Command current = new Command();
		String original = in.getInput();
		//Print("Orig: " + original);
<<<<<<< HEAD
		ArrayList<String> filtered = Filter(original);
		current.setTriggers(filtered);
		ArrayList<Command> matched = commands.Match(in);
		if(matched.size() == 0){
=======
		String[] filtered = Filter(original);
		current.setCommandList(filtered);
		Command processed = commands.Match(current);
		if(processed == null){
>>>>>>> master
			error = "No Matches!";
			return null;
		}else{
			//Print("Matched: " + matched.getName());
			Print("Processed: " + processed);
			return processed;
<<<<<<< HEAD
		}else{
			Command preference = CheckMemory(current, matched);
			if(preference == null){
				Command choice = AskUser(current, matched);
				if(choice == null){
					return null;
				}
				previous.Add(choice, current.getInput().split(" "));
				Command processed = commands.Merge(current, choice);
				Print("Processed: " + processed);
				return processed;
			}
		}
		return null;
	}
	private Command AskUser(Command input, ArrayList<Command> possible){
		Print("I could not match your query to an action. I have found " + possible.size() + " matched for your query.");
		Scanner user = new Scanner(System.in);
		Print("Here are the options I found. Please enter the corresponding number or \"none\" if none of them are correct.");
		int i = 1;
		for(Command com : possible){
				Print("[ " + i + " ]\t" + com.getName());
				i++;
			}
			String str = user.nextLine();
			if(str.equalsIgnoreCase("none")){
				user.close();
				return null;
			}
			int choice = Integer.parseInt(str);
			choice--;
			if((choice >= 0) && (choice < possible.size())){
				user.close();
				return possible.get(choice);
			}else{
				Print("Error: Please choose a number displayed on screen.");
				user.close();
				return null;
			}
=======
		}
>>>>>>> master
	}
	private Command CheckMemory(Command input, ArrayList<Command> possible){
		for(Command com : possible){
			Memory last = previous.Get(com);
			if(last != null){
				return last.getPreference();
			}
		}
		return null;
	}
	public boolean Run(Command current, String OS){
		//System.out.println(com);
		String result = "cmd.exe /C ";
		ArrayList<String> array = new ArrayList<String>();
		array.add(current.getAction(OS));
		if(current.hasArgument){
			array.add(current.getArgLink().getAction(OS));
		}
		for(String com : array){
			if(com.matches(".*%.+%.*")){
				String env = com.substring(com.indexOf('%')+1, com.lastIndexOf('%'));
				String bad = com.substring(com.indexOf('%'), com.lastIndexOf('%')+1);
				//Print("Env: \"" + env + "\"");
				String value = System.getenv(env);
				if(env.equalsIgnoreCase("AppData")){
					value = value.replace("\\Roaming", "");
				}
				//Print("Rep: " + value);
				com = com.replace(bad, value);
				result.concat("\"" + com + "\"");
				//Print("Final: " + com);
			}else if(com.matches("http://.*")){
				result.concat(" <" + com + ">");
			}
		}
		try{
			Print("Final: " + result);
			Runtime rt = Runtime.getRuntime();
			@SuppressWarnings("unused")
<<<<<<< HEAD
			Process proc = rt.exec("cmd.exe /C \"" + com + "\"");
			rt.gc();
=======
			Process proc = rt.exec(result);
>>>>>>> master
		}catch (Exception e){
			//e.printStackTrace();
			//Print("Could Not Run Command.");
			return false;
		}
		return true;
	}
	private ArrayList<String> Filter(String unfiltered){
		String[] split = unfiltered.split(" ");
		//for(String s : split){
		//	Print("Split: " + s);
		//}
		ArrayList<String> filtered = new ArrayList<String>();
		ArrayList<String> badAL = new ArrayList<String>();
		
		try{
			BufferedReader br = new BufferedReader(new FileReader("docs/badWords.txt"));
			while(br.ready()){
				badAL.add(br.readLine());
			}
			
			for(String input : split){
				if(badAL.contains(input)){
					//Print("Bad: " + input);
				}else{
					//Print("Good: " + input);
					filtered.add(input);
				}
			}
			br.close();
		}catch(FileNotFoundException e){
			error = "File Not Found.";
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filtered;
	}
	public long CurrentTime(){
		return System.nanoTime();
    	
	}
	public <T> void Print(T object){
			System.out.println(object.toString());
	}
}
