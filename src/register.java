import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class register implements ActionListener{
	static JFrame jfrm=new JFrame("注册wordpython");
	static JButton loginBt=new JButton("登录");
	static JButton registerBt=new JButton("注册");
	static JTextField jname=new JTextField();
	static JPasswordField jpwd=new JPasswordField();
	static JPasswordField jpwd2=new JPasswordField();
	static JLabel jl1=new JLabel("昵称");
	static JLabel jl2=new JLabel("密码");
	static JLabel jl22=new JLabel("确认密码");
	register(){
		jfrm.setBounds(200, 200, 350, 300);
		jfrm.setLocationRelativeTo(null);
		jname.setBounds(65,20,250,40);
		jl1.setBounds(10, 23, 40, 30);
		jpwd.setBounds(65, 80, 250,40);
		jl2.setBounds(10, 83, 40, 30);
		jpwd2.setBounds(65,140,250,40);
		jl22.setBounds(5, 143, 55, 30);
		loginBt.setBounds(65, 200, 110, 50);
		registerBt.setBounds(205, 200, 110, 50);
		jfrm.setLayout(null);
		jfrm.setLayout(null);
		jfrm.add(jpwd2);
		jfrm.add(jl22);
		jfrm.add(jl1);
		jfrm.add(jl2);
		jfrm.add(loginBt);
		jfrm.add(registerBt);
		jfrm.add(jname);
		jfrm.add(jpwd);
		jfrm.setResizable(false);
		jfrm.setVisible(true);
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//设置字体
		Font font=new Font("宋体",Font.BOLD+Font.ITALIC,18);
		jl1.setFont(font);
		jl2.setFont(font);
		jname.setFont(font);
		jpwd.setFont(font);
		jpwd2.setFont(font);
		jname.setMargin(new Insets(5,10,5,5));//设置上左下右内边距
		jpwd.setMargin(new Insets(5,10,5,5));
		jpwd2.setMargin(new Insets(5,10,5,5));
		//添加监听事件
		loginBt.addActionListener(this);
		registerBt.addActionListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==loginBt) {//触发登录按钮
			jfrm.setVisible(false);
			new login().jfrm.setVisible(true);
			jfrm.dispose();
		}
		if(e.getSource()==registerBt) {//触发注册按钮
			jfrm.setVisible(false);
			new login().jfrm.setVisible(true);
			jfrm.dispose();//本窗口销毁,释放内存资源
		}
	}
}
