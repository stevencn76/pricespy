package net.ojava.openkit.pricespy.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import net.ojava.openkit.pricespy.dao.BaseDaoHibernate;
import net.ojava.openkit.pricespy.dao.ProductDao;
import net.ojava.openkit.pricespy.entity.Product;
import net.ojava.openkit.pricespy.entity.Store;

@Repository("productDao")
public class ProductDaoImpl extends BaseDaoHibernate<Product> implements ProductDao {
	@SuppressWarnings("unchecked")
	@Override
	public List<Product> findProductByName(String[] namePattern) {
		List<Criterion> criterionList = new ArrayList<>();
		if (namePattern != null) {
			for(String ts : namePattern) {
				if (ts != null && ts.length() > 0) {
					criterionList.add(Restrictions.like("name", "%" + ts + "%"));
				}
			}
		}
		Criterion combinedCriterion = null;
		if (criterionList.size() == 1) {
			combinedCriterion = criterionList.get(0);
		} else if (criterionList.size() > 1) {
			Criterion c1 = criterionList.get(0);
			for (int i=1; i<criterionList.size(); i++) {
				Criterion c2 = criterionList.get(i);
				if (combinedCriterion == null) {
					combinedCriterion = Restrictions.and(c1, c2);
				} else {
					combinedCriterion = Restrictions.and(combinedCriterion, c2);
				}
			}
		}
		
		DetachedCriteria c = DetachedCriteria.forClass(Product.class);
		if (combinedCriterion != null) {
			c.add(combinedCriterion);
		}
		c.addOrder(Order.asc("name"));
		return this.getMyHibernateTemplate().findByCriteria(c);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Product findProduct(Store store, String number) {
		DetachedCriteria c = DetachedCriteria.forClass(Product.class)
				.add(Restrictions.eq("store", store))
				.add(Restrictions.eq("number", number));
		List<Product> productList = this.getHibernateTemplate().findByCriteria(c);
		
		if (productList != null && productList.size() > 0)
			return productList.get(0);
		
		return null;
	}

	@Override
	public long getCount(Store store) {
		return querySize("select count(*) from Product p where p.store=?", store);
	}

	@Override
	public void deleteProducts(Store store) {
		getHibernateTemplate().bulkUpdate("delete from Product p where p.store.id = " + store.getId());
	}
}
