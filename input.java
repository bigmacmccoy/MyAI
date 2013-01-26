public class Input extends MAIObject{
	private String input = "";
	private long startTime = 0;
	
	public Input(String userInput, long time){
		setInput(userInput);
		setStartTime(time);
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
}
