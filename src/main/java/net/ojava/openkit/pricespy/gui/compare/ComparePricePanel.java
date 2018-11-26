package net.ojava.openkit.pricespy.gui.compare;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.ojava.openkit.pricespy.entity.Product;
import net.ojava.openkit.pricespy.entity.Store;

public class ComparePricePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTable storeTable;
	private JTable productTable;
	private JButton removeBtn = new JButton("移除产品");
	
	private CompareStoreTableModel storeTableModel = new CompareStoreTableModel();
	private CompareProductTableModel productTableModel = new CompareProductTableModel();
	
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
		
		productTable = new JTable(productTableModel);
		JPanel pp = new JPanel();
		pp.setLayout(new BorderLayout(5, 5));
		pp.add(new JScrollPane(productTable));
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		btnPanel.add(removeBtn);
		pp.add(btnPanel, BorderLayout.SOUTH);
		splitPane.setRightComponent(pp);
		
		splitPane.setDividerLocation(400);
		
		this.add(splitPane);
		
		updateComponentStatus();
	}
	
	private void initEvents() {
		storeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				doShowProductList();
			}
		});
		productTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				updateComponentStatus();
			}
		});
		
		removeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				doRemoveProduct();
			}
		});
	}
	
	private void updateComponentStatus() {
		int productIndex = productTable.getSelectedRow();
		if (productIndex >= 0 && productIndex < productTableModel.getRowCount()) {
			removeBtn.setEnabled(true);
		} else {
			removeBtn.setEnabled(false);
		}
	}
	
	private void doShowProductList() {
		int storeIndex = storeTable.getSelectedRow();
		Store store = storeTableModel.getStore(storeIndex);

		productTableModel.setStore(store);
	}
	
	private void doRemoveProduct() {
		int productIndex = productTable.getSelectedRow();
		
		Product product = productTableModel.getProduct(productIndex);
		if (product != null) {
			DataCache.getInstance().removeProduct(product);
		}
	}
}
