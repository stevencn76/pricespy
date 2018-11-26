package net.ojava.openkit.pricespy.gui.compare;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import net.ojava.openkit.pricespy.entity.Product;
import net.ojava.openkit.pricespy.entity.Store;
import net.ojava.openkit.pricespy.util.NumberUtil;

public class CompareProductTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private static String [] headers = new String[] {
		"产品名称", "纽币单价", "人民币单价", "人民币报价"	
	};
	
	private Store store;

	@Override
	public String getColumnName(int columnIndex) {
		return headers[columnIndex];
	}
	
	@Override
	public int getColumnCount() {
		return headers.length;
	}

	@Override
	public int getRowCount() {
		if (store == null) {
			return 0;
		}
		
		StoreCart cart = DataCache.getInstance().getStoreCart(store);
		if (cart == null)
			return 0;
		
		return cart.getProducts().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (store == null) {
			return 0;
		}
		
		StoreCart cart = DataCache.getInstance().getStoreCart(store);
		if (cart == null)
			return 0;
		
		List<Product> productList = cart.getProducts();
		if (rowIndex >= 0 && rowIndex < productList.size()) {
			Product p = productList.get(rowIndex);
			switch(columnIndex) {
			case 0:
				return p.getName();
			case 1:
				return "" + p.getNzPrice();
			case 2:
				return "" + p.getCnPrice();
			case 3:
				double price = p.getNzPrice() * ParamCache.getInstance().getExchangeRates() * ParamCache.getInstance().getProfitRates();
				return NumberUtil.double2String(price, 2);
			}
		}
		return null;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
		
		this.fireTableDataChanged();
	}

	public Product getProduct(int index) {
		if (store == null) {
			return null;
		}
		
		StoreCart cart = DataCache.getInstance().getStoreCart(store);
		if (cart == null)
			return null;
		
		List<Product> productList = cart.getProducts();
		if (index >= 0 && index < productList.size()) {
			return productList.get(index);
		}
		return null;
	}
}
