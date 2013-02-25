import java.io.File;
import java.util.ArrayList;

public class ExeSearch {
	private ArrayList<File> found = new ArrayList<File>();
	public ExeSearch(){
		String OS = System.getProperty("os.name");
		//System.out.println(OS);
		switch(OS){
			case "Windows 7":
				String win64 = System.getenv("PROGRAMFILES").concat("\\");
				String win32 = System.getenv("PROGRAMFILES(x86)").concat("\\");
				//String win64 = "C:\\Program Files (x86)\\7-Zip";
				//String win32 = null;
				//System.out.println(win64 + "\t" + win32);
				if(win32 != null){
					found = search(win32);
					found.addAll(search(win64));
					//System.out.println(results.size());
				}else if(win64 != null){
					found = search(win64);
				}else{
					System.out.println("Error!");
				}
				break;
			// Need to add other OS support.
		}
	}
	public int getNumFiles(){
		return found.size();
	}
	public String getInfo(int i){
		File cur =  found.get(i);
		return this.getInfo(cur);
	}
	public File getFile(int index){
		return found.get(index);
	}
	public String getInfo(File f){
		if(found.contains(f)){
			return ("Name: " +  found.get(found.indexOf(f)).getName() + ", Location: " + found.get(found.indexOf(f)).getAbsolutePath());
		}else{
			return "No File Found";
		}
	}
	public String getName(File f){
		if(found.contains(f)){
			return found.get(found.indexOf(f)).getName();
		}else{
			return "No File Found";
		}
	}
	public String getLocation(File f){
		if(found.contains(f)){
			return (found.get(found.indexOf(f)).getAbsolutePath());
		}else{
			return "No File Found";
		}
	}
	public File findFile(String filename){
		String standard = filename.replace(".exe", "");
		standard = standard.replaceAll("\\W", "");
		standard = standard.toLowerCase();
		System.out.println(standard);
		for(File f : found){
			if((f.getName().contains(standard)) ||  (f.getName().contains(filename))){
				return f;
			}
		}
		return null;
	}
	public String toString(){
		String result = "Files: " + found.size();
		return result;
	}
	private ArrayList<File> search( String dirName){
		ArrayList<File> files = new ArrayList<File>();
    	File dir = new File(dirName);
	    	String[] list = dir.list();
	    	for(String str : list){
	    		File parent = new File(dirName + str);
	        	if(parent.isDirectory()){
	        		String[] children = parent.list();
	        		for(String childStr : children){
		        		File child = new File(dirName + parent.getName() + "\\" + childStr);
			    		if((child.getName().matches("([^\\s]+(\\.(?i)(exe))$)")) && (!(child.getName().equalsIgnoreCase("uninstall.exe")))){
			    			files.add(child);
			    			//System.out.println("Child: " + child.getName() + " Exists: " + child.exists() + " Dir: " + child.isDirectory());
			    		}
	        		}
		    	}
	    	}
    	//System.out.println(files.size());
    	return files;
    }
}
