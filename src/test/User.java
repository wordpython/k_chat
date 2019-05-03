package test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

public class User implements Serializable{
	String hostName;//计算机名
	String username;//用户名
	String message;//信息内容
	int numbers;//在线人数
	int port;
	String state="yes";//在线状态
	Map<Integer,Integer> al;//记录表情的位置和文件名

	public User(String hostName, String username, String message,Map<Integer,Integer> al, int port,String state,int numbers) {
		super();
		this.hostName = hostName;
		this.username = username;
		this.message = message;
		this.port = port;
		this.state=state;
		this.al=al;
		this.numbers=numbers;
	}
	public User(String hostName, String username, String message, int port,int numbers) {
		super();
		this.hostName = hostName;
		this.username = username;
		this.message = message;
		this.port = port;
		this.numbers = numbers;
	}

	private void writeObject(final ObjectOutputStream out) throws IOException {
		out.writeUTF(this.hostName);
		out.writeUTF(this.username);
		out.writeUTF(this.message);
		out.writeInt(this.numbers);
		out.writeInt(this.port);
		out.writeUTF(this.state);
		out.writeObject(this.al);
	}
	//发现发送的信息中大于两位时，服务就报错！！
	@SuppressWarnings("unchecked")
	private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.hostName=in.readUTF();
		this.username=in.readUTF();
		this.message=in.readUTF();
		this.numbers=in.readInt();
		this.port=in.readInt();
		this.state=in.readUTF();
		this.al = (Map<Integer, Integer>) in.readObject();
	} 
}
