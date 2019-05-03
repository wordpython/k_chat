package UDP;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class clientOne extends Frame implements ActionListener{
	static JPanel jpL=new JPanel();//左中
	static JPanel jpL2=new JPanel();//左下
	static JPanel jpR1=new JPanel();//右上
	private static JTextArea jta1=new JTextArea();//聊天信息
	private static JTextArea jta2=new JTextArea();//发送信息
	private static JTextArea jf=new JTextArea();
	private static JTextArea myName=new JTextArea();//显示自己的名字
	private static JTextArea jFriends=new JTextArea();//显示在线用户的名字
	private JButton bt=new JButton("发送");
	private String username=null;
	private String[] s=null;//记录在线用户的名字
	private DatagramSocket skt;
	private String address;
	public static void main(String[] args) {
		new clientOne("wordpython1");
	}
	clientOne(String username) {
		setVisible(true);
		setBounds(500, 50, 700, 680);
		setTitle("wordpython 聊天室");
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
		jpL2.setBackground(Color.LIGHT_GRAY);
		jpR1.setBackground(Color.gray);
		jf.setEditable(false);
		jf.setMargin(new Insets(0,5,0,10));
		jFriends.setEditable(false);
		jf.setSize(150,20);
		Font font1=new Font("宋体",Font.ITALIC,18);
		jf.setFont(font1);
		myName.setEditable(false);
		myName.setBounds(30,40,120,30);
		myName.setText(username);
		Font font2=new Font("宋体",Font.BOLD,20);
		myName.setFont(font2);
		jFriends.setSize(110,360);
		jFriends.setFont(font2);
		jFriends.setBackground(Color.lightGray);
		add(myName);
		jpL.add(jf);
		jpL2.add(jFriends);
		add(bt);
		add(jpR1);
		add(jpL);
		add(jpL2);
		add(jpR2);
		add(jpR);
		
		jta1.setMargin(new Insets(5,10,0,10));//设置上下左右边距为10。
		jta2.setMargin(new Insets(5,10,0,10));
		setResizable(false);
		bt.addActionListener(this);
		this.username=username;
		
		try {
			address=InetAddress.getLocalHost().getHostName();
			skt = new DatagramSocket(9000);
			sendMessageToServer(address,username,null,9000,"yes");//将客户端本地计算机名发送给服务器
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (SocketException e1) {
			e1.printStackTrace();
		}

		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				System.out.println("用户试图关闭窗口");
				//用户关闭之前发送关闭信息给服务器
				sendMessageToServer(address,username,null,9000,"no");
				System.exit(0);
			}
		});
		new Thread(new ReciveMessage()).start();
	}
	private class ReciveMessage implements Runnable{
		public void run() {
			try {
				String str;
				DatagramPacket pkt;
				while(true) {
					byte[] inBuf=new byte[256];
					pkt = new DatagramPacket(inBuf, inBuf.length);
					skt.receive(pkt);
					System.out.println("00");
					//将字节数组转换成对象（反序列化）
					//基类流：内存流  
					ByteArrayInputStream bais=new ByteArrayInputStream(inBuf);
					//封装流：对象流
					ObjectInputStream ois=new ObjectInputStream(bais);
					//内存输入流 读取对象消息
					System.out.println("11");
					Object object = ois.readObject();
					// 向下转型，获取对象消息
					User user = (User) object;
					System.out.println("22");
					if (user.numbers != -1) {// 发过来的是在线人数
						jf.setText("在线人数为：" + user.numbers);
					} else {
						str = user.getMessage();
						jta1.append(str + "\n");
					}
				}
			} catch (SocketException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	public void actionPerformed(ActionEvent e) {
		String message=jta2.getText().trim();
		if(message!=null && !message.equals("")) {
			String time=new SimpleDateFormat("h:m:s").format(new Date());
			time="                                     ------"+time+"------                                     ";
			jta1.append(time+"\n我:  "+message+"\n");
			message=username+" >>> "+message;
			jta2.setText(null);
			sendMessageToServer(address,username,message,9000,"yes");
		}
	}
	private void sendMessageToServer(String hostName,String username,String message,int port,String state) {//发送消息的方法
		try {
			DatagramSocket skt = new DatagramSocket();
			User user=new User(hostName, username,message,port,state);//发送的对象
			//对象流  写  对象输出流
	        //内存流（基类流）
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			//对象流（封装流）因为传输对象
			ObjectOutputStream oos=new ObjectOutputStream(baos);
			//将对象写入对象流
			oos.writeObject(user);
			//将对象转化为字节数组，因为数据包需要字节数组，该方法属于字节流
			byte[] buf=baos.toByteArray();
			//将字节数组的数据放入数据包
			DatagramPacket pkt = new DatagramPacket(buf, buf.length,InetAddress.getByName("DESKTOP-PMALJHC"),8000);
			skt.send(pkt);
			skt.close();
			System.out.println("发送消息: "+InetAddress.getByName("DESKTOP-PMALJHC")+" :"+8000);
			} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
