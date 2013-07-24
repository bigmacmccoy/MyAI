import java.awt.AWTException;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Frame extends JFrame implements ActionListener{
	Container con = this.getContentPane();
	JLabel output = new JLabel("");
	JTextField input = new JTextField();
	JPanel mainPanel = new JPanel();
	GroupLayout layout = new GroupLayout(mainPanel);
	JPanel image = new JPanel();
	JButton submit = new JButton("Submit");
	JCheckBox minTray = new JCheckBox();
	JButton audio = new JButton("Browse");
	JButton video = new JButton("Browse");
	JFileChooser find = new JFileChooser();
	JButton done = new JButton("Close");
	JLabel audioLabel = new JLabel();
	JLabel videoLabel = new JLabel();
	JMenuBar mBar = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenu edit = new JMenu("Edit");
	JMenuItem close = new JMenuItem("Close");
	JMenuItem preferences = new JMenuItem("Preferences");
	JMenuItem about = new JMenuItem("About");
	SystemTray tray = null;
	BufferedImage img;
	boolean pref = false;
	
	String OS = "";
	Boolean onTop = true;
	boolean again = false;
	AICore MAI =  new AICore();
	String inputText = "";
	
	public Frame(String str) throws IOException{
		OS = str;
		
		this.setBounds(0, 0, 500, 300);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("docs/icon.png")));
		mainPanel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainPanel.setBackground(Color.BLACK);
		file.add(about);
		file.add(close);
		edit.add(preferences);
		mBar.add(file);
		mBar.add(edit);
		setJMenuBar(mBar);
		mBar.setBackground(Color.DARK_GRAY);
		mBar.setForeground(Color.WHITE);
		mBar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
		file.setBackground(Color.DARK_GRAY);
		edit.setBackground(Color.DARK_GRAY);
		about.setBackground(Color.DARK_GRAY);
		close.setBackground(Color.DARK_GRAY);
		preferences.setBackground(Color.DARK_GRAY);
		file.setForeground(Color.WHITE);
		edit.setForeground(Color.WHITE);
		about.setForeground(Color.WHITE);
		close.setForeground(Color.WHITE);
		preferences.setForeground(Color.WHITE);
		
		setView("default");
		setVisible(true);
	}
	public void print(String str){
		output.setText(str);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if((event.getSource() == submit) || (event.getSource() == input)){
			if(input.getText().length() > 0){
				inputText = input.getText();
				input.setText("");
				if(inputText.equalsIgnoreCase("exit")){
					MAI.ShutDown();
					this.dispose();
				}else{
					if(again){
						output.setText("Awaiting Input: ");
						ArrayList<Action> list = MAI.Process(MAI.Recieve(inputText), OS);
						for(Action act : list){
							MAI.AddMemory(act, inputText);
						}
					}
					Command current =  new Command();
					current = MAI.Recieve(inputText);
					ArrayList<Action> result = MAI.Process(current, OS);
					if(result == null){
						again = true;
						output.setText("Please rephrase your request");
					}else{
						boolean success = MAI.Run(result);
						if(success){
							//print("Operation Complete.");
						}else{
							//print("Operation Failed.");
						}
					}
				}
			}
		}else if(event.getSource() == about){
			setView("about");
		}else if(event.getSource() == close){
			this.dispose();
		}else if(event.getSource() == preferences){
			pref = true;
			setView("preferences");
		}else if(event.getSource() == done){
			setView("default");
		}else if(event.getSource() == audio){
			find.setDialogTitle("Choose Audio Source Folder: ");
			find.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			find.setCurrentDirectory(new File(MAI.settings.getDefaultAudioFolder()));
			find.addActionListener(this);
			find.showOpenDialog(this);
			File f = find.getSelectedFile();
			if(f != null){
				MAI.SetSourceFolder("audio", f.getAbsolutePath());
				audioLabel.setText("Audio Source Folder: " + MAI.settings.getDefaultAudioFolder());
			}
		}else if(event.getSource() == video){
			find.setDialogTitle("Choose Video Source Folder: ");
			find.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			find.addActionListener(this);
			find.showOpenDialog(this);
			File f = find.getSelectedFile();
			if(f != null){
				MAI.SetSourceFolder("video", f.getAbsolutePath());
				videoLabel.setText("Video Source Folder: " + MAI.settings.getDefaultVideoFolder());
			}
		}else if(event.getSource() == minTray){
			MAI.settings.setMinimizeToTray(minTray.isSelected());
		}
	}
	public void setView(String view){
		mainPanel.removeAll();
		mainPanel.validate();
		switch(view){
		case "default":
			DisplayDefault();
			break;
		case "preferences":
			DisplayPref();
			break;
		case "about":
			DisplayAbout();
		default:
			DisplayDefault();
			break;
		}	
	}
	private void DisplayDefault() {
		output.setForeground(Color.WHITE);
		input.setBackground(Color.DARK_GRAY);
		input.setForeground(Color.WHITE);
		input.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.DARK_GRAY));
		submit.setBackground(Color.DARK_GRAY);
		submit.setForeground(Color.WHITE);
		submit.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.DARK_GRAY));
		input.addActionListener(this);
		submit.addActionListener(this);
		about.addActionListener(this);
		close.addActionListener(this);
		preferences.addActionListener(this);
		mainPanel.add(output);
		mainPanel.add(input);
		mainPanel.add(submit);
		
		
		output.setText("Awaiting input: ");
		con.add(mainPanel);
		
		layout.setHorizontalGroup(layout.createParallelGroup().addComponent(output).addGroup(layout.createSequentialGroup().addComponent(input).addComponent(submit)));
		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(output).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(input).addComponent(submit)));
		layout.setHonorsVisibility(false);
	}
	private void DisplayAbout() {
		
	}
	private void DisplayPref() {
		JLabel info = new JLabel();
		info.setText("Preferences:");
		info.setMaximumSize(new Dimension(400, 200));
		info.setForeground(Color.WHITE);
		JLabel minTrayLabel = new JLabel();
		minTrayLabel.setText("Minimixe to tray:\t\t ");
		minTrayLabel.setForeground(Color.WHITE);
		minTray.setBackground(mainPanel.getBackground());
		minTray.setForeground(Color.WHITE);
		minTray.setBorder(null);
		minTray.setSelected(MAI.settings.getMinimizeToTray());
		audioLabel.setText("Audio Source Folder: " + MAI.settings.getDefaultAudioFolder());
		videoLabel.setText("Video Source Folder: " + MAI.settings.getDefaultVideoFolder());

		done.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.DARK_GRAY));
		done.setBackground(Color.DARK_GRAY);
		done.setForeground(Color.WHITE);
		
		audioLabel.setBackground(mainPanel.getBackground());
		audioLabel.setForeground(Color.WHITE);
		videoLabel.setBackground(mainPanel.getBackground());
		videoLabel.setForeground(Color.WHITE);
		audio.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.DARK_GRAY));
		audio.setBackground(Color.DARK_GRAY);
		audio.setForeground(Color.WHITE);
		video.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.DARK_GRAY));
		video.setBackground(Color.DARK_GRAY);
		video.setForeground(Color.WHITE);
		
		done.addActionListener(this);
		audio.addActionListener(this);
		video.addActionListener(this);
		minTray.addActionListener(this);
		
		mainPanel.add(info);
		mainPanel.add(minTray);
		mainPanel.add(audioLabel);
		mainPanel.add(audio);
		mainPanel.add(videoLabel);
		mainPanel.add(video);
		
		layout.setHorizontalGroup(layout.createParallelGroup().addComponent(info).addGroup(layout.createSequentialGroup().addComponent(minTrayLabel).addComponent(minTray)).addGroup(layout.createSequentialGroup().addComponent(audioLabel).addComponent(audio)).addGroup(layout.createSequentialGroup().addComponent(videoLabel).addComponent(video)).addGroup(layout.createSequentialGroup().addComponent(done)));
		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(info).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(minTrayLabel).addComponent(minTray)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(audioLabel).addComponent(audio)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(videoLabel).addComponent(video)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(done)));
		layout.setHonorsVisibility(false);
	}
	
}
