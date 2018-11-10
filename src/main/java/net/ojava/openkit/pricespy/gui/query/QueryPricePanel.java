package net.ojava.openkit.pricespy.gui.query;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

import org.apache.log4j.Logger;

import net.ojava.openkit.pricespy.app.Main;
import net.ojava.openkit.pricespy.entity.Product;
import net.ojava.openkit.pricespy.gui.ProductViewer;

public class QueryPricePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static Logger LOG = Logger.getLogger(QueryPricePanel.class);

	private JTable productTable;
	private ProductTableModel productTableModel = new ProductTableModel();
	
	private JTextField nameField = new JTextField(20);
	private JButton searchBtn = new JButton("搜索");
	
	private ProductViewer productViewer = new ProductViewer();
	
	public QueryPricePanel() {
		initComponents();
		initEvents();
	}
	
	private void initComponents() {
		JPanel searchPanel = new JPanel();
		searchPanel.setBorder(new TitledBorder(new EmptyBorder(5, 5, 5, 5), "搜索条件"));
		searchPanel.setLayout(new BorderLayout());
		
		JPanel fieldPanel = new JPanel();
		fieldPanel.setBorder(new EmptyBorder(0, 5, 0, 5));
		fieldPanel.setLayout(new BorderLayout());
		fieldPanel.add(new JLabel("名称"), BorderLayout.WEST);
		fieldPanel.add(nameField);
		
		searchPanel.add(fieldPanel);
		searchPanel.add(searchBtn, BorderLayout.EAST);
		searchPanel.add(new JLabel("搜索内容例子: 康维他 蜂蜜"), BorderLayout.NORTH);
		
		this.setBorder(new EmptyBorder(5, 5, 7, 5));
		this.setLayout(new BorderLayout());
		
		this.add(searchPanel, BorderLayout.NORTH);
		
		productTable = new JTable(productTableModel);
		TableColumnModel tcm = productTable.getColumnModel();
		int[] widths = ProductTableModel.getWidths();
		for (int i = 0; i < tcm.getColumnCount(); i++) {
			tcm.getColumn(i).setPreferredWidth(widths[i]);
		}
		
		JPanel cp = new JPanel();
		cp.setBorder(new EtchedBorder());
		cp.setLayout(new BorderLayout(5, 5));
		cp.add(new JScrollPane(productTable));
		cp.add(productViewer, BorderLayout.EAST);
		
		this.add(cp);
	}
	
	private void initEvents() {
		searchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				doSearch();
			}
		});
		nameField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				doSearch();
			}
		});
		productTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int index = productTable.getSelectedRow();
				if (index >= 0 && index < productTableModel.getRowCount()) {
					Product product = productTableModel.getProduct(index);
					productViewer.setProduct(product);
				} else {
					productViewer.setProduct(null);
				}
			}
		});
	}
	
	private void doSearch() {
		String namePattern = nameField.getText();
		if (namePattern != null) {
			namePattern = namePattern.trim();
		}
		
		if (namePattern == null || namePattern.length() == 0) {
			JOptionPane.showMessageDialog(Main.mainFrame, 
					"名称内容为空", "错误提示", JOptionPane.ERROR_MESSAGE);
			return;
		} else {
			String[] ps = namePattern.split(" |,|，|%");
			List<String> patterns = new ArrayList<>();

			for (String ts : ps) {
				if (ts != null && ts.length() > 0) {
					patterns.add(ts);
				}
			}
			
			try {
				productTableModel.updateProducts(Main.getService().findProducts(patterns.toArray(new String[] {})));
			} catch (Exception e) {
				JOptionPane.showMessageDialog(Main.mainFrame, 
						"搜索产品出错:" + e.getMessage(), 
						"错误提示", JOptionPane.ERROR_MESSAGE);
				LOG.debug("search product failed", e);
			}
		}
	}
}
