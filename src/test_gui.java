import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class test_gui implements ActionListener{
	private TextField textFieldContent=new TextField();
	static JFrame jfrm=new JFrame();
	static JPanel jpL1=new JPanel();//左上
	static JPanel jpL=new JPanel();//左中
	static JPanel jpL2=new JPanel();//左下
	static JPanel jpR1=new JPanel();//右上
	private static JTextArea jta1=new JTextArea();
	private static JTextArea jta2=new JTextArea();
	private static JTextArea jf=new JTextArea();
	private static JTextArea jFriends=new JTextArea();
	private JButton bt=new JButton("发送");
	private TextArea textAreaContent =new TextArea();
	private Socket socket=null;
	private OutputStream out=null;
	private DataOutputStream dos=null;
	private InputStream in=null;
	private DataInputStream dis=null;
	private boolean flag=false;
	public static void main(String[] args) {
		new test_gui("wordpython");
	}
	test_gui(String username){
		JScrollPane jpR=new JScrollPane(jta1);//右中
		JScrollPane jpR2=new JScrollPane(jta2);//右下
		// TODO Auto-generated method stub
		jfrm.setBounds(500, 50, 700, 680);
		jfrm.setLayout(null);
		jpR1.setBounds(170, 0, 500, 40);
		jpR.setBounds(170, 40, 500, 410);//聊天信息的面板
		jpR2.setBounds(170, 450, 500, 150);//发送信息的面板
		jta1.setSize(500,410);
		jta2.setBounds(2,20,500,40);
		jpL1.setBounds(0, 0, 170, 40);
		jpL.setBounds(0, 40, 170, 30);
		jpL2.setBounds(0, 70, 170, 580);
		bt.setBounds(560, 600, 70, 30);
		jta2.setLineWrap(true);
		jpL.setBackground(Color.green);
		jpL1.setBackground(Color.LIGHT_GRAY);
		jpL2.setBackground(Color.gray);
		jpR1.setBackground(Color.red);
		jfrm.add(bt);
		jfrm.add(jpR1);
		jfrm.add(jpL1);
		jfrm.add(jpL);
		jfrm.add(jpL2);
		jfrm.add(jpR2);
		jfrm.add(jpR);
		jfrm.setVisible(true);
		jfrm.setResizable(true);
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
