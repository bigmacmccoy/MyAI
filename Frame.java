import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Frame extends JFrame implements ActionListener{
	Container con = this.getContentPane();
	JFrame newWindow = new JFrame();
	JLabel output = new JLabel("");
	JTextField input = new JTextField();
	JPanel mainPanel = new JPanel();
	JPanel otherPanel = new JPanel();
	JPanel image = new JPanel();
	JButton submit = new JButton("Submit");
	JMenuBar mBar = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenu edit = new JMenu("Edit");
	JMenuItem close = new JMenuItem("Close");
	JMenuItem preferences = new JMenuItem("Preferences");
	JMenuItem about = new JMenuItem("About");
	BufferedImage img;
	
	String OS = "";
	Boolean onTop = true;
	boolean again = false;
	AICore MAI =  new AICore();
	MemoryBank MemBank = new MemoryBank();
	String inputText = "";
	
	public Frame(String str) throws IOException{
		OS = str;
		
		this.setBounds(0, 0, 500, 200);
		this.setAlwaysOnTop(true);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("docs/icon.png")));
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		mainPanel.setBackground(Color.BLACK);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		file.add(about);
		file.add(close);
		edit.add(preferences);
		mBar.add(file);
		mBar.add(edit);
		setJMenuBar(mBar);
		mainPanel.add(output);
		mainPanel.add(input);
		mainPanel.add(submit);
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
		
		
		output.setText("Awaiting input: ");
		con.add(mainPanel);
		
		layout.setHorizontalGroup(layout.createParallelGroup().addComponent(output).addGroup(layout.createSequentialGroup().addComponent(input).addComponent(submit)));
		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(output).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(input).addComponent(submit)));
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
					this.dispose();
				}else{
					if(again){
						output.setText("Awaiting Input: ");
						MemBank.Add(MAI.Recieve(inputText), inputText);
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
			newWindow.setBounds(50, 50, 500, 200);
			otherPanel.setBackground(Color.BLACK);
			JLabel info = new JLabel();
			info.setText("<html><body style='width: 380px'><center>About:<br /><br />MyAI was created by Braedon McCoy as a way to interact with computers differently. Please see my website for more info.</center></body></html>");
			info.setMaximumSize(new Dimension(400, 200));
			info.setForeground(Color.WHITE);
			otherPanel.add(info);
			newWindow.add(otherPanel);
			newWindow.setVisible(true);
		}else if(event.getSource() == close){
			this.dispose();
		}else if(event.getSource() == preferences){
			newWindow.setBounds(50, 50, 500, 200);
			otherPanel.setBackground(Color.BLACK);
			JLabel info = new JLabel();
			info.setText("<html><body style='width: 380px'><center>Preferences:<br /><br />Change settings here</center></body></html>");
			info.setMaximumSize(new Dimension(400, 200));
			info.setForeground(Color.WHITE);
			otherPanel.add(info);
			newWindow.add(otherPanel);
			newWindow.setVisible(true);
		}
	}
}
