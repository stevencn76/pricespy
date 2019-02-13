package net.ojava.openkit.pricespy.gui.compare;

import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import net.ojava.openkit.pricespy.entity.Product;
import net.ojava.openkit.pricespy.entity.Store;
import net.ojava.openkit.util.NumberUtil;

public class CompareStoreTableModel extends AbstractTableModel implements IDataCacheListener {
	private static final long serialVersionUID = 1L;
	
	private static final String headers[] = new String[] {
		"店名", "产品数量", "纽币总价", "人民币总价", "总报价"
	};
	
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
		return DataCache.getInstance().getStores().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		List<Store> storeList = DataCache.getInstance().getStores();
		if (rowIndex >= 0 && rowIndex < storeList.size()) {
			Store store = storeList.get(rowIndex);
			StoreCart cart = DataCache.getInstance().getStoreCart(store);
			
			switch (columnIndex) {
			case 0:
				return store.getName();
			case 1:
				return "" + cart.getProducts().size();
			case 2:
				double totalNz = 0;
				for (Product tp : cart.getProducts()) {
					totalNz += tp.getNzPrice();
				}
				return "" + totalNz;
			case 3:
				double totalCn = 0;
				for (Product tp : cart.getProducts()) {
					totalCn += tp.getCnPrice();
				}
				return "" + totalCn;
			case 4:
				double total = 0;
				for (Product tp : cart.getProducts()) {
					total += tp.getNzPrice();
				}
				total = total * ParamCache.getInstance().getExchangeRates() * ParamCache.getInstance().getProfitRates();
				return NumberUtil.double2String(total, 2);
			}
		}
		return null;
	}
	
	public Store getStore(int index) {
		List<Store> storeList = DataCache.getInstance().getStores();
		if (index >= 0 && index < storeList.size()) {
			return storeList.get(index);
		}
		return null;
	}

	@Override
	public void cacheChanged() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				CompareStoreTableModel.this.fireTableDataChanged();
			}
		});
	}

}
