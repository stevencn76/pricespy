package net.ojava.openkit.pricespy.gui.sync;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

import org.apache.log4j.Logger;

import net.ojava.openkit.pricespy.app.Main;
import net.ojava.openkit.pricespy.business.Retriever;
import net.ojava.openkit.pricespy.business.RetrieverListener;
import net.ojava.openkit.pricespy.business.RetrieverManager;
import net.ojava.openkit.pricespy.entity.Store;
import net.ojava.openkit.pricespy.util.TimeUtil;

public class StorePanel extends JPanel implements RetrieverListener {
	private static final long serialVersionUID = 1L;
	private static Logger LOG = Logger.getLogger(StorePanel.class);
	private JTable storeTable;
	private StoreTableModel storeTableModel = new StoreTableModel();
	
	private JButton syncBtn = new JButton("完全同步");
	private JButton stopBtn = new JButton("停止同步");
	private JButton refreshBtn = new JButton("刷新列表");

	public StorePanel() {
		initComponents();
		initEvents();
		initData();
	}
	
	private void initComponents() {
		this.setPreferredSize(new Dimension(450, 150));
		this.setLayout(new BorderLayout(5, 5));
		
		storeTable = new JTable(storeTableModel);
		TableColumnModel tcm = storeTable.getColumnModel();
		int[] widths = StoreTableModel.getWidths();
		for (int i = 0; i < tcm.getColumnCount(); i++) {
			tcm.getColumn(i).setPreferredWidth(widths[i]);
		}
		this.add(new JScrollPane(storeTable));
		
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		btnPanel.add(syncBtn);
		btnPanel.add(stopBtn);
		btnPanel.add(refreshBtn);
		this.add(btnPanel, BorderLayout.NORTH);
		
		updateComponentsStatus();
	}
	
	private void initEvents() {
		storeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				updateComponentsStatus();
			}
		});
		syncBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int index = storeTable.getSelectedRow();
				if (index >= 0  && index < storeTableModel.getRowCount()) {
					Store store = storeTableModel.getStore(index);
					
					try {
						RetrieverManager.getInstance().startRetriever(store, StorePanel.this);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(Main.mainFrame, 
								"启动同步失败: " + e.getMessage(), 
								"错误提示", 
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		stopBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int index = storeTable.getSelectedRow();
				if (index >= 0  && index < storeTableModel.getRowCount()) {
					Store store = storeTableModel.getStore(index);
					
					try {
						RetrieverManager.getInstance().stopRetriever(store.getName());
					} catch (Exception e) {
						JOptionPane.showMessageDialog(Main.mainFrame, 
								"启动同步失败: " + e.getMessage(), 
								"错误提示", 
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
	}
	
	private void initData() {
		storeTableModel.loadData();
	}
	
	private void updateComponentsStatus() {
		int index = storeTable.getSelectedRow();
		if (index >= 0 && index < storeTable.getRowCount()) {
			Store store = storeTableModel.getStore(index);
			syncBtn.setEnabled(true);
			Retriever retriever = RetrieverManager.getInstance().getRetriever(store.getName());
			stopBtn.setEnabled(retriever != null && retriever.isSync());
		} else {
			syncBtn.setEnabled(false);
			stopBtn.setEnabled(false);
		}
	}
	
	private void updateModel(Store store) {
		int index = storeTable.getSelectedRow();
		int storeIndex = storeTableModel.getIndex(store);
		if (storeIndex >= 0 && storeIndex < storeTableModel.getRowCount()) {
			storeTableModel.fireTableRowsUpdated(index, index);
		}
		if (index >= 0 && index < storeTableModel.getRowCount()) {
			storeTable.getSelectionModel().setSelectionInterval(index, index);
		}
	}

	@Override
	public void syncStarted(Store store, String statusMsg) {
		updateModel(store);
		
		updateComponentsStatus();
	}

	@Override
	public void syncStep(Store store, int productSyncCount, String statusMsg) {
		updateModel(store);
	}

	@Override
	public void syncStopped(Store store, String statusMsg) {
		try {
			store.setProductCount(Main.getService().getProductCount(store));
			store.setLastSyncTime(TimeUtil.getCurTime());
			Main.getService().saveStore(store);
		} catch(Exception e) {
			LOG.debug("update store failed", e);
		}
		
		updateModel(store);
		updateComponentsStatus();
	}
	
}
