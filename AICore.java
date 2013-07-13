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
	public Command Recieve(String userInput){
		Command current = new Command(userInput, CurrentTime());
		//Print("Received: " + current);
		return current;
	}
	public Command Process(Command current){
		String original = current.getInput();
		//Print("Orig: " + current);
		String[] filtered = Filter(original);
		//Print("Filtered: " + original);
		current.setCommandList(filtered);
		//Print("Modified: " + current);
		Command processed = commands.Match(current);
		if(processed == null){
			error = "No Matches!";
			return null;
		}else{
			Print("Matched: " + processed.getName());
			Print("Processed: " + processed);
			return processed;
		}
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
		//System.out.println(current);
		String result = "cmd.exe /C ";
		ArrayList<String> array = new ArrayList<String>();
		array.add(current.getAction(OS));
		System.out.println("Command: " + current.getAction(OS) + " - " + current.hasArgument);
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
				result = result.concat("\"" + com + "\"");
				//Print("Final: " + com + " - " + result);
			}else if(com.matches("http://.*")){
				result.concat(" <" + com + ">");
			}
		}
		try{
			Print("Final: " + result);
			Runtime rt = Runtime.getRuntime();
			@SuppressWarnings("unused")
			Process proc = rt.exec(result);
		}catch (Exception e){
			//e.printStackTrace();
			//Print("Could Not Run Command.");
			return false;
		}
		return true;
	}
	private String[] Filter(String unfiltered){
		String[] split = unfiltered.split(" ");
		//for(String s : split){
		//	Print("Split: " + s);
		//}
		ArrayList<String> filtered = new ArrayList<String>();
		ArrayList<String> badAL = new ArrayList<String>();
		String[] result = null;
		
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
			result = new String[filtered.size()];
			
			for(int i = 0; i < filtered.size(); i++){
				result[i] = filtered.get(i);
			}
			
			//for(String s : result){
			//	Print("Result: " + s);
			//}
			
			br.close();
		}catch(FileNotFoundException e){
			error = "File Not Found.";
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	public long CurrentTime(){
		return System.nanoTime();
    	
	}
	public <T> void Print(T object){
			System.out.println(object.toString());
	}
}