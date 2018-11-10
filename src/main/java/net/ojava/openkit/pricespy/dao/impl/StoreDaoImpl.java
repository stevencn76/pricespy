package net.ojava.openkit.pricespy.dao.impl;

import org.springframework.stereotype.Repository;

import net.ojava.openkit.pricespy.dao.BaseDaoHibernate;
import net.ojava.openkit.pricespy.dao.StoreDao;
import net.ojava.openkit.pricespy.entity.Store;

@Repository("storeDao")
public class StoreDaoImpl extends BaseDaoHibernate<Store> implements StoreDao {

}
