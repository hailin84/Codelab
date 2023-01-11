package org.alive.learn.ui.swing;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class GroupBoxDemo {
	public GroupBoxDemo() {
		new SmallFrame().setVisible(true);
		// new JFrameBackground().setVisible(true);
	}
}

class SmallFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5793089288348196222L;
	Container p;
	JLabel tip = new JLabel("请输入学号：");
	JTextField stuNoText = new JTextField();
	JPanel viewPane = new JPanel();
	JButton ok = new JButton("确定");
	JButton reset = new JButton("重置");

	public SmallFrame() {
		super("按学号查询");
		setBounds(200, 100, 350, 280);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		p = getContentPane();
		p.setLayout(null);
		tip.setBounds(70, 20, 100, 20);
		tip.setForeground(Color.red);
		stuNoText.setBounds(155, 20, 100, 20);
		p.add(tip);
		p.add(stuNoText);
		ok.setBounds(90, 60, 60, 20);
		ok.setForeground(Color.green);
		p.add(ok);
		reset.setBounds(180, 60, 60, 20);
		reset.setForeground(Color.green);
		p.add(reset);
		// 关键代码块
		TitledBorder tb = new TitledBorder(
				BorderFactory.createLineBorder(new Color(255, 0, 0)), "查询结果");
		tb.setTitleColor(Color.blue);
		viewPane.setBorder(tb);
		viewPane.setLayout(null);
		JLabel stuNoLabe = new JLabel("学号：");
		stuNoLabe.setBounds(80, 130, 80, 20);
		stuNoLabe.setForeground(new Color(34, 139, 34));
		p.add(stuNoLabe);
		JTextField stuNoTextf = new JTextField();
		stuNoTextf.setBounds(150, 130, 90, 20);
		p.add(stuNoTextf);
		JLabel stuNameLabe = new JLabel("姓名：");
		stuNameLabe.setBounds(80, 160, 80, 20);
		stuNameLabe.setForeground(new Color(34, 139, 34));
		p.add(stuNameLabe);
		JTextField nameTextf = new JTextField();
		nameTextf.setBounds(150, 160, 90, 20);
		p.add(nameTextf);
		JLabel stuAgeLabe = new JLabel("年龄：");
		stuAgeLabe.setBounds(80, 190, 80, 20);
		stuAgeLabe.setForeground(new Color(34, 139, 34));
		p.add(stuAgeLabe);
		JTextField ageTextf = new JTextField();
		ageTextf.setBounds(150, 190, 90, 20);
		p.add(ageTextf);

		viewPane.add(new JButton("HKJJ"));
		viewPane.setBounds(18, 100, 310, 141);
		p.add(viewPane);
	}
}

class JFrameBackground extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7840720468434417100L;

	public JFrameBackground() {
		this.setTitle("我的swing界面");
		this.setLayout(new FlowLayout());
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createTitledBorder("分组框")); // 设置面板边框，实现分组框的效果，此句代码为关键代码

		buttonPanel.setBorder(BorderFactory.createLineBorder(Color.red));// 设置面板边框颜色
		JButton button = new JButton("我的按钮");
		buttonPanel.add(button);
		this.setSize(300, 300);
		this.getContentPane().add(buttonPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new JFrameBackground();
	}

}