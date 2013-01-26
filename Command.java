
public class Command extends MAIObject{
	public boolean hasArgument = false;
	
	public Command(){
		
	}
	public Command(MAIObject obj){
		this.setName(obj.getName());
		this.setAction(obj.getAction("Windows 7 32bit"), "Windows 7 32bit");
		this.setAction(obj.getAction("Windows 7 64bit"), "Windows 7 64bit");
		this.setTriggers(obj.getTriggers());
	}
	public String toString(){
		return "Type: Command" + 
				"Name: \"" + this.getName()
				+ "\".";
	}
	public void setHasArgument(boolean hasArgument) {
		this.hasArgument = hasArgument;
	}
}
