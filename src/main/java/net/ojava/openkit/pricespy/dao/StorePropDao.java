package net.ojava.openkit.pricespy.dao;

import java.util.List;

import net.ojava.openkit.pricespy.entity.Store;
import net.ojava.openkit.pricespy.entity.StoreProp;

public interface StorePropDao extends AbstractDao<StoreProp> {
	public StoreProp findStoreProp(Store store, String name);
	public List<StoreProp> findByStore(Store store) throws Exception;
}
