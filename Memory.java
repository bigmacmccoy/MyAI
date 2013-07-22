import java.util.ArrayList;


public class Memory {
	private Command preference;
	private boolean userDefined = false;
	public Memory(Command preference, String newComm) {
		setCommands(newComm);
		setPreference(preference);
		setUserDefined(false);
		return;
	}
	public Memory(Command preference, String newComm, boolean userDefined) {
		setCommands(newComm);
		setPreference(preference);
		setUserDefined(userDefined);
		return;
	}
	public void setCommands(String command) {
		String[] exist = this.getPreference().getCommandList();
		ArrayList<String> updated = new ArrayList<String>();
		for(String oldWord : exist){
			for(String newWord : command.split(" ")){
				if(oldWord.equalsIgnoreCase(newWord)){
					
				}else{
					updated.add(newWord);
				}
			}
			updated.add(oldWord);
		}
		this.getPreference().setCommandList(updated.toArray(exist));
	}
	public Command getPreference() {
		return preference;
	}
	public void setPreference(Command preference) {
		this.preference = preference;
	}
	public boolean isUserDefined() {
		return userDefined;
	}
	public void setUserDefined(boolean userDefined) {
		this.userDefined = userDefined;
	}

}
