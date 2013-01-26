
public class Argument extends Command{
	private Command associated;
	
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
}
