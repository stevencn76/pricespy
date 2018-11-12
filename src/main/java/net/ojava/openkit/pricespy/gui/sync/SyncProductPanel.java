package net.ojava.openkit.pricespy.gui.sync;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class SyncProductPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private StorePanel storePanel;
	
	public SyncProductPanel() {
		initComponents();
		initEvents();
	}
	
	private void initComponents() {
		storePanel = new StorePanel();
		
		this.setLayout(new BorderLayout(5, 5));
		this.add(storePanel);
	}
	
	private void initEvents() {
		
	}
}
