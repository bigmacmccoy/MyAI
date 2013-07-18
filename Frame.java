import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Frame extends JFrame implements ActionListener{
	JLabel output = new JLabel("");
	JTextField input = new JTextField();
	JPanel total = new JPanel();
	JButton submit = new JButton("Submit");
	String OS = "";
	
	String inputText = "";
	
	public Frame(String str){
		OS = str;
		GroupLayout layout = new GroupLayout(total);
		total.setLayout(layout);
		this.setBounds(0, 0, 500, 500);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container con = this.getContentPane(); // inherit main frame
		total.add(output);
		total.add(input);
		total.add(submit);
		input.addActionListener(this);
		submit.addActionListener(this);
		output.setText("Awaiting input: ");
		con.add(total);
		layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(output).addGroup(layout.createSequentialGroup().addComponent(input).addComponent(submit)));
		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(output).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(input).addComponent(submit)));
		setVisible(true); // make frame visible
	}
	public void print(String str){
		output.setText(str);
	}
	public String waitForInput(){
		String cur = input.getText();
		while(cur.equalsIgnoreCase("")){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			cur = input.getText();
		}
		return cur;
	}
	@Override
	public void actionPerformed(ActionEvent event) {
		if((event.getSource() == submit) || (event.getSource() == input)){
			if(input.getText().length() > 0){
				inputText = input.getText();
				input.setText("");
				if(inputText.equalsIgnoreCase("exit")){
					System.exit(0);
				}else{
					AICore MAI =  new AICore();
					Command current =  new Command();
					current = MAI.Recieve(inputText);
					current = MAI.Process(current);
					if(current == null){
						//print("No Match Found.");
						return;
					}
					boolean success = MAI.Run(current, OS);
					if(success){
						//print("Operation Complete.");
					}else{
						//print("Operation Failed.");
					}
				}
			}
		}
	}
}
