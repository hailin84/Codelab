package org.alive.tools.transencoding;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import org.alive.tools.socketmessage.UIUpdater;
import org.apache.commons.lang3.StringUtils;

/**
 * 编码转换工具UI
 * 
 * @author hailin84
 * @since 2017.05.11
 *
 */
public class TransEncodingUI extends JFrame {

	private static final long serialVersionUID = 9209873813255495069L;

	private static final Insets INSETS = new Insets(1, 1, 1, 1);
	// private static final Color TB_COLOR = UIUtil.parseColor("#3e62a6");

	private JMenuBar menuBar;

	private JLabel sourceLabel;

	private JTextField source;

	private JButton choose;

	private JLabel encodingLabel;

	private JComboBox<String> encodingCombo;

	private JButton transButton;
	
	private JLabel typeLabel;
	
	private JTextField type;

	//private JTable table;
	
	private JLabel status;

	private Map<String, JMenuItem> miMap = new HashMap<String, JMenuItem>();

	public TransEncodingUI() {
		// 初始化界面
		this.initialize();

		// 加载数据
		this.loadData();

		// 添加Listener
		this.addListeners();
	}

	private void initialize() {
		// 初始化菜单
		this.menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem open = new JMenuItem("Open");
		open.setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_MASK));
		fileMenu.add(open);
		miMap.put("open", open);

		JMenu helpMenu = new JMenu("Help");
		JMenuItem about = new JMenuItem("About");
		helpMenu.add(about);

		miMap.put("about", about);
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		this.setJMenuBar(menuBar);

		// 界面布局
		GridBagLayout layout = new GridBagLayout();
		// JFrame
		this.setSize(700, 200);
		this.setTitle("Trans Encoding Tool - V1.0 By myumen Build.20170511");
		this.setLayout(layout);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.sourceLabel = new JLabel("源文件目录：");
		addComponent(this, sourceLabel, 0, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);

		this.source = new JTextField("", 40);
		addComponent(this, source, 1, 0, 2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);

		this.choose = new JButton("....");
		this.choose.setToolTipText("点击选择源文件目录...");
		addComponent(this, choose, 3, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);

		this.typeLabel = new JLabel("文件扩展名：");
		addComponent(this, typeLabel, 0, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
		
		this.type = new JTextField("java", 40);
		this.type.setToolTipText("输入文件扩展名，不带.号，用空格分隔");
		addComponent(this, type, 1, 1, 3, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
		
		this.encodingLabel = new JLabel("目标编码：");
		addComponent(this, encodingLabel, 0, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);

		// Object[] encodings = Charset.availableCharsets().keySet().toArray();
		this.encodingCombo = new JComboBox<String>(new String[] { "GB18030", "UTF-8" });
		this.encodingCombo.setEditable(true);
		this.encodingCombo.setToolTipText("也可以直接输入编码");
		addComponent(this, encodingCombo, 1, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);

		this.transButton = new JButton("转换");
		addComponent(this, transButton, 2, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);

		this.status = new JLabel();
		addComponent(this, status, 3, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
		
//		TitledBorder reqBorder = new TitledBorder(BorderFactory.createLineBorder(TB_COLOR), "文件转换列表");
//		reqBorder.setTitleColor(Color.blue);
//		
//		this.table = new JTable(new FileTableModel());
//		// this.table.setModel(new FileTableModel());
//		// this.requestMessage.setLineWrap(true); // 自动换行功能
//		// this.requestMessage.setWrapStyleWord(true);
//		JScrollPane reqjs = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
//				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//		reqjs.setBorder(reqBorder);
//
//		addComponent(this, reqjs, 0, 2, 6, 5, GridBagConstraints.WEST, GridBagConstraints.NONE);
		//this.pack();
		this.setVisible(true);
	}

	private static void addComponent(Container container, JComponent component, int gridx, int gridy, int gridwidth,
			int gridheight, int anchor, int fill) {
		GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, 1.0, 1.0, anchor, fill,
				INSETS, 0, 0);
		container.add(component, gbc);
	}

	private void loadData() {

	}

	private void addListeners() {
		final JFrame container = this;
		ActionListener fileChooseListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				fc.setDialogTitle("选择源文件或者目录");
				int result = fc.showOpenDialog(container); // 打开"打开文件"对话框
				// int result = dlg.showSaveDialog(this); // 打"开保存文件"对话框
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					source.setText(file.getAbsolutePath());
				}
			}
		};

		this.choose.addActionListener(fileChooseListener);
		miMap.get("open").addActionListener(fileChooseListener);

		miMap.get("about").addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(container, "文件编码转换工具\nmyumen\n20170511", "关于",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		this.transButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String fileType = type.getText();
				if (StringUtils.isEmpty(fileType)) {
					JOptionPane.showMessageDialog(container, "请输入需要进行编码转换的文件扩展名",
							"Error", JOptionPane.ERROR_MESSAGE);
					// requestMessage.
					return;
				}
				
				String encoding = encodingCombo.getSelectedItem().toString();
				if (StringUtils.isEmpty(encoding)) {
					JOptionPane.showMessageDialog(container, "请输入目标编码",
							"Error", JOptionPane.ERROR_MESSAGE);
					// requestMessage.
					return;
				}
				try {
					"ABC中国".getBytes(encoding);
				} catch (UnsupportedEncodingException e1) {
					JOptionPane.showMessageDialog(container, "系统不支持" + encoding + "编码",
							"Error", JOptionPane.ERROR_MESSAGE);
					// requestMessage.
					return;
				}
				
				String path = source.getText();
				if (StringUtils.isEmpty(path)) {
					JOptionPane.showMessageDialog(container, "请选择或输入源文件名/目录",
							"Error", JOptionPane.ERROR_MESSAGE);
					// requestMessage.
					return;
				}
				
				File f = new File(path);
				if (!f.exists()) {
					JOptionPane.showMessageDialog(container, "目录/文件不存在",
							"Error", JOptionPane.ERROR_MESSAGE);
					// requestMessage.
					return;
				}
				status.setText("start..");
				
				UIUpdater updater = new UIUpdater();
				// 注册需要在业务代码中更新的UI组件
				updater.register("status", status);
				
				new Thread(new EncodingTransServiceTask(fileType, encoding, path, updater)).start();
			}
		});
	}
}
