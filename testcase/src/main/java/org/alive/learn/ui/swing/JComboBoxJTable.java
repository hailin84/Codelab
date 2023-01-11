package org.alive.learn.ui.swing;

import java.awt.Dimension;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

public class JComboBoxJTable {

	public JComboBoxJTable() {
		JFrame aFrame = new JFrame();
		ExtendJComboBox JComboBox1 = new ExtendJComboBox();
		JComboBox1.setPreferredSize(new Dimension(20, 20));
		JComboBox1.setMaximumSize(new Dimension(30, 20));
		String[] aArr = {
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
				"BBAAAAAAAAAAA", "CCCCC", "DDDDDDDDDDDDD", "EEEEEEEEEEEE",
				"FFFFFFFFFFFFFFFFFFFFFFFFFFFF",
				"GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "BBAAAAAAAAAAA",
				"CCCCC", "DDDDDDDDDDDDD", "EEEEEEEEEEEE",
				"FFFFFFFFFFFFFFFFFFFFFFFFFFFF",
				"GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG" };
		for (int i = 0; i < aArr.length; ++i) {
			JComboBox1.addItem(aArr[i]);
		}
		aFrame.getContentPane().add(JComboBox1);
		aFrame.getContentPane().setSize(100, 100);
		aFrame.pack();
		aFrame.setVisible(true);
	}

	@SuppressWarnings("serial")
	class ExtendJComboBox extends JComboBox {
		public ExtendJComboBox() {
			super();
			setUI(new ExtendComboUI());
		} // end of default constructor
	}

	@SuppressWarnings("serial")
	class ExtendComboUI extends BasicComboBoxUI {
		protected ComboPopup createPopup() {
			BasicComboPopup popup = new BasicComboPopup(comboBox) {

				protected JScrollPane createScroller() {
					return new JScrollPane(list,
							ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
							ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				} // end of method createScroller
			};
			return popup;
		} // end of method createPopup
	} // end of inner class myComboUI
}
