import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class login implements ActionListener{
	static JFrame jfrm=new JFrame("登录wordpython");
	static JButton loginBt=new JButton("登录");
	static JButton registerBt=new JButton("注册");
	static JTextField jname=new JTextField();
	static JPasswordField jpwd=new JPasswordField();
	static JLabel jl1=new JLabel("昵称");
	static JLabel jl2=new JLabel("密码");
	login(){
		jfrm.setBounds(200, 200, 350, 270);
		jfrm.setLocationRelativeTo(null);
		jname.setBounds(65,20,250,40);
		jl1.setBounds(10, 23, 40, 30);
		jpwd.setBounds(65, 90, 250,40);
		jl2.setBounds(10, 93, 40, 30);
		loginBt.setBounds(65, 160, 110, 50);
		registerBt.setBounds(205, 160, 110, 50);
		jfrm.setLayout(null);
		jfrm.add(jl1);
		jfrm.add(jl2);
		jfrm.add(loginBt);
		jfrm.add(registerBt);
		jfrm.add(jname);
		jfrm.add(jpwd);
		jfrm.setVisible(true);
		jfrm.setResizable(false);
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//设置字体
		Font font=new Font("宋体",Font.BOLD+Font.ITALIC,18);
		jl1.setFont(font);
		jl2.setFont(font);
		jname.setFont(font);
		jpwd.setFont(font);
		jname.setMargin(new Insets(5,10,5,5));//设置上左下右内边距
		jpwd.setMargin(new Insets(5,10,5,5));
		//添加监听事件
		loginBt.addActionListener(this);
		registerBt.addActionListener(this);
	}
	public static void main(String[] args) {
		new login();
	}
	public void actionPerformed(ActionEvent e) {
		String username=jname.getText();
		if(e.getSource()==loginBt&&!username.equals("")) {//触发登录按钮
			jfrm.setVisible(false);
			new client(username).setVisible(true);
			jfrm.dispose();
		}
		if(e.getSource()==registerBt) {//触发注册按钮
			jfrm.setVisible(false);
			new register().jfrm.setVisible(true);
			jfrm.dispose();//本窗口销毁,释放内存资源
		}
	}
}
