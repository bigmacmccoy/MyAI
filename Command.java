
public class Command extends Catalog{
	private String input = "";
	private long startTime = 0;
	private String actionName = "";
	private String actionWin64 = "";
	private String actionWin32 = "";
	private String[] commandList = null;
	
	public Command(String textInput, long time){
		input = textInput;
		startTime = time;
	}
	public Command(){
		input = null;
		startTime = 0;
	}
	public String getInput(){
		return input;
	}
	public String getName(){
		return actionName;
	}
	public void setName(String name){
		actionName = name;
		return;
	}
	public String[] getCommandList(){
		return commandList;
	}
	public void setCommandList(String[] list){
		commandList = null;
		commandList = list;
		return;
	}
	public long getStartTime(){
		return startTime;
	}
	public String getAction(String os){
		switch(os){
			case "Windows 7 64bit":
				return actionWin64;
		case "Windows 7 32bit":
				return actionWin32;
		default:
				return null;
		}
	}
	public void setAction(String action, String os){
		switch(os){
			case "Windows 7 64bit":
				actionWin64 = action;
				break;
			case "Windows 7 32bit":
				actionWin32 = action;
				break;
			default:
				break;
		}
	}
	public String toString(){
		return "Input: " + input + ", Time: " + startTime + ".";
	}
	public void Clear(){
		input = null;
		startTime = 0;
		actionName = null;
		actionWin64 = null;
		actionWin32 = null;
		commandList = null;
	}
}
