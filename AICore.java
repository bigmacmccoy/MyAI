import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class AICore {
	private Catalog cat = new Catalog();
	private Engine eng = new Engine(System.getenv("os.name"));
	private ExeSearch search = new ExeSearch();
	
	public AICore(){
		searchSystem(this.search);
	}
	public void Recieve(String input){
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
			System.out.println("Error!");
			return;
		}
		for(Action act : todo){
			cat.add(act);
		}
		cat.add(in);
		boolean success = eng.run(todo);
		if(success){
			System.out.println("Complete.");
		}else{
			System.out.println("Error!");
		}
		return;
	}
	
	public String[] Check(String word, Input in){
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
