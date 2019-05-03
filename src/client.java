
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class client extends Frame implements ActionListener{
//	static JPanel jpL1=new JPanel();//左上  !!!!!！！记住，面板位置参考对象是窗口最顶端，这不清楚的话会以为怎么找不到面板
	static JPanel jpL=new JPanel();//左中
	static JPanel jpL2=new JPanel();//左下
	static JPanel jpR1=new JPanel();//右上
	private static JTextArea jta1=new JTextArea();//聊天信息
	private static JTextArea jta2=new JTextArea();//发送信息
	private static JTextArea jf=new JTextArea();
	private static JTextArea myName=new JTextArea();//显示自己的名字
	private static JTextArea jFriends=new JTextArea();//显示在线用户的名字
	private JButton bt=new JButton("发送");
	private Socket socket=null;
	private OutputStream out=null;
	private DataOutputStream dos=null;
	private InputStream in=null;
	private DataInputStream dis=null;
	private boolean flag=false;
	private String username=null;
	private String[] s=null;//记录在线用户的名字
//	public static void main(String[] args) {
//		new client("wordpython");
//	}
	client(String username) {
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
//		jpL1.setBounds(0, 20, 170, 60);//个人头像，昵称
		jpL.setBounds(0, 80, 170, 30);//在线人数
		jpL2.setBounds(0, 110, 170, 580);//在线朋友列表
		bt.setBounds(560, 640, 70, 30);//发送按钮
//		jpL1.setBackground(Color.LIGHT_GRAY);
//		jpL.setBackground(Color.BLUE);
		jpL2.setBackground(Color.LIGHT_GRAY);
		jpR1.setBackground(Color.gray);
//		jpL1.setLayout(null);
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
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				System.out.println("用户试图关闭窗口");
				disconnect();
				System.exit(0);
			}
		});
		bt.addActionListener(this);
		this.username=username;
		connect();
		new Thread(new ReciveMessage()).start();
	}
	private class ReciveMessage implements Runnable{
		public void run() {
			flag=true;
			try {
				while(flag) {
					String message=dis.readUTF();//为什么直接报错这里，因为服务器端出错了！！那么抛出EOFException一定是因为连接断了还在继续read，什么原因导致连接断了呢？一定是业务逻辑哪里存在错误，比如NullPoint、 ClassCaseException、ArrayOutofBound，即使程序较大也没关系，最多只要单步调适一次就能很快发现bug并且解决它。
					if(!message.split(",")[0].equals("47106199133")) {
						jta1.append(message+"\n");
					}else {
						String[] s=message.split(",");
						jf.setText("在线人数："+(s.length-1));
						jFriends.setText(null);
						for(int i=1;i<s.length;i++) {
							jFriends.append(s[i]+"\n");
						}
					}
				}
			} catch (EOFException e) {
				flag = false;
				System.out.println("客户端已关闭");
			} catch (SocketException e) {
				flag = false;
				System.out.println("客户端已关闭");
			} catch (IOException e) {
				flag = false;
				System.out.println("接受消息失败");
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
			jta2.setText(null);
			sendMessageToServer(message);
		}
	}
	private void sendMessageToServer(String message) {
		try {
			dos.writeUTF(message);
			dos.flush();
		} catch (IOException e) {
			System.out.println("消息发送失败");
		}
	}
	private void connect() {
		try {
			socket=new Socket("localhost",8888);//47.106.199.133
			System.out.println("服务器ip地址："+socket.getInetAddress());
			out=socket.getOutputStream();//.........落了这里
			dos=new DataOutputStream(out);
			sendName(username);//发送名字给服务器
			in=socket.getInputStream();
			dis=new DataInputStream(in);
			allFriends();
		} catch (UnknownHostException e) {
			System.out.println("申请链接失败");
		} catch (IOException e) {
			System.out.println("连接服务器失败");
		}
	}
	//读取在线人数以及名字
	private void allFriends() throws IOException {
		String allName=dis.readUTF();//allName="47106199133,name1,name2,...nameN,"
		s=allName.split(",");
		jf.setText("在线人数："+(s.length-1));
		jFriends.setText(null);
		for(int i=1;i<s.length;i++) {
			jFriends.append(s[i]+"\n");
		}
	}
	//发送名字
	private void sendName(String username2) throws IOException {
		dos.writeUTF(username2);
		dos.flush();
	}
	private void disconnect() {
		flag = false;
		if (dos != null) {
			try {
				dos.close();
			} catch (IOException e) {
				System.out.println("dos关闭失败");
				e.printStackTrace();
			}
		}
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				System.out.println("dos关闭失败");
				e.printStackTrace();
			}
		}
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("socket关闭失败");
				e.printStackTrace();
			}
		}
	}
}
