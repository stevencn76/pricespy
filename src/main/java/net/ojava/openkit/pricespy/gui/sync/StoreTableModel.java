package net.ojava.openkit.pricespy.gui.sync;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import net.ojava.openkit.pricespy.app.Main;
import net.ojava.openkit.pricespy.business.Retriever;
import net.ojava.openkit.pricespy.business.RetrieverManager;
import net.ojava.openkit.pricespy.entity.Store;
import net.ojava.openkit.util.TimeUtil;

public class StoreTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private static Logger LOG = Logger.getLogger(StoreTableModel.class);
	
	private static String [] headers = new String[] {
			"店铺名称", "产品数量", "上次同步时间", "同步状态"
	};
	
	private static final int widths[] = {
		100, 100, 200, 500
	};
	
	public static final int[] getWidths() {
		return widths;
	}
	
	private List<Store> storeList = new ArrayList<>();

	@Override
	public String getColumnName(int column) {
		return headers[column];
	}

	@Override
	public int getColumnCount() {
		return headers.length;
	}

	@Override
	public int getRowCount() {
		return storeList.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Store store = storeList.get(rowIndex);
		
		switch(columnIndex) {
		case 0:
			return store.getName();
		case 1:
			return String.valueOf(store.getProductCount());
		case 2:
			return TimeUtil.toStringTimestamp(store.getLastSyncTime());
		case 3:
			Retriever retriever = RetrieverManager.getInstance().getRetriever(store.getName());
			if (retriever == null) {
				return "未启动";
			}
			else {
				return retriever.getStatusMsg();
			}
		}
		return null;
	}
	
	public void loadData() {
		storeList.clear();
		
		try {
			storeList.addAll(Main.getService().findAllStores());
			
			for (Store store : storeList) {
				store.setProductCount((int)Main.getService().getProductCount(store));
			}
			
			this.fireTableDataChanged();
		} catch (Exception e) {
			LOG.debug("load store data failed", e);
		}
	}

	public Store getStore(int index) {
		return storeList.get(index);
	}
	
	public int getIndex(Store store) {
		return storeList.indexOf(store);
	}
}
