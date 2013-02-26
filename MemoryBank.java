import java.util.ArrayList;


public class MemoryBank {
	private ArrayList<Memory> bank = new ArrayList<Memory>();
	public MemoryBank(){
		
	}
	public void Add(Memory mem){
		bank.add(mem);
	}
	public Action Check(String input){
		String[] inSplit = input.split(" ");
		int matches = 0;
		ArrayList<String> matched = new ArrayList<String>();
		ArrayList<Integer> count = new ArrayList<Integer>();
		ArrayList<Memory> good = new ArrayList<Memory>();
		for(Memory mem : bank){
			if(mem.getMatchedTo().contains(input)){
				return mem.getAction();
			}
			boolean isGood = false;
			String[] memSplit = mem.getInput().getOriginal().split(" ");
			for(String in : inSplit){
				for(String str : memSplit){
					if(str.equalsIgnoreCase(in)){
						matches++;
					}
				}
				if(matches > 0){
					matched.add(in);
					count.add(matches);
					isGood = true;
					matches = 0;
				}
			}
			if(isGood){
				good.add(mem);
			}
		}
		if(matched.size() > 0){
			int temp = 0;
			for(int i : count){
				if(i > temp){
					temp = i;
				}
			}
			int index = count.indexOf(temp);
			Memory result = good.get(index);
			result.addMatch(matched.get(index));
			return result.getAction();
		}
		return null;
	}
}
