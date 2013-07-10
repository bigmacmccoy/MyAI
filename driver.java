import java.util.*;

public class driver {

	private static Command current = new Command();
	
	public static void main(String[] args) {
		System.out.println("Initializing MAI...");
		AICore MAI = new AICore();
		//Start Voice Recognition
		//Wait for input
		

		String OS = System.getProperty("os.name");
		String ARCH = System.getProperty("os.arch");
		if(ARCH.contains("64")){
			OS += " 64bit";
		}else{
			OS += " 32bit";
		}
		System.out.println(OS);
		
		String input = "";
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Waiting for input: ");
		input = keyboard.nextLine();
		while(!(input.equalsIgnoreCase("exit"))){
			Input in = MAI.Recieve(input);
			current = MAI.Process(in);
			if(current == null){
				System.out.println("No Match Found.");
			}
			boolean success = MAI.Run(current, OS);
			if(success){
				System.out.println("Operation Complete.");
			}else{
<<<<<<< HEAD
				System.out.println("Operation Failed.");
=======
				current = MAI.Recieve(input);
				
				current = MAI.Process(current);
				if(current == null){
					System.out.println("No Match Found.");
					return;
				}
				boolean success = MAI.Run(current, OS);
				if(success){
					System.out.println("Operation Complete.");
				}else{
					System.out.println("Operation Failed.");
				}
>>>>>>> master
			}
			input = keyboard.nextLine();
		}
		keyboard.close();
		System.out.println("Exiting...");
	}
}
