package net.ojava.openkit.pricespy.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author songshr
 *
 */
public abstract interface ResultSetHandler<T> {

	public abstract T handle(ResultSet resultSet)
			throws SQLException;
}
