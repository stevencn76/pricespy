package net.ojava.openkit.pricespy.business;

import java.util.HashMap;
import java.util.Map;

import net.ojava.openkit.pricespy.app.Main;
import net.ojava.openkit.pricespy.entity.Store;
import net.ojava.openkit.pricespy.entity.StoreProp;

public class RetrieverManager {
	private static RetrieverManager instance;
	
	private Map<String, Retriever> retrieverCache = new HashMap<>();
	
	private RetrieverManager() {}
	
	public static RetrieverManager getInstance() {
		if (instance == null) {
			instance = new RetrieverManager();
		}
		
		return instance;
	}
	
	public Retriever getRetriever(String storeName) {
		synchronized(retrieverCache) {
			return retrieverCache.get(storeName);
		}
	}
	
	public void stopRetriever(String storeName) {
		synchronized(retrieverCache) {
			Retriever retriever = retrieverCache.get(storeName);
			if (retriever != null) {
				retriever.stopSync();
			}
		}
	}
	
	public void startRetriever(Store store, RetrieverListener listener) throws Exception {
		Retriever retriever = null;
		synchronized(retrieverCache) {
			retriever = retrieverCache.get(store.getName());
			if (retriever != null) {
				if (retriever.isSync()) {
					throw new Exception("正在同步中");
				} else {
					retriever.removeListener(listener);
				}
			}
			
			Map<String, StoreProp> storePropMap = new HashMap<>();
			for (StoreProp sp : Main.getService().findStoreProps(store)) {
				storePropMap.put(sp.getName(), sp);
			}
			retriever = new Retriever(store, storePropMap);
			retriever.addListener(listener);
			retrieverCache.put(store.getName(), retriever);
		}
		
		retriever.syncAll();
	}
}
