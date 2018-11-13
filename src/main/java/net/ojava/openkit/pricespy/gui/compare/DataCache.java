package net.ojava.openkit.pricespy.gui.compare;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.ojava.openkit.pricespy.entity.Product;
import net.ojava.openkit.pricespy.entity.Store;

public class DataCache {
	private static DataCache instance;
	
	private Map<Store, StoreCart> cartMap = new Hashtable<>();
	
	private List<Store> storeList = null;
	
	private Set<IDataCacheListener> listeners = new HashSet<>();
	
	private DataCache() {
		
	}
	
	public static DataCache getInstance() {
		if (instance == null) {
			instance = new DataCache();
		}
		
		return instance;
	}
	
	public void addListener(IDataCacheListener listener) {
		this.listeners.add(listener);
	}
	
	public synchronized void addProduct(Product product) {
		if (product.getStore() == null)
			return;
		
		synchronized(cartMap) {
			StoreCart cart = cartMap.get(product.getStore());
			if (cart == null) {
				cart = new StoreCart(product.getStore());
				cartMap.put(product.getStore(), cart);
				
				storeList = null;
			}
			
			cart.addProduct(product);
		}
		
		synchronized(listeners) {
			for (IDataCacheListener tl : listeners) {
				tl.cacheChanged();
			}
		}
	}
	
	public synchronized void removeProduct(Product product) {
		if (product.getStore() == null)
			return;
		
		synchronized(cartMap) {
			StoreCart cart = cartMap.get(product.getStore());
			
			if (cart != null) {
				cart.removeProduct(product);
			}
		}
		
		synchronized(listeners) {
			for (IDataCacheListener tl : listeners) {
				tl.cacheChanged();
			}
		}
	}
	
	public synchronized List<Store> getStores() {
		if (storeList != null) 
			return storeList;
		
		storeList = new ArrayList<>();
		
		for (Store ts : cartMap.keySet()) {
			if (storeList.size() == 0) {
				storeList.add(ts);
			} else {
				boolean added = false;
				for (int i=0; i<storeList.size(); i++) {
					if (ts.getName().compareTo(storeList.get(i).getName()) < 0) {
						storeList.add(i, ts);
						added = true;
						break;
					}
				}
				if (!added) {
					storeList.add(ts);
				}
			}
		}
		
		return storeList;
	}
	
	public StoreCart getStoreCart(Store store) {
		return cartMap.get(store);
	}
}
