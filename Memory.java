import java.util.ArrayList;


public class Memory {
	private Action action;
	private String cause;
	private Input input;
	private long time;
	private ArrayList<String> link = new ArrayList<String>();
	
	public Memory(Action act, String match, Input input){
		this.setAction(act);
		this.setCause(match);
		this.setTime(System.currentTimeMillis());
		this.setInput(input);
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Input getInput() {
		return input;
	}

	public void setInput(Input input) {
		this.input = input;
	}

	public ArrayList<String> getMatchedTo() {
		return link;
	}

	public void addMatch(String match) {
		this.link.add(match);
	}
}
