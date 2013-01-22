
public class Argument {
	private Command associated;
	private String actionName = "";
	private String action = "";
	private String[] argList = null;
	
	public Argument(Command com){
		setAssociated(com);
		return;
	}
	public Command getAssociated() {
		return associated;
	}
	public void setAssociated(Command associated) {
		this.associated = associated;
	}
	public String getName() {
		return actionName;
	}
	public void setName(String actionName) {
		this.actionName = actionName;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String[] getArgList() {
		return argList;
	}
	public void setArgList(String[] argList) {
		this.argList = argList;
	}
}
