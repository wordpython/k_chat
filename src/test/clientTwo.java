package test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class clientTwo extends Frame{
	static JPanel jpL=new JPanel();//左中
	static JPanel jpL2=new JPanel();//左下
	static JPanel jpR1=new JPanel();//右上
	static JPanel jpl=new JPanel();//显示表情的面板
	private static JTextPane jta1=new JTextPane();//聊天信息
	private static JTextPane jta2=new JTextPane();//发送信息
	private static JTextArea jf=new JTextArea();
	private static JTextArea myName=new JTextArea();//显示自己的名字
	private static JTextArea jFriends=new JTextArea();//显示在线用户的名字
	private JButton bt=new JButton("发送");
	private JButton bt1=new JButton("表情");
	private DatagramSocket skt;
	private String address;
	private static int m=27;//表情数
	private static JLabel[] jll=new JLabel[m];//显示表请的位置
	private static Icon icon;
	private static Map<Integer, Integer> al=new HashMap<>();//记录表情的位置和文件名
	public static void main(String[] args) {
		new clientTwo("wordpython");
	}
	clientTwo(String username) {
		setVisible(true);
		setBounds(500, 50, 700, 680);
		setTitle("wordpython 聊天室");

		jpl.setBounds(170, 200, 300, 300);
		jpl.setVisible(true);
		GridLayout grid=new GridLayout(5,5);
		jpl.setLayout(grid);
		jpl.setVisible(false);
		add(jpl);
		
		jta1.setEditable(false);    //设置可编辑
		JScrollPane jpR=new JScrollPane(jta1);//消息历史区
		JScrollPane jpR2=new JScrollPane(jta2);//消息编辑器
		setLayout(null);
		jpR1.setBounds(170, 0, 500, 80);
		jpR.setBounds(170, 80, 500, 410);//聊天信息的面板
		jpR2.setBounds(170, 490, 500, 150);//发送信息的面板
		jta1.setSize(500,410);//聊天信息区
		jta2.setBounds(0,20,500,40);//发送信息区
		jpL.setBounds(0, 80, 170, 30);//在线人数
		jpL2.setBounds(0, 110, 170, 580);//在线朋友列表
		bt.setBounds(560, 640, 70, 30);//发送按钮
		bt1.setBounds(400,640,70,30);
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
		add(bt1);
		add(jpR1);
		add(jpL);
		add(jpL2);
		add(jpR2);
		add(jpR);
		
		jta1.setMargin(new Insets(5,10,0,10));//设置上下左右边距为10。
		jta2.setMargin(new Insets(5,10,0,10));
		setResizable(false);

		/*
		 * 将图片的文件名存入数组，因为文本中图片的位置时空格，所以以空格为切割符，进行切割
		 * 当选择表情后又删除时，要同时把它从al中移除
		 */
		//在面板中添加图片
		for(int i=0;i<m;i++) {
			jll[i]=new JLabel();
			icon=new ImageIcon("src/test/1/"+i+".png");
			jll[i].setIcon(icon);
//			jll[i].setSize(icon.getIconWidth(), icon.getIconHeight());
			jpl.add(jll[i],new Integer(Integer.MIN_VALUE));
			final int a=i;
			jll[i].addMouseListener(new MouseListener() {
				public void mouseClicked(MouseEvent e) {//单击鼠标
					//在文本区中添加图片
					jta2.insertIcon(new ImageIcon("src/test/1/"+a+".png"));//插入图片，在文本中是 空格
					al.put(jta2.getText().length(), a);
					jpl.setVisible(false);
				}
				public void mousePressed(MouseEvent e) {
				}
				public void mouseReleased(MouseEvent e) {
				}
				public void mouseEntered(MouseEvent e) {//鼠标进入指定区域时
					jll[a].setBackground(Color.yellow);
				}
				public void mouseExited(MouseEvent e) {
				}
			});
		}
		//点击表情按钮，显示表情面板
		bt1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jpl.setVisible(true);
			}
		});
		//设置字体大小
        SimpleAttributeSet attrset = new SimpleAttributeSet();
        StyleConstants.setFontSize(attrset,24);
		//点击发送按钮
		bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Document doc = jta1.getDocument();
				String str = jta2.getText();
				String[] str1 = str.split("");
				if (al.size() != 0) {// 有表情
					for (int j = 0; j < str1.length; j++) {
						try {
							doc.insertString(doc.getLength(), str1[j], attrset);
							if (al.get(j + 1) != null) {
								jta1.insertIcon(new ImageIcon("src/test/1/" + al.get(j + 1) + ".png"));
							}
						} catch (BadLocationException e1) {
							e1.printStackTrace();
						}
					}
					try {
						doc.insertString(doc.getLength(), "\n", attrset);
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
					al.clear();
				} else {// 没有表情，则先把前后空格去掉，再判空
					if (!str.equals("") && str != null) {
						try {
							doc.insertString(doc.getLength(), str + "\n", attrset);
						} catch (BadLocationException e1) {
							e1.printStackTrace();
						}
					}
				}
				sendMessageToServer(address,username,str,al,9001,"yes");
				jta2.setText(null);
			}
		});
		try {
			address=InetAddress.getLocalHost().getHostName();
			skt = new DatagramSocket(9001);
			sendMessageToServer(address,username,"",al,9001,"yes");//将客户端本地计算机名发送给服务器
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (SocketException e1) {
			e1.printStackTrace();
		}

		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				System.out.println("用户试图关闭窗口");
				//用户关闭之前发送关闭信息给服务器
				sendMessageToServer(address,username,"",al,9001,"no");
				System.exit(0);
			}
		});
		new Thread(new ReciveMessage()).start();
	}
	private class ReciveMessage implements Runnable{
		public void run() {
			try {
				DatagramPacket pkt;
				while(true) {
					byte[] inBuf=new byte[256];
					pkt = new DatagramPacket(inBuf, inBuf.length);
					skt.receive(pkt);
					//将字节数组转换成对象（反序列化）
					//基类流：内存流  
					ByteArrayInputStream bais=new ByteArrayInputStream(inBuf);
					//封装流：对象流
					ObjectInputStream ois=new ObjectInputStream(bais);
					//内存输入流 读取对象消息
					Object object = ois.readObject();
					// 向下转型，获取对象消息
					User user = (User) object;
					System.out.println("22");
					if (user.numbers != -1) {// 发过来的是在线人数
						System.out.println("发过来的是在线人数:"+user.numbers);
						jf.setText("在线人数为：" + user.numbers);
					} else {
						System.out.println("表情数："+al.size());
						Document doc = jta1.getDocument();
				        SimpleAttributeSet attrset = new SimpleAttributeSet();
				        StyleConstants.setFontSize(attrset,24);
						doc.insertString(doc.getLength(), "\n", attrset);
						jta1.insertIcon(new ImageIcon("src/test/1/" + al.get(1) + ".png"));
					}
				}
			} catch (SocketException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}
	private void sendMessageToServer(String hostName,String username,String message,Map<Integer, Integer> al,int port,String state) {//发送消息的方法
		try {
			DatagramSocket skt = new DatagramSocket();
//			User user=new User("1", username,message,0,port,state,al);//发送的对象
			User user=new User("",username,message,al,port,state,0);//"1"时：$BlockDataInputStream.peekByte ?为什么username就可以
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
			System.out.println("发送消息: "+hostName+" 信息为:"+message);
			} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
