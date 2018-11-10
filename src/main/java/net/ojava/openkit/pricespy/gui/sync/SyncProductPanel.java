package net.ojava.openkit.pricespy.gui.sync;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class SyncProductPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JSplitPane splitPane1;
	private JSplitPane splitPane2;
	private StorePanel storePanel;
	
	public SyncProductPanel() {
		initComponents();
		initEvents();
	}
	
	private void initComponents() {
		splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		storePanel = new StorePanel();
		
		splitPane1.setLeftComponent(storePanel);
		splitPane1.setRightComponent(splitPane2);
		
		splitPane2.setLeftComponent(new JPanel());
		splitPane2.setRightComponent(new JPanel());
		
		this.setLayout(new BorderLayout(5, 5));
		this.add(splitPane1, BorderLayout.CENTER);
	}
	
	private void initEvents() {
		
	}
}
