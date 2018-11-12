package net.ojava.openkit.pricespy.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;

import net.ojava.openkit.pricespy.entity.BaseEntity;
import net.ojava.openkit.pricespy.entity.Pagination;


public abstract class BaseDaoHibernate<E extends BaseEntity> implements AbstractDao<E> {

	protected static final String COUNT_STR = "select count(*) ";
	protected static final String COUNT_ID = "select count(id) ";
	protected static final int PAGE_SIZE = 20;
	protected final Class<E> clazz;

	protected boolean cacheable = false;
	
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;

	public Session getSession() {
	    return sessionFactory.getCurrentSession();
	}

	public boolean isCacheable() {

		return cacheable;
	}

	protected BaseDaoHibernate() {

		this(false);
	}

	@SuppressWarnings("unchecked")
	protected BaseDaoHibernate(boolean cacheable) {

		clazz = (Class<E>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		this.cacheable = cacheable;
	}

	protected BaseDaoHibernate(Class<E> clazz) {

		this(clazz, false);
	}

	protected BaseDaoHibernate(Class<E> clazz, boolean cacheable) {

		this.clazz = clazz;
		if (clazz == null)
			throw new IllegalArgumentException("Invalidate clazz.");
		this.cacheable = cacheable;
	}

	public Serializable save(E obj) {

		if (obj == null)
			return null;
		return getSession().save(obj);
	}

	public void saveOrUpdate(E obj) {

		if (obj == null)
			return;
		getSession().saveOrUpdate(obj);
	}

	@SuppressWarnings("unchecked")
	public E findById(Serializable id) {
		return (id == null) ? null : (E)(getSession().get(clazz, id));
	}

	public Pagination pageById(Serializable id) {

		Pagination pg = new Pagination();
		List<E> rs = queryById(id);
		long totalCount = rs.size();
		pg.setTotalCount(totalCount);
		pg.setResult(rs);
		pg.setCurrentPage(1);
		pg.setPageSize(PAGE_SIZE);
		pg.setPageCount(Pagination.getPageCount(totalCount, PAGE_SIZE));
		return pg;
	}

	public List<E> queryById(Serializable id) {

		List<E> rs = new ArrayList<E>();
		E record = findById(id);
		if (record != null)
			rs.add(record);
		return rs;
	}

	@SuppressWarnings("unchecked")
	public E findByProperty(final String propertyName, final Object value) {
		DetachedCriteria criteria = DetachedCriteria.forClass(clazz);
		criteria.add(Restrictions.eq(propertyName, value));
		List<E> res = criteria.getExecutableCriteria(getSession()).list();
		if (res != null && res.size() > 0)
			return res.get(0);
		return null;
		/*
		 * Assert.hasText(propertyName, "propertyName must specified."); return
		 * (E) getHibernateTemplate().execute(new HibernateCallback<E>() {
		 * 
		 * public E doInHibernate(Session session) throws HibernateException,
		 * SQLException {
		 * 
		 * Criteria criteria = session.createCriteria(clazz).add(
		 * Restrictions.eq(propertyName, value)); return (E)
		 * criteria.uniqueResult(); } });
		 */
	}

	public void update(E obj) {
		if (obj == null)
			return;
		getSession().update(obj);
		getSession().flush();
	}

	public void delete(E obj) {

		if (obj == null)
			return;
		getSession().delete(obj);
	}

	public int deleteAll() {

		String hql = "delete from " + clazz.getName();
		return getSession().createQuery(hql).executeUpdate();
	}

	@Override
	public List<E> getAll() {

		return getAllInOrder(null);
	}

	@SuppressWarnings("unchecked")
	public List<E> getAllInOrder(String orderHql) {

		if (clazz == null)
			return new ArrayList<E>();
		String hql = "from " + clazz.getName();
		if (StringUtils.hasText(orderHql))
			hql = hql + " " + orderHql;
		return getSession().createQuery(hql).list();
	}
	
	@SuppressWarnings("unchecked")
	protected List<E> findByCriteria(DetachedCriteria c) {
		return c.getExecutableCriteria(getSession()).list();
	}

	@SuppressWarnings("unchecked")
	protected E queryOne(String hql) {

		List<?> e = getSession().createQuery(hql).list();
		if (null != e & e.size() > 0)
			return (E) e.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<E> queryList(String hqlStr) {

		if (clazz == null)
			return new ArrayList<E>();
		String hql = "from " + clazz.getName();
		if (StringUtils.hasText(hqlStr))
			hql = hql + " " + hqlStr;
		return getSession().createQuery(hql).list();
	}

	protected List<?> queryList(DetachedCriteria criteria) {

		if (criteria == null)
			return new ArrayList<Object>();
		return criteria.getExecutableCriteria(getSession()).list();
	}

	protected int executeHql(String hql) {

		return getSession().createQuery(hql).executeUpdate();
	}

	protected long querySize(Class<E> clazz) {

		String hql = "select count(*) from " + clazz.getName();
		return querySize(hql);
	}

	protected long querySize(String hql) {

		return querySize(hql, (Object[]) null);
	}

	protected long querySize(String hql, Object value) {

		return querySize(hql, new Object[] { value });
	}

	public long getTotalCount() {
		return this.querySize(clazz);
	}

	public long queryMax(String property) {
		Object obj = getSession()
				.createCriteria(clazz)
				.setProjection(
						Projections.projectionList().add(
								Projections.max(property))).uniqueResult();
		
		if(obj == null)return 0;
		
		if(obj instanceof Integer)
			return ((Integer)obj).intValue();
		else if(obj instanceof Long)
			return ((Long)obj).longValue();
		else
			return 0;
	}
}
