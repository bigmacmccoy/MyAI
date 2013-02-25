import java.util.ArrayList;


public class Catalog {
	private ArrayList<Executable> exes = new ArrayList<Executable>();
	private ArrayList<Input> inputs = new ArrayList<Input>();
	private ArrayList<Action> actions = new ArrayList<Action>();
	
	public Catalog(){
		assert(exes != null);
	}
	public int add(Executable item){
		exes.add(item);
		return exes.size();
	}
	public int add(Action act){
		actions.add(act);
		return actions.size();
	}
	public int add(Input in){
		inputs.add(in);
		return inputs.size();
	}
	public Executable getExe(){
		return exes.get(0);
	}
	public Executable getExe(int i){
		return exes.get(i);
	}
	public String PrintAll(){
		String result = "Exes: ";
		for(Executable e : exes){
			result.concat(e.getName() + ", ");
		}
		result.concat(";\nInputs: ");
		for(Input i : inputs){
			result.concat(i.getOriginal() + ", ");
		}
		result.concat(";\nActions: ");
		for(Action a : actions){
			result.concat(a.getName());
		}
		result.concat(";");
		return result;
	}
}
