package net.ojava.openkit.pricespy.dao.impl;

import org.springframework.stereotype.Repository;

import net.ojava.openkit.pricespy.dao.BaseDaoHibernate;
import net.ojava.openkit.pricespy.dao.ParameterDao;
import net.ojava.openkit.pricespy.entity.Parameter;

@Repository("parameterDao")
public class ParameterDaoImpl extends BaseDaoHibernate<Parameter> implements ParameterDao {

}
