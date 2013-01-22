
public class Memory {
	private String[] commands;
	private Command preference;
	private boolean userDefined = false;
	public Memory(Command preference, String[] commands) {
		setCommands(commands);
		setPreference(preference);
		setUserDefined(false);
		return;
	}
	public Memory(Command preference, String[] commands, boolean userDefined) {
		setCommands(commands);
		setPreference(preference);
		setUserDefined(userDefined);
		return;
	}
	public String[] getCommands() {
		return commands;
	}
	public void setCommands(String[] commands) {
		this.commands = commands;
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
