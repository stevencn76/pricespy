package net.ojava.openkit.pricespy.gui.query;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import net.ojava.openkit.pricespy.entity.Product;
import net.ojava.openkit.pricespy.entity.Store;

public class ProductTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private static final String headers[] = new String[] { 
		"店名", "名称", "纽币价格", "人民币价格"
	};
	
	private static final int widths[] = {
		100, 500, 80, 80
	};
	
	public static final int[] getWidths() {
		return widths;
	}
	
	private List<Object> itemList = new ArrayList<>();

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
		return itemList.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object obj = itemList.get(rowIndex);
		
		if (obj instanceof Store) {
			switch(columnIndex) {
			case 0:
				return ((Store)obj).getName();
			}
		} else if (obj instanceof Product) {
			Product product = (Product)obj;
			switch(columnIndex) {
			case 0:
				return "";
			case 1:
				return product.getName();
			case 2:
				return "$"+product.getNzPrice();
			case 3:
				return "¥"+product.getCnPrice();
			}
		}
		
		return "";
	}

	public void updateProducts(List<Product> products) {
		itemList.clear();
		
		if (products == null)
			return;
		
		List<Store> storeList = new ArrayList<>();
		Map<Store, List<Product>> productMap = new Hashtable<>();
		
		for (Product p : products) {
			Store s = p.getStore();
			
			List<Product> pList = productMap.get(s);
			if (pList == null) {
				pList = new ArrayList<Product>();
				storeList.add(s);
				productMap.put(s, pList);
			}
			pList.add(p);
		}
		
		for (Store s : storeList) {
			itemList.add(s);
			List<Product> pList = productMap.get(s);
			if (pList != null) {
				for (Product p : pList) {
					itemList.add(p);
				}
			}
		}
		this.fireTableDataChanged();
	}
	
	public Product getProduct(int index) {
		if (index >= 0 && index < getRowCount()) {
			Object obj = itemList.get(index);
			if (obj instanceof Product)
				return (Product)obj;
		}
		
		return null;
	}
	
	public void updateProduct(int index, Product product) {
		if (index >= 0 && index < getRowCount()) {
			itemList.set(index, product);
			
			this.fireTableRowsUpdated(index, index);
		}
	}
}
