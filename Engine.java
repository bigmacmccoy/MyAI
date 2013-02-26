import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Engine {
	private String OS = "";
	public Engine(String OS){
		this.setOS(OS);
	}
	public boolean run(String command){
		System.out.println("Command: " + command);
		String output = "";
		try {
			Process pr = Runtime.getRuntime().exec(command);
			BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			while ((output = input.readLine()) != null) {
				System.out.println(output);
			}
			input.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	public boolean run(Action act){
		String command = act.getExe().getLocation();
		if(act.HasArgument()){
			command = command.concat(act.getExe().getArgument());
		}
		return run(command);
	}
	public boolean run(ArrayList<Action> actions){
		boolean result = true;
		for(Action act : actions){
			boolean res = this.run(act);
			if((result == false) && (res == true)){
				result = false;
			}else{
				result = res;
			}
		}
		return result;
	}
	public String getOS() {
		return OS;
	}
	public void setOS(String os) {
		OS = os;
	}
}
