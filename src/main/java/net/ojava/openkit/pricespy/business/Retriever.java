package net.ojava.openkit.pricespy.business;

import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import net.ojava.openkit.pricespy.app.Main;
import net.ojava.openkit.pricespy.business.converter.ConverterManager;
import net.ojava.openkit.pricespy.entity.Product;
import net.ojava.openkit.pricespy.entity.Store;
import net.ojava.openkit.pricespy.entity.StoreProp;
import net.ojava.openkit.pricespy.util.HttpClientKeepSession;
import net.ojava.openkit.pricespy.util.StrUtil;

public class Retriever implements Runnable {
	private static Logger LOG = Logger.getLogger(Retriever.class);
	private Store store;
	private Map<String, StoreProp> storePropMap;
	private int productNumberLength = 0;
	
	private Object thread_lock = new Object();
	private Thread thread = null;
	private boolean stopped = true;
	private HttpClientKeepSession httpClient = new HttpClientKeepSession();
	
	private Set<RetrieverListener> listeners = new HashSet<>();
	
	private String statusMsg = "未启动";
	
	public Retriever(Store store, Map<String, StoreProp> storePropMap) {
		this.store = store;
		this.storePropMap = storePropMap;
		this.productNumberLength = 0;

		StoreProp pidLenProp = storePropMap.get(StoreProp.PROP_PID_LENGTH);
		if (pidLenProp != null) {
			try {
				this.productNumberLength = Integer.parseInt(pidLenProp.getValue());
			} catch (Exception e) {}
		}
	}
	
	public void addListener(RetrieverListener listener) {
		this.listeners.add(listener);
	}
	
	public void removeListener(RetrieverListener listener) {
		this.listeners.remove(listener);
	}
	
	public boolean isSync() {
		synchronized(thread_lock) {
			return thread != null && thread.isAlive();
		}
	}

	public void syncAll() throws Exception {
		if (isSync())
			throw new Exception("同步正在进行中");
		
		synchronized(thread_lock) {
			stopped = false;
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public void stopSync() {
		stopped = true;
	}
	
	public Product syncOne(String productNumber, boolean firstCall) throws Exception {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Fetching product id:").append(productNumber).append(" from ").append(store.getName());
		LOG.info(sb.toString());
		
		if (firstCall && store.isRequireLogin()) {
			StoreProp loginUrl = storePropMap.get(StoreProp.PROP_LOGIN_URL);
			if (loginUrl == null) {
				throw new Exception("login url does not exist");
			}
			
			StoreProp loginForm = storePropMap.get(StoreProp.PROP_LOGIN_FORM);
			if (loginForm == null) {
				throw new Exception("login form does not exist");
			}
			
			httpClient.get(store.getWebsite());
			
			httpClient.post(loginUrl.getValue(), loginForm.getValue());
			
			sb.setLength(0);
			sb.append("Done login for ").append(store.getName());
			LOG.info(sb.toString());
		}
		
		StoreProp productUrl = storePropMap.get(StoreProp.PROP_PRODUCT_URL);
		if (productUrl == null) {
			throw new Exception("product url does not exist");
		}
		if (this.productNumberLength > 0) {
			productNumber = StrUtil.prefixLength(productNumber, this.productNumberLength, '0');
		}
		String url = productUrl.getValue().replaceAll("#pid", productNumber);
		
		String content = httpClient.get(url);
		if (content != null) {
			sb.setLength(0);
			sb.append("Got product id:").append(productNumber).append(" in ").append(store.getName());
			LOG.info(sb.toString());
			
			return ConverterManager.convert(store, content);
		} else {
			sb.setLength(0);
			sb.append("Product id:").append(productNumber).append(" does not exist in ").append(store.getName());
			LOG.info(sb.toString());
			
			return null;
		}
	}
	
	@Override
	public void run() {
		statusMsg = "开始同步";
		for (RetrieverListener listener : listeners) {
			listener.syncStarted(store, statusMsg);
		}
//		
//		try {
//			Main.getService().clearProducts(store);
//		} catch (Exception e) {
//			LOG.debug("delete all product failed", e);
//		}
		
		int productCount = 0;
		StringBuilder sb = new StringBuilder();
		
		sb.append("Start fetching all products from ").append(store.getName());
		LOG.info(sb.toString());
		StoreProp minProductId = storePropMap.get(StoreProp.PROP_MIN_PRODUCTID);
		StoreProp maxProductId = storePropMap.get(StoreProp.PROP_MAX_PRODUCTID);
		
		int minId = 0;
		int maxId = 0;
		if (minProductId != null && maxProductId != null) {
			try {
				minId = Integer.parseInt(minProductId.getValue());
			} catch (Exception e) {}
			
			try {
				maxId = Integer.parseInt(maxProductId.getValue());
			} catch (Exception e) {}
			
			if(minId >= 0 && maxId > 0 && maxId >= minId) {
				try {
					for (int id=minId; id<=maxId; id++) {
						String productNumber = String.valueOf(id);
						Product product = null;
						statusMsg = "正在获取产品编号: " + productNumber + ", 已同步数量: " + productCount;
						for (int ti=0; ti<3; ti++) {
							try {
								product = syncOne(productNumber, id == minId);
								break;
							} catch (SocketTimeoutException ste) {
								LOG.debug("fetch product: " + productNumber + " failed", ste);
							}
						}
						
						if (product != null) {
							product.setNumber(productNumber);
							Main.getService().createProduct(product);
							productCount++;
							
							for (RetrieverListener listener : listeners) {
								listener.syncStep(store, productCount, statusMsg);
							}
						}
						
						if (stopped)
							break;
					}
				} catch (Exception e) {
					sb.setLength(0);
					sb.append("Fetch product failed from ").append(store.getName()).append(" : ");
					LOG.debug(sb.toString(), e);
				}
			}
		}
		
		synchronized(thread_lock) {
			thread = null;
		}
		
		statusMsg = "同步完成，同步数量: " + productCount;
		
		for (RetrieverListener listener : listeners) {
			listener.syncStopped(store, statusMsg);
		}
		
		sb.setLength(0);
		sb.append("Finish fetching all products from ").append(store.getName());
		LOG.info(sb.toString());
	}
	
	public String getStatusMsg() {
		return statusMsg;
	}
}
