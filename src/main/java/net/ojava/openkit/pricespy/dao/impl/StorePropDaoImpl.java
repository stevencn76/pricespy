package net.ojava.openkit.pricespy.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import net.ojava.openkit.pricespy.dao.BaseDaoHibernate;
import net.ojava.openkit.pricespy.dao.StorePropDao;
import net.ojava.openkit.pricespy.entity.Store;
import net.ojava.openkit.pricespy.entity.StoreProp;

@Repository("storePropDao")
public class StorePropDaoImpl extends BaseDaoHibernate<StoreProp> implements StorePropDao {

	@Override
	public StoreProp findStoreProp(Store store, String name) {
		DetachedCriteria c = DetachedCriteria.forClass(StoreProp.class)
				.add(Restrictions.eq("store", store))
				.add(Restrictions.eq("name", name));
		List<StoreProp> storePropList = this.findByCriteria(c);
		
		if (storePropList != null && storePropList.size() > 0)
			return storePropList.get(0);
		
		return null;
	}


	@Override
	public List<StoreProp> findByStore(Store store) throws Exception {
		DetachedCriteria c = DetachedCriteria.forClass(StoreProp.class)
				.add(Restrictions.eq("store", store));
		List<StoreProp> storePropList = this.findByCriteria(c);
		
		if (storePropList != null && storePropList.size() > 0)
			return storePropList;
		
		return null;
	}
}
