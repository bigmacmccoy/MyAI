import java.util.ArrayList;

public class MAIObject {
	private String name = "";
	private ArrayList<String> triggers = new ArrayList<String>();
	private String Win7x32 = "";
	private String Win7x64 = "";
	
	public MAIObject(){
		return;
	}
	public String getAction(String os){
		switch(os){
		case "Windows 7 32bit":
			return Win7x32;
		case "Windows 7 64bit":
			return Win7x64;
		default:
			return null;
		}
	}
	public void setAction(String os, String action){
		switch(os){
		case "Windows 7 32bit":
			Win7x32 = action;
			break;
		case "Windows 7 64bit":
			Win7x64 = action;
			break;
		default:
			return;
		}
		return;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getTriggers() {
		return triggers;
	}

	public void setTriggers(ArrayList<String> triggers) {
		this.triggers = triggers;
	}

	public void Clear(){
		this.setName(null);
		this.setAction("Windows 7 64bit", null);
		this.setAction("Windows 7 32bit", null);
		this.triggers.clear();
		return;
	}
}
