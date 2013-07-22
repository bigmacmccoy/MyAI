public class Action {
	private String Name;
	private String Action;
	private String Input;
	
	public Action(String name, String action, String in){
		this.setName(name);
		this.setAction(action);
		this.setInput(in);
	}
	public String getName(){
		return Name;
	}
	public void setName(String newName){
		Name = newName;
	}
	public String getAction(){
		return Action;
	}
	public void setAction(String newAction){
		Action = newAction;
	}
	public String getInput(){
		return Input;
	}
	public void setInput(String in){
		Input = in;
	}
	public String toString(){
		return("Name: " + this.getName() + " - " + this.getAction());
	}
}
