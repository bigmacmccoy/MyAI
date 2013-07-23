import java.util.ArrayList;


public class MemoryBank {
	
	private ArrayList<Memory> allMem = null;
	
	public MemoryBank() {
		allMem = new ArrayList<Memory>();
	}
	public void Add(Action result, String input){
		Memory mem = new Memory(result, input);
		allMem.add(mem);
	}
	public Memory Get(Command result){
		for(Memory mem : allMem){
			if(mem.getPreference() == result){
				return mem;
			}
		}
		return null;
	}
}
