import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class AICore {
	public static String error = "";
// Should have input reception, output, and processing
	Catalog commands = new Catalog();
	
	public AICore(){
		Print(error);
		Print("MAI Online");
		return;
	}
	public void Recieve(String userInput){
		Command current = new Command(userInput, CurrentTime());
		Print(current);
		current = Process(current);
		
		return;
	}
	public Command Process(Command current){
		String original = current.getInput();
		String[] filtered = Filter(original);
		current.setCommandList(filtered);
		Command matched = commands.Match(current);
		Command processed = commands.Merge(current, matched);
		return processed;
	}
	private String[] Filter(String unfiltered){
		String[] split = unfiltered.split(" ");
		String[] filtered = new String[split.length];
		
		try{
			BufferedReader br = new BufferedReader(new FileReader("badWords.txt"));
			for(int i = 0; i < split.length; i++){
				while(br.ready()){
					if(split[i].equals(br.readLine())){
						filtered[i] = split[i];
					}
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
