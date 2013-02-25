
public class Action {
	private String name = "";
	private Executable exe;
	private Input in;
	private boolean hasArgument = false;
	
	public Action(String name, Executable exe, Input in){
		this.setName(name);
		this.setExe(exe);
		this.setIn(in);
	}
	public Action(String name){
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Executable getExe() {
		return exe;
	}

	public void setExe(Executable exe) {
		this.exe = exe;
	}

	public Input getIn() {
		return in;
	}

	public void setIn(Input in) {
		this.in = in;
	}
	public boolean HasArgument() {
		return hasArgument;
	}
	public void HasArgument(boolean hasArgument) {
		this.hasArgument = hasArgument;
	}
}
