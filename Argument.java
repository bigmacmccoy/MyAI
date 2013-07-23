public class Argument {
	private Command associated;
	private String input;
	private String name = "";
	private String actionWin64 = "";
	private String actionWin32 = "";
	private String[] commandList;
	
	public Argument(Command com){
		setAssociated(com);
		return;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public Command getAssociated() {
		return associated;
	}
	public void setAssociated(Command associated) {
		this.associated = associated;
	}
	public String getName() {
		return name;
	}
	public void setName(String actionName) {
		this.name = actionName;
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
	public String toString(){
		return("Name: " + this.getName() + " Action: " + this.getInput());
	}
}
