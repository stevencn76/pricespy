package net.ojava.openkit.pricespy.gui.compare;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.ojava.openkit.pricespy.entity.Product;
import net.ojava.openkit.pricespy.entity.Store;

public class StoreCart {
	private Store store;
	private Set<Product> products = new HashSet<>();
	private List<Product> productList = null;
	
	public StoreCart(Store store) {
		this.setStore(store);
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public synchronized List<Product> getProducts() {
		if (productList != null)
			return productList;
		
		productList = new ArrayList<>();
		for (Product tp : products) {
			if (productList.size() == 0) {
				productList.add(tp);
			} else {
				boolean added = false;
				for (int i=0; i<productList.size(); i++) {
					if (tp.getName().compareTo(productList.get(i).getName()) < 0) {
						productList.add(i, tp);
						added = true;
						break;
					}
				}
				if (!added) {
					productList.add(tp);
				}
			}
		}
		return productList;
	}
	
	public synchronized void addProduct(Product product) {
		if (productList != null) {
			productList.clear();
			productList = null;
		}
		
		if (products.contains(product)) {
			products.remove(product);
		}
		products.add(product);
	}
	
	public synchronized void removeProduct(Product product) {
		if (productList != null) {
			productList.clear();
			productList = null;
		}
		
		if (products.contains(product)) {
			products.remove(product);
		}
	}
}
