package UDP;

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

public class serverTwo {
	private Map<String,String> map=new HashMap<>();
//	private Map<Integer,String> map=new HashMap<>();
	private Map<String,String> mapHostName=new HashMap<>();
	private Map<String,Integer> userName=new HashMap<>();
	public static void main(String[] args) {
		new serverTwo();
	}
	private serverTwo() {
		try {
			DatagramPacket pkt;
			DatagramSocket skt = new DatagramSocket(8000);
			System.out.println("服务器已开");
			while(true) {
				byte[] inBuf=new byte[256];
				pkt = new DatagramPacket(inBuf, inBuf.length);
				skt.receive(pkt);
				ByteArrayInputStream bais=new ByteArrayInputStream(inBuf);
				ObjectInputStream ois=new ObjectInputStream(bais);
				Object object=ois.readObject();
				User user=(User)object;
				//判断是消息还是登录信息
				if(user.message!=null) {
					System.out.println(user.message);
					sendToAll(user);
				}else if(user.state.equals("no")) {
//					map.remove(user.port);
					map.remove(user.hostName);
					mapHostName.remove(user.username);
					userName.remove(user.username);
					User user1=new User(null,null,null,0,map.size());
					System.out.println("在线人数："+map.size()+" 端口："+user.port);
					sendToAll(user1);
				}
				else {
					Integer sss=0;
					for (String username : map.values()) {//一个用户名只能登录一次
						if(username.equals(user.username)) {
							sss=1;
						}
					}
					if(sss.equals(0)) {
//						map.put(user.port, user.username);
						map.put(user.hostName, user.username);
						mapHostName.put(user.username, user.hostName);
						userName.put(user.username, user.port);
//						user.setNumbers(map);//对象里面有对象，就会报错，
						User user1=new User(null,null,null,0,map.size());
						System.out.println("在线人数："+map.size());
						sendAllNumberTo(user1);
					}
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
	private void sendAllNumberTo(User user) {//将在线人数发送过去
		try {
			for (String username : map.values()) {
				DatagramSocket skt = new DatagramSocket();
				InetAddress address = InetAddress.getByName(mapHostName.get(username));
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(user);//如果这里写到for循环里面，会出现反序列化时报异常java.io.EOFException，解决方法：一，写到for循环外；二，加上这个oos.writeObject(null)，读取的时候要while ((obj = ois.readObject()) != null);三，将若干个对象（数量不定）都装入 一个容器中（如：ArrayList之类的），然后写对象的时候，将该容器写入。四，将对象都存入数组中，然后写入数组对象。 
				oos.writeObject(null);
				byte[] outBuf = baos.toByteArray();
				DatagramPacket pkt = new DatagramPacket(outBuf, outBuf.length, address, userName.get(username));// 发送消息
				skt.send(pkt);
				skt.close();
				System.out.println("发送人数成功："+username+" :"+userName.get(username));
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void sendToAll(User user) {
		for(String username:map.values()) {
			if(!username.equals(user.username)) {
				try {
					DatagramSocket skt = new DatagramSocket();
					InetAddress address = InetAddress.getByName(mapHostName.get(username));
					ByteArrayOutputStream baos=new ByteArrayOutputStream();
					ObjectOutputStream oos=new ObjectOutputStream(baos);
					oos.writeObject(user);
					byte[] outBuf=baos.toByteArray();
					DatagramPacket pkt=new DatagramPacket(outBuf, outBuf.length,address,userName.get(username));//发送消息
					skt.send(pkt);
					skt.close();
					System.out.println("转发成功："+username+" "+userName.get(username));
				} catch (SocketException e) {
					e.printStackTrace();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println("当前用户："+user.username+" "+username);
		}
	}
}