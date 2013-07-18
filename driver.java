import java.util.Scanner;

public class driver{
	private static String OS;
	public static Frame window = null;
	
	public static void main(String[] args) {
		OS = System.getProperty("os.name");
		String ARCH = System.getProperty("os.arch");
		if(ARCH.contains("64")){
			OS += " 64bit";
		}else{
			OS += " 32bit";
		}
		window = new Frame(OS);
	}
}