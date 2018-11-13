package net.ojava.openkit.pricespy.service;

import java.util.List;

import net.ojava.openkit.pricespy.entity.Parameter;
import net.ojava.openkit.pricespy.entity.Product;
import net.ojava.openkit.pricespy.entity.Store;
import net.ojava.openkit.pricespy.entity.StoreProp;

public interface PriceService {
	public List<Parameter> findAllParameters() throws Exception;
	public Parameter findParameter(String name) throws Exception;
	public Parameter saveParameter(Parameter param) throws Exception;
	
	public List<Store> findAllStores() throws Exception;
	public Store saveStore(Store store) throws Exception;
	
	public StoreProp findStoreProp(Store store, String name) throws Exception;
	public List<StoreProp> findStoreProps(Store store) throws Exception;
	
	public Product createProduct(Product product) throws Exception;
	public Product saveProduct(Product product) throws Exception;
	public List<Product> findProducts(String[] namePattern) throws Exception;
	public long getProductCount(Store store) throws Exception;
	public void clearProducts(Store store) throws Exception;
}
