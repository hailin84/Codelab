package org.alive.learn.heartbeat;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.alive.tools.socketmessage.UIUpdater;
import org.alive.tools.socketmessage.UIUtil;

/**
 * Swing界面 
 * 
 * @author hailin84
 * @since 2017.06.15
 */
public class LongConnClientUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4311823576466134915L;

	// -------------- 静态变量
	private static final String PROFILE = "profile-nio.ini";
	private static final Insets INSETS = new Insets(1, 1, 1, 1);
	private static final Color TB_COLOR = UIUtil.parseColor("#3e62a6");

	/** 是否添加消息长度到消息头部选项 */
	public static final String[] LENGTH_OPTIONS = new String[] { "12字节消息头部" };

	/** 消息编码下拉框 */
	public static final String[] ENCODING_OPTIONS = new String[] { "GBK", "UTF-8" };
	
	/** 消息是明文还是密文 */
	public static final String[] MSG_CATEGORY = new String[] { "明文", "密文" };

	// -------------- Swing UI component
	private JLabel profileLabel;

	private JComboBox<String> profileCombo;

	private JButton saveProfileButton;

	private JLabel ipLabel;

	private JTextField ipField;

	private JLabel portLabel;

	private JTextField portField;

	private JLabel encodingLabel;

	private JComboBox<String> encodingCombo;
	
	private JLabel msgCategoryLabel;

	private JComboBox<String> msgCategoryCombo;

	private JLabel lengthLabel;

	private JComboBox<String> lengthCombo;

	private JButton send;

	private JLabel lengthMsg;

	private JLabel statusMsg;

	private JTextArea requestMessage;

	private JTextArea responseMessage;

	private JButton connectOrDisconnect;

	private boolean connected = false;

	// Data fields
	private Set<String> profiles = new HashSet<String>();

	private ClientStarter starter = null;
	
	public LongConnClientUI() {
		// 初始化界面
		this.initialize();

		// 加载数据
		this.loadData();

		// 添加Listener
		this.addListeners();
	}

	private void initialize() {
		GridBagLayout layout = new GridBagLayout();
		// JFrame
		this.setSize(900, 500);
		this.setTitle("Nio长连接客户端 - V1.0 By myumen Build.20170615");
		this.setLayout(layout);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Controls
		this.profileLabel = new JLabel("Profile:");
		addComponent(this, profileLabel, 0, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
		this.profileCombo = new JComboBox<String>();
		profileCombo.setSize(15, profileCombo.getHeight());
		addComponent(this, profileCombo, 1, 0, 2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);

		this.ipLabel = new JLabel("IP:");
		addComponent(this, ipLabel, 0, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
		this.ipField = new JTextField("", 12);
		addComponent(this, ipField, 1, 1, 2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);

		this.portLabel = new JLabel("Port:");
		addComponent(this, portLabel, 0, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);

		this.portField = new JTextField("", 12);
		addComponent(this, portField, 1, 2, 2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);

		this.encodingLabel = new JLabel("消息编码");
		addComponent(this, encodingLabel, 0, 3, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
		this.encodingCombo = new JComboBox<String>(ENCODING_OPTIONS);
		this.encodingCombo.setEditable(true);
		this.encodingCombo.setToolTipText("也可以直接输入编码，不填写则默认为GBK编码");
		addComponent(this, encodingCombo, 1, 3, 2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);

		
		this.msgCategoryLabel = new JLabel("明文/密文");
		addComponent(this, msgCategoryLabel, 0, 4, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
		this.msgCategoryCombo = new JComboBox<String>(MSG_CATEGORY);
		this.msgCategoryCombo.setEditable(false);
		// this.msgCategoryCombo.setToolTipText("也可以直接输入编码，不填写则默认为GBK编码");
		addComponent(this, msgCategoryCombo, 1, 4, 2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
		
		TitledBorder reqBorder = new TitledBorder(BorderFactory.createLineBorder(TB_COLOR), "请求消息");
		reqBorder.setTitleColor(Color.blue);
		this.requestMessage = new JTextArea(10, 50);
		this.requestMessage.setLineWrap(true); // 自动换行功能
		this.requestMessage.setWrapStyleWord(true);
		JScrollPane reqjs = new JScrollPane(requestMessage, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		reqjs.setBorder(reqBorder);
		addComponent(this, reqjs, 3, 0, 5, 4, GridBagConstraints.CENTER, GridBagConstraints.BOTH);

		this.lengthLabel = new JLabel("添加长度到消息头部");
		addComponent(this, lengthLabel, 0, 5, 2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
		this.lengthCombo = new JComboBox<String>(LENGTH_OPTIONS);
		this.lengthCombo.setEditable(true);
		this.lengthCombo.setSelectedIndex(0); // 默认选中
		this.lengthCombo.setToolTipText("也可以直接输入消息头部长度，默认为12字节");
		addComponent(this, lengthCombo, 2, 5, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);

		this.connectOrDisconnect = new JButton("连接");
		addComponent(this, connectOrDisconnect, 0, 6, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
		this.send = new JButton("发送消息");
		addComponent(this, send, 1, 6, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
		this.saveProfileButton = new JButton("保存Profile");
		addComponent(this, saveProfileButton, 2, 6, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);

		this.lengthMsg = new JLabel("消息长度...");
		addComponent(this, lengthMsg, 0, 7, 3, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
		this.statusMsg = new JLabel("状态...");
		addComponent(this, statusMsg, 0, 8, 3, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);

		TitledBorder rspBorder = new TitledBorder(BorderFactory.createLineBorder(TB_COLOR), "响应消息");
		rspBorder.setTitleColor(Color.blue);
		this.responseMessage = new JTextArea(10, 50);
		this.responseMessage.setLineWrap(true); // 自动换行功能
		this.responseMessage.setWrapStyleWord(true);

		JScrollPane rspjs = new JScrollPane(responseMessage, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		rspjs.setBorder(rspBorder);
		addComponent(this, rspjs, 3, 4, 5, 5, GridBagConstraints.CENTER, GridBagConstraints.BOTH);

		// this.pack();
		this.setVisible(true);
	}

	private static void addComponent(Container container, JComponent component, int gridx, int gridy, int gridwidth,
			int gridheight, int anchor, int fill) {
		GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, 1.0, 1.0, anchor, fill,
				INSETS, 0, 0);
		container.add(component, gbc);
	}

	private void loadData() {
		// 先清除数据再重新加载
		this.profileCombo.removeAllItems();
		this.profiles.clear();

		BufferedReader br = null;
		try {
			File p = new File(PROFILE);
			if (!p.exists()) {
				p.createNewFile();
			}
			br = new BufferedReader(new FileReader(new File(PROFILE)));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (isBlank(line)) {
					continue;
				}
				this.profiles.add(line);
				this.profileCombo.addItem(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 保存数据到文件
	 */
	public void saveData() {
		PrintWriter pw = null;
		try {
			File p = new File(PROFILE);
			if (!p.exists()) {
				p.createNewFile();
			}
			pw = new PrintWriter(new FileWriter(new File(PROFILE)));
			for (String line : profiles) {
				pw.println(line);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}

	private void addListeners() {
		// 为了向匿名事件处理类中传递顶层容器JFrame，也可以直接用JFrame.this
		final JFrame container = this;

		final UIUpdater updater = new UIUpdater();
		// 注册需要在业务代码中更新的UI组件
		updater.register("statusMsg", statusMsg);
		updater.register("responseMessage", responseMessage);
		updater.register("lengthMsg", lengthMsg);
		updater.register("connectOrDisconnect", connectOrDisconnect);

		starter = new ClientStarter(updater);
		
		this.send.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// actionPerformed本身就是在EventDispatchThread(EDT)中执行，所以不需要在使用
				// SwingUtilities.invokeLater这种方式更新界面
				lengthMsg.setText("消息长度...");
				statusMsg.setText("状态...");

				if (!connected) {
					JOptionPane.showMessageDialog(container, "未连接到服务器，请先连接", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				String request = requestMessage.getText();
				if (isBlank(request)) {
					JOptionPane.showMessageDialog(container, "请求消息不能为空", "Error", JOptionPane.ERROR_MESSAGE);
					// requestMessage.
					return;
				}

				String encoding = encodingCombo.getSelectedItem().toString();
				if (isBlank(encoding)) {
					encoding = "GBK";
				}
				try {
					request.getBytes(encoding);
				} catch (UnsupportedEncodingException e1) {
					JOptionPane.showMessageDialog(container, "不支持" + encoding + "编码", "Error",
							JOptionPane.ERROR_MESSAGE);
					// requestMessage.
					return;
				}

				String len = lengthCombo.getSelectedItem().toString();
				int headLen = 0;
				boolean errorFlag = false;
				if (isBlank(len)) {
					errorFlag = true;
				} else if (!Arrays.toString(LENGTH_OPTIONS).contains(len)) {
					try {
						headLen = Integer.parseInt(len);
					} catch (NumberFormatException ne) {
						errorFlag = true;
					}

					if (headLen < 0) {
						errorFlag = true;
					}
				}
				if (errorFlag) {
					JOptionPane.showMessageDialog(container, "消息头部长度请输入大于0的数字", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				// 启动工作线束，发送请求消息并接收响应消息；
				// 耗时的任务不要放在EDT线程中
				// new Thread(new SendAndReceiveMessageTask(request, updater, msgCat), "SendAndReceiveMessageThread").start();
				starter.sendMessage(request);
			}
		});

		// 下拉框选择事件
		this.profileCombo.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) { //
					String value = e.getItem().toString();

					if (isBlank(value)) {
						return;
					}
					String[] parts = value.split("/");
					if (parts == null || parts.length != 2) {
						JOptionPane.showMessageDialog(container, value + "格式错误", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					ipField.setText(parts[0]);
					portField.setText(parts[1]);
				}
			}
		});

		this.saveProfileButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String ip = ipField.getText();
				String port = portField.getText();
				if (isBlank(ip) || isBlank(port)) {
					JOptionPane.showMessageDialog(container, "IP或PORT不能为空", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					Integer.parseInt(port);
				} catch (NumberFormatException ne) {
					JOptionPane.showMessageDialog(container, "PORT请输入正确的数字", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				profiles.add(ip + "/" + port);
				saveData();
				loadData();

				statusMsg.setText("保存成功..");
			}
		});

		this.connectOrDisconnect.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (!connected) {
					connect(e);
					connected = true;
				} else {
					disconnect(e);
					connected = false;
				}
			}

			private void connect(ActionEvent e) {
				String ip = ipField.getText();
				String port = portField.getText();
				if (isBlank(ip) || isBlank(port)) {
					JOptionPane.showMessageDialog(container, "IP或PORT不能为空", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				int nport = -1;
				try {
					nport = Integer.parseInt(port);
				} catch (NumberFormatException ne) {
					JOptionPane.showMessageDialog(container, "PORT请输入正确的数字", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				starter.setIp(ip);
				starter.setPort(nport);
				starter.connect();
				// new Thread(new ConnectToServerTask(ip, nport, updater, true), "ConnectToServerThread").start();
			}

			private void disconnect(ActionEvent e) {
				starter.disConnect();
				// new Thread(new ConnectToServerTask("", 0, updater, false), "ConnectToServerThread").start();
			}
		});
	}

	private boolean isBlank(String s) {
		return s == null || s.length() == 0;
	}


}
