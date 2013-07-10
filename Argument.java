
public class Argument extends Command{
	private Command associated;
<<<<<<< HEAD
=======
	private String actionName = "";
	private String actionWin64 = "";
	private String actionWin32 = "";
	private String[] commandList;
>>>>>>> master
	
	public Argument(){
	}
	public Argument(MAIObject obj){
		this.setName(obj.getName());
		this.setAction(obj.getAction("Windows 7 32bit"), "Windows 7 32bit");
		this.setAction(obj.getAction("Windows 7 64bit"), "Windows 7 64bit");
		this.setTriggers(obj.getTriggers());
	}
	
	public Command getAssociated() {
		return associated;
	}
	public void setAssociated(Command associated) {
		this.associated = associated;
	}
<<<<<<< HEAD
=======
	public String getName() {
		return actionName;
	}
	public void setName(String actionName) {
		this.actionName = actionName;
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
	public String[] getCommandList() {
		return commandList;
	}
	public void setCommandList(String[] commandList) {
		this.commandList = commandList;
	}
>>>>>>> master
}
