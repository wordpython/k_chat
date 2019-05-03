import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class server {
	private List<Client> clients=new ArrayList<>();
	private Map<String,String> map=new HashMap<>();
	public static void main(String[] arg) {
		new server().init();
	}
	private void init() {
		System.out.println("服务器已开启");
		ServerSocket ss=null;
		Socket socket=null;
		try {
			ss=new ServerSocket(8888);
		}catch(BindException e) {
			System.out.println("端口已被占用");
		}catch(IOException e1) {
			e1.printStackTrace();
		}
		try {
			Client client = null;
			while (true) {
				socket = ss.accept();//等候客户端请求
				System.out.println("客户端连接");
				//接收客户名字，并发送在线人数
				String username=new DataInputStream(socket.getInputStream()).readUTF();
				client = new Client(socket);
				clients.add(client);
				//给客户发送在线人数和名字
				map.put(""+socket.getPort(), username);
				allFriends(map);
				//开启线程，接收和发送消息
				new Thread(client).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void allFriends(Map<String, String> map2) throws IOException {
		String allName="";
		for(String name:map.values()) {
			allName=allName+name+",";
		}
		for(Client c:clients) {
			OutputStream out=c.socket.getOutputStream();
			DataOutputStream dos=new DataOutputStream(out);
			dos.writeUTF("47106199133,"+allName);
			dos.flush();
		}
	}
	private class Client implements Runnable{
		private Socket socket=null;
		InputStream in=null;
		DataInputStream din=null;
		OutputStream out=null;
		DataOutputStream dos=null;
		boolean flag=true;
		public Client(Socket socket) {
			this.socket=socket;
			try {
				in=socket.getInputStream();
				din=new DataInputStream(in);
			}catch(IOException e) {
				System.out.println("接受消息失败");
			}
		}
		public void run() {
			String message;
			try {
				while (flag) {//这里写到try的上面了，导致客户端连接中断
					message = din.readUTF();//获取客户端消息
					forwordToAllClients(message);//发送给其他用户
				}
			} catch (SocketException e) {
				flag = false;
				System.out.println("客户下线");
				clients.remove(this);
				map.remove(""+socket.getPort());
			} catch (EOFException e) {
				flag = false;
				System.out.println("客户下线");
				map.remove(""+socket.getPort());
				clients.remove(this);
				try {
					allFriends(map);
				} catch (IOException e1) {
					System.out.println("用户下线，但修改在线人数失败");
				}
			} catch (IOException e) {
				flag = false;
				System.out.println("接受消息失败");
				clients.remove(this);
				map.remove(""+socket.getPort());
			}
			if (din != null) {
				try {
					din.close();
				} catch (IOException e) {
					System.out.println("din关闭失败");
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					System.out.println("din关闭失败");
				}
			}
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					System.out.println("din关闭失败");
				}
			}
		}
		private void forwordToAllClients(String message) throws IOException {
			String mes=null;
			for(Client c:clients) {
				if(c!=this) {
					mes=map.get(socket.getPort()+"")+": "+message;//这里有什么有的端口号会发送两次？？？？？
					out=c.socket.getOutputStream();
					dos=new DataOutputStream(out);
					forwordToClient(mes);
				}
			}
		}
		private void forwordToClient(String message) throws IOException {
			dos.writeUTF(message);
			dos.flush();
		}
	}
}
