package net.ojava.openkit.pricespy.dao;

import java.util.List;

import net.ojava.openkit.pricespy.entity.Product;
import net.ojava.openkit.pricespy.entity.Store;

public interface ProductDao extends AbstractDao<Product> {
	public List<Product> findProductByName(String[] namePattern);
	
	public Product findProduct(Store store, String number);
	
	public long getCount(Store store);
	
	public void deleteProducts(Store store);
}
