import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Settings {
	private boolean minToTray;
	private String audioFolder;
	private String videoFolder;
	
	public Settings(){
		this.readFile("docs/settings.msf");
		System.out.println(minToTray + " - " + audioFolder + " - " + videoFolder);
	}
	public boolean getMinimizeToTray() {
		return minToTray;
	}
	public void setMinimizeToTray(boolean minTray) {
		this.minToTray = minTray;
		writeFile("docs/settings.msf");
	}
	public String getDefaultAudioFolder() {
		return audioFolder;
	}
	public void setDefaultAudioFolder(String defaultAudioFolder) {
		this.audioFolder = defaultAudioFolder;
		writeFile("docs/settings.msf");
	}
	public String getDefaultVideoFolder() {
		return videoFolder;
	}
	public void setDefaultVideoFolder(String defaultVideoFolder) {
		this.videoFolder = defaultVideoFolder;
		writeFile("docs/settings.msf");
	}
	private void readFile(String location){
		String line = "";
		try {
			BufferedReader read = new BufferedReader(new FileReader(location));
			while(read.ready()){
				line = read.readLine();
				if(line.contains("Tray")){
					String min = line.replace("Tray:", "");
					//System.out.println("Min: " + min);
					this.setMinimizeToTray(Boolean.parseBoolean(min));
				}else if(line.contains("Audio")){
					String loc = line.replace("Audio:", "");
					//System.out.println("Aud: " + loc);
					this.setDefaultAudioFolder(loc);
				}else if(line.contains("Video")){
					String loc = line.replace("Video:", "");
					//System.out.println("Vid: " + loc);
					this.setDefaultVideoFolder(loc);
				}
			}
			read.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void writeFile(String location){
		try {
			BufferedWriter write = new BufferedWriter(new FileWriter(location));
			String tray = ("Tray:" + this.getMinimizeToTray());
			String audio = ("Audio:" + this.getDefaultAudioFolder());
			String video = ("Video:" + this.getDefaultVideoFolder());
			write.write(tray + "\n");
			write.write(audio + "\n");
			write.write(video + "\n");
			write.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
}
