import java.util.*;

public class driver {

	public static void main(String[] args) {
		System.out.println("Initializing MAI...");
		AICore MAI = new AICore();
		//Start Voice Recognition
		//Wait for input
		String input = "";
		Scanner keyboard = new Scanner(System.in);
		while(!(input.equalsIgnoreCase("exit"))){
			input = keyboard.nextLine();
			if(input.equalsIgnoreCase("exit")){
				MAI.Recieve(input);
			}
		}
		keyboard.close();
		System.out.println("Exiting...");

	}

}
