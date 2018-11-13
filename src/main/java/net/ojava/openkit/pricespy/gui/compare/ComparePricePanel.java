package net.ojava.openkit.pricespy.gui.compare;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;

public class ComparePricePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTable storeTable;
	private JTable productTable;
	
	private CompareStoreTableModel storeTableModel = new CompareStoreTableModel();
	
	public ComparePricePanel() {
		initComponents();
		initEvents();
	}
	
	private void initComponents() {
		storeTable = new JTable(storeTableModel);
		DataCache.getInstance().addListener(storeTableModel);
		
		this.setLayout(new BorderLayout());
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setLeftComponent(new JScrollPane(storeTable));
		splitPane.setRightComponent(new JPanel());
		
		splitPane.setDividerLocation(400);
		
		this.add(splitPane);
	}
	
	private void initEvents() {
		
	}
}
