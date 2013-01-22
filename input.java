public class input {
	private String textInput;
	private String timeEntered;
	
	public input(String userInput, String time){
		textInput = userInput;
		timeEntered = time;
	}
	
	public String getInput(){
		return textInput;
	}
	
	public String getTime(){
		return timeEntered;
	}
}
