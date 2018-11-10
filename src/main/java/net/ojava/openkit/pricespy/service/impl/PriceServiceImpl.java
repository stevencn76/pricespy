package net.ojava.openkit.pricespy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.ojava.openkit.pricespy.dao.ParameterDao;
import net.ojava.openkit.pricespy.dao.ProductDao;
import net.ojava.openkit.pricespy.dao.StoreDao;
import net.ojava.openkit.pricespy.dao.StorePropDao;
import net.ojava.openkit.pricespy.entity.Parameter;
import net.ojava.openkit.pricespy.entity.Product;
import net.ojava.openkit.pricespy.entity.Store;
import net.ojava.openkit.pricespy.entity.StoreProp;
import net.ojava.openkit.pricespy.service.PriceService;

@Transactional(propagation=Propagation.REQUIRED)
@Service("priceService")
public class PriceServiceImpl implements PriceService {
	
	@Autowired
	private ParameterDao parameterDao;
	
	@Autowired
	private StoreDao storeDao;
	
	@Autowired
	private StorePropDao storePropDao;
	
	@Autowired
	private ProductDao productDao;

	@Override
	public List<Parameter> findAllParameters() throws Exception {
		return parameterDao.getAll();
	}

	@Override
	public Parameter findParameter(String name) throws Exception {
		return parameterDao.findByProperty("name", name);
	}

	@Override
	public List<Store> findAllStores() throws Exception {
		return storeDao.getAll();
	}
	
	@Override
	public Store saveStore(Store store) throws Exception {
		storeDao.saveOrUpdate(store);
		return store;
	}

	@Override
	public StoreProp findStoreProp(Store store, String name) throws Exception {
		return storePropDao.findStoreProp(store, name);
	}
	
	@Override
	public List<StoreProp> findStoreProps(Store store) throws Exception {
		return storePropDao.findByStore(store);
	}
	
	@Override
	public Product createProduct(Product product) throws Exception {
		if (product.getStore() == null || product.getStore().getId() == null)
			throw new Exception("No store assigned for product");
		
		Store store = storeDao.findById(product.getStore().getId());
		if (store == null) 
			throw new Exception("Store does not exist, id: " + product.getStore().getId());
		
		product.setStore(store);
		
		Product oldProduct = productDao.findProduct(store, product.getNumber());
		if (oldProduct != null) {
			oldProduct.updateProduct(product);
			product.setId(oldProduct.getId());
			
			return oldProduct;
		} else {
			productDao.saveOrUpdate(product);
			
			return product;
		}
	}

	@Override
	public List<Product> findProducts(String[] namePattern) throws Exception {
		return productDao.findProductByName(namePattern);
	}

	@Override
	public long getProductCount(Store store) throws Exception {
		return productDao.getCount(store);
	}

	@Override
	public void clearProducts(Store store) throws Exception {
		productDao.deleteProducts(store);
	}
}
