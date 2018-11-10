package net.ojava.openkit.pricespy.business.converter;

import net.ojava.openkit.pricespy.entity.Product;

public interface IConverter {
	public Product convert(String content);
}
