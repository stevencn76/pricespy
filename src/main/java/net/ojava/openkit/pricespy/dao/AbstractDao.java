package net.ojava.openkit.pricespy.dao;

import java.io.Serializable;
import java.util.List;

import net.ojava.openkit.pricespy.entity.BaseEntity;

public abstract interface AbstractDao<E extends BaseEntity> {

	public abstract Serializable save(E obj);

	public abstract E findById(Serializable id);

	public abstract List<E> queryById(Serializable id);

	public abstract void saveOrUpdate(E obj);

	public abstract void update(E obj);

	public abstract void delete(E obj);

	public abstract int deleteAll();

	public abstract List<E> getAll();

	public abstract List<E> getAllInOrder(String orderHql);

	public abstract long getTotalCount();
	
	public abstract List<E> queryList(String orderHql);
	
	public abstract long queryMax(String property);
	
	public abstract E findByProperty(final String propertyName, final Object value);
}
