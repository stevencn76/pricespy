package net.ojava.openkit.pricespy.business.converter;

import java.util.HashMap;
import java.util.Map;

import net.ojava.openkit.pricespy.entity.Product;
import net.ojava.openkit.pricespy.entity.Store;

public class ConverterManager {
	private static Map<String, IConverter> converterMap = new HashMap<>();

	public static Product convert(Store store, String content) throws Exception {
		IConverter converter = getConverter(store);
		
		Product product = converter.convert(content);
		if (product != null) {
			product.setStore(store);
		}
		
		return product;
	}
	
	private static IConverter getConverter(Store store) throws Exception {
		IConverter converter = converterMap.get(store.getConverterName());
		if (converter == null) {
			converter = (IConverter)Class.forName(store.getConverterName()).newInstance();
			
			converterMap.put(store.getConverterName(), converter);
		}
		
		return converter;
	}
}
