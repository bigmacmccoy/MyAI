
public class Executable extends MAIobj{
	private String location;
	private boolean hasArguments;
	private String argument;
	
	public Executable(String name, long last, String location, String argument) {
		this.setName(name);
		this.setLastUpdated(last);
		this.setLocation(location);
		this.setArgument(argument);
		
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public boolean hasArguments() {
		return hasArguments;
	}
	public void hasArguments(boolean arguments) {
		this.hasArguments = arguments;
	}
	public String getArgument() {
		return argument;
	}
	public void setArgument(String argument) {
		hasArguments(true);
		this.argument = argument;
	}

}
