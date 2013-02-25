import java.util.Scanner;


public class driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AICore core = new AICore();
		System.out.println("MAI Online. Please input: ");
		Scanner input = new Scanner(System.in);
		String line = "";
		do{
			line = input.nextLine();
			if(line.equalsIgnoreCase("exit")){
				break;
			}
			core.Recieve(line);
		}while(!(line.equalsIgnoreCase("exit")));
	}

}
