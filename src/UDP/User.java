package UDP;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Map;

public class User implements Serializable{
	String hostName;//计算机名
	String username;//用户名
	String message;//信息内容
	int numbers=-1;//在线人数
	int port;
	String state="yes";
	public int getNumbers() {
		return numbers;
	}

	public void setNumbers(int numbers) {
		this.numbers = numbers;
	}

	public User(String hostName, String username) {
		super();
		this.hostName = hostName;
		this.username = username;
	}

	public User(String hostName, String username, String message) {
		super();
		this.hostName = hostName;
		this.username = username;
		this.message = message;
	}
	
	public User(String hostName, String username, String message, int port,String state) {
		super();
		this.hostName = hostName;
		this.username = username;
		this.message = message;
		this.port = port;
		this.state=state;
	}
	public User(String hostName, String username, String message, int port,int numbers) {
		super();
		this.hostName = hostName;
		this.username = username;
		this.message = message;
		this.port = port;
		this.numbers = numbers;
	}
	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
