import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;


public class AICore {
	private Catalog cat = new Catalog();
	private Engine eng = new Engine(System.getenv("os.name"));
	private ExeSearch search = new ExeSearch();
	private MemoryBank bank = new MemoryBank();
	
	public AICore(){
		searchSystem(this.search);
	}
	public boolean Recieve(String input){
		Input in = new Input(input);
		String[] inputSplit = input.split(" ");
		String[] result = null;
		int actions = 0;
		ArrayList<String> matches = new ArrayList<String>();
		ArrayList<Action> todo = new ArrayList<Action>();
		Executable exe = null;
		for(String str : inputSplit){
			result = Check(str, in);
			if(result == null){
				File file = search.findFile(str);
				if(file != null){
					Action current = new Action(str);
					actions++;
					matches.add(str);
					exe = new Executable(str, System.currentTimeMillis(), file.getAbsolutePath(), null);
					current.setExe(exe);
					current.setIn(in);
					todo.add(current);
				}
			}else if(result[0].equalsIgnoreCase("CurrentState")){
				System.out.println("Hello. I am MAI, the personalized Artificial Intelligence. I am currently online and waiting for your command.");
				return true;
			}else if(result[0].equalsIgnoreCase("skip")){
				continue;
			}else{
				actions++;
				matches.add(str);
				Action current = new Action(result[0]);
				if(result[1].contains(" ")){
					String argument = result[1].substring(result[1].indexOf(' '));
					String filename = result[1].replace(argument, "");
					exe = new Executable(result[0], System.currentTimeMillis(), search.findFile(filename).getAbsolutePath(), argument);
					current.HasArgument(true);
				}else{
					exe = new Executable(result[0], System.currentTimeMillis(), search.findFile(result[0]).getAbsolutePath(), null);
				}
				current.setExe(exe);
				current.setIn(in);
				todo.add(current);
			}
		}
		if(actions > 1){
			in.hasMultipleActions(true);
		}else if(actions == 0){
			System.out.println("I couldn't figure out what you wanted me to do. If can rephrase it, please do, if not please type \"Skip\".");
			Scanner keyboard = new Scanner(System.in);
			String rephrase = keyboard.nextLine();
			Recieve(input, rephrase);
			return true;
		}
		for(Action act : todo){
			cat.add(act);
			Memory mem = new Memory(act, null, act.getIn());
			bank.Add(mem);
		}
		cat.add(in);
		boolean success = eng.run(todo);
		if(success){
			System.out.println("Complete.");
			return true;
		}else{
			System.out.println("Error!");
			return false;
		}
	}
	private void Recieve(String input, String rephrase){
		Recieve(rephrase);
		//Need to make recursive while updating memory... hmm...
	}
	
	public String[] Check(String word, Input in){
		if(word.equalsIgnoreCase("Mai")){
			if(in.getOriginal().split(" ").length == 1){
				String[] res = {"CurrentState"};
				return res;
			}else{
				String[] res = {"Skip"};
				return res;
			}
		}
		try {
			URL url = LoadActions.class.getClassLoader().getResource("MAIActions.txt");
			BufferedReader br = new BufferedReader(new FileReader(url.getPath()));
			while(br.ready()){
				String all = br.readLine();
				String[] names = all.substring(all.indexOf('(') + 1, all.indexOf(')')).split(",");
				String[] triggers = all.substring(all.indexOf('{') + 1, all.indexOf('}')).split(",");
				ArrayList<String> trig = new ArrayList<String>();
				boolean special = false;
				for(String str : triggers){
					if(str.contains("[*]")){
						special = true;
					}
					trig.add(str);
				}
				if(trig.contains(word)){
					return names;
				}else if(special){
					for(String trigger : trig){
						if(trigger.contains("[*]")){
							String norm = trigger.replace("[*]", "");
							if(word.matches(norm + ".*")){
								names[1] = names[1].replace("[*]", word);
								return names;
							}else if(word.matches(".*" + norm)){
								names[1] = names[1].replace("[*]", word);
								return names;
							}
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			DownloadFile(System.getenv("os.name"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public boolean DownloadFile(String OS){
		System.out.println("No File.");
		return true;
	}
	public boolean searchSystem(ExeSearch search){
		if(search.getNumFiles() > 0){
			return true;
		}else{
			return false;
		}
	}
}
