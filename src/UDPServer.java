import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class UDPServer extends Frame implements ActionListener{
	static JPanel jpL=new JPanel();//左中
	static JPanel jpL2=new JPanel();//左下
	static JPanel jpR1=new JPanel();//右上
	private static JTextArea jta1=new JTextArea();//聊天信息
	private static JTextArea jta2=new JTextArea();//发送信息
	private static JTextArea jf=new JTextArea();
	private static JTextArea myName=new JTextArea();
	private static JTextArea jFriends=new JTextArea();
	private JButton bt=new JButton("发送");
	public static void main(String[] args) {
		new UDPServer("wordjava");
	}
	UDPServer(String username) {
		setBounds(500, 50, 700, 680);
		setVisible(true);
		setTitle("服务器 聊天室");
		jta1.setLineWrap(true);    //设置自动换行,自动换行则不会出现横向的滚动条
		jta2.setLineWrap(true);    //设置自动换行,自动换行则不会出现横向的滚动条
		jta1.setEditable(false);    //设置可编辑
		
		JScrollPane jpR=new JScrollPane(jta1);//右中
		JScrollPane jpR2=new JScrollPane(jta2);//右下
		setLayout(null);
		jpR1.setBounds(170, 0, 500, 80);
		jpR.setBounds(170, 80, 500, 410);//聊天信息的面板
		jpR2.setBounds(170, 490, 500, 150);//发送信息的面板
		jta1.setSize(500,410);//聊天信息区
		jta2.setBounds(0,20,500,40);//发送信息区
		jpL.setBounds(0, 80, 170, 30);//在线人数
		jpL2.setBounds(0, 110, 170, 580);//在线朋友列表
		bt.setBounds(560, 640, 70, 30);//发送按钮
//		jpL1.setBackground(Color.LIGHT_GRAY);
		jpL.setBackground(Color.BLUE);
		jpL2.setBackground(Color.LIGHT_GRAY);
		jpR1.setBackground(Color.gray);
		jf.setEditable(false);
		jFriends.setEditable(false);
		jf.setSize(110,20);
		jf.setText("在线人数：");
		Font font1=new Font("宋体",Font.ITALIC,18);
		jf.setFont(font1);
		myName.setEditable(false);
		myName.setBounds(30,40,120,30);
		myName.setText(username);
		Font font2=new Font("宋体",Font.BOLD,20);
		myName.setFont(font2);
		add(myName);
		jpL.add(jf);
		add(bt);
		add(jpR1);
		add(jpL);
		add(jpL2);
		add(jpR2);
		add(jpR);
		
		jta1.setMargin(new Insets(5,10,0,10));//设置上下左右边距为10。
		jta2.setMargin(new Insets(5,10,0,10));
		setResizable(false);
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				System.out.println("服务器关闭窗口");
				System.exit(0);
			}
		});
		bt.addActionListener(this);
		new Thread(new ReciveMessage()).start();
	}
	private class ReciveMessage implements Runnable{
		public void run() {
			// TODO Auto-generated method stub
			//接收客户端的信息
			try {
				String str;
				DatagramSocket skt=new DatagramSocket(8000);
				while(true) {
					byte[] inBuf=new byte[256];
					DatagramPacket pkt;
					pkt=new DatagramPacket(inBuf,inBuf.length);
					skt.receive(pkt);
					str=new String(pkt.getData());
					str=str.trim();
					System.out.println(str);
					if(str.length()>0) {
						int pot=pkt.getPort();
						jta1.append(pot+":"+str+"\n");
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return;
			}
		}
		
	}
	public void actionPerformed(ActionEvent e) {
		String message=jta2.getText().trim();
		if(message!=null && !message.equals("")) {
			String time=new SimpleDateFormat("h:m:s").format(new Date());
			time="                                     ------"+time+"------                                     ";
			jta1.append(time+"\n我:  "+message+"\n");
			jta2.setText(null);
			sendMessageToServer(message);
		}
	}
	private void sendMessageToServer(String message) {
		// TODO Auto-generated method stub
		try {
			System.out.println("发送成功");
			String serverName="localhost";
			DatagramSocket skt=new DatagramSocket();//建立UDP socket对象
			DatagramPacket pkt;
			byte[] outBuf=new byte[message.length()];
			outBuf=message.getBytes();
			InetAddress address=InetAddress.getByName(serverName);
			pkt=new DatagramPacket(outBuf,outBuf.length,address,9000);
			skt.send(pkt);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("消息发送失败");
		}
	}
}
