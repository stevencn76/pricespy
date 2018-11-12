package net.ojava.openkit.pricespy.gui.query;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

import org.apache.log4j.Logger;

import net.ojava.openkit.pricespy.app.Main;
import net.ojava.openkit.pricespy.business.Retriever;
import net.ojava.openkit.pricespy.entity.Product;
import net.ojava.openkit.pricespy.entity.StoreProp;
import net.ojava.openkit.pricespy.gui.ProductViewer;

public class QueryPricePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static Logger LOG = Logger.getLogger(QueryPricePanel.class);

	private JTable productTable;
	private ProductTableModel productTableModel = new ProductTableModel();
	
	private JTextField nameField = new JTextField(20);
	private JButton searchBtn = new JButton("搜索");
	
	private JButton updateBtn = new JButton("更新产品");
	
	private ProductViewer productViewer = new ProductViewer();
	
	private Map<Integer, Thread> updateThreadMap = new Hashtable<>();
	
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
		
		JPanel viewPanel = new JPanel();
		viewPanel.setLayout(new BorderLayout(5, 5));
		viewPanel.add(productViewer);
		
		JPanel viewBtnPanel = new JPanel();
		viewBtnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		viewBtnPanel.add(updateBtn);
		viewPanel.add(viewBtnPanel, BorderLayout.NORTH);
		cp.add(viewPanel, BorderLayout.EAST);
		
		this.add(cp);
		
		updateComponentStatus();
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
					productViewer.setProduct(product, false);
				} else {
					productViewer.setProduct(null, false);
				}
				
				updateComponentStatus();
			}
		});
		
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				doUpdateProduct();
			}
		});
	}
	
	private void updateComponentStatus() {
		int index = productTable.getSelectedRow();
		if (index >= 0 && index < productTableModel.getRowCount()) {
			updateBtn.setEnabled(true);
		} else {
			updateBtn.setEnabled(false);
		}
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
			
			updateComponentStatus();
		}
	}
	
	private void doUpdateProduct() {
		int index = productTable.getSelectedRow();
		final Product product = productTableModel.getProduct(index);
		if (product == null)
			return;
		
		synchronized(updateThreadMap) {
			Thread t = updateThreadMap.get(product.getId());
			if (t != null) {
				JOptionPane.showMessageDialog(Main.mainFrame, "产品已经开始更新", "提示信息", JOptionPane.WARNING_MESSAGE);
				return;
			} else {
				t = new Thread() {
					public void run() {
						try {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									productViewer.setText("正在重新下载数据......");
									productViewer.setIcon(null);
								}
							});

							Map<String, StoreProp> storePropMap = new HashMap<>();
							for (StoreProp sp : Main.getService().findStoreProps(product.getStore())) {
								storePropMap.put(sp.getName(), sp);
							}
							Retriever retriever = new Retriever(product.getStore(), storePropMap);
							Product newPdt = retriever.syncOne(product.getNumber(), true);
							
							if (newPdt != null) {
								product.setName(newPdt.getName());
								product.setPicUrl(newPdt.getPicUrl());
								product.setNzPrice(newPdt.getNzPrice());
								product.setCnPrice(newPdt.getCnPrice());
								
								Main.getService().saveProduct(product);
								
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										int curIndex = productTable.getSelectedRow();
										if (curIndex == index) {
											productTableModel.updateProduct(index, product);
											productViewer.setProduct(product, true);
										}
									}
								});
							}
						} catch (Exception e) {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									productViewer.setProduct(product, false);
									JOptionPane.showMessageDialog(Main.mainFrame, "更新产品出错: " + e.getMessage(), "错误提示", JOptionPane.ERROR_MESSAGE);
								}
							});
							LOG.debug("update product failed", e);
						}
						
						synchronized(updateThreadMap) {
							updateThreadMap.remove(product.getId());
						}
					}
				};
				
				updateThreadMap.put(product.getId(), t);
				t.start();
			}
		}
	}
}
