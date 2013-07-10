
public class Command extends MAIObject{
	public boolean hasArgument = false;
	private Argument ArgLink = null;
	
	public Command(){
<<<<<<< HEAD
		
	}
	public Command(MAIObject obj){
		this.setName(obj.getName());
		this.setAction(obj.getAction("Windows 7 32bit"), "Windows 7 32bit");
		this.setAction(obj.getAction("Windows 7 64bit"), "Windows 7 64bit");
		this.setTriggers(obj.getTriggers());
=======
		input = null;
		startTime = 0;
	}
	public String getInput(){
		return input;
	}
	public void setInput(String input){
		this.input = input;
		return;
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
	public void setStartTime(long time){
		this.startTime = time;
		return;
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
>>>>>>> master
	}
	public String toString(){
		return "Type: Command" + 
				"Name: \"" + this.getName()
				+ "\".";
	}
	public void setHasArgument(boolean hasArgument) {
		this.hasArgument = hasArgument;
	}
	public Argument getArgLink() {
		return ArgLink;
	}
	public void setArgLink(Argument argLink) {
		ArgLink = argLink;
	}
}
