package net.ojava.openkit.pricespy.business.converter;

import net.ojava.openkit.pricespy.entity.Product;
import net.ojava.openkit.util.StrUtil;

public class NZHConverter implements IConverter {

	@Override
	public Product convert(String content) {
		String pictureContent = StrUtil.subString(content, "<div id=\"picshow-img\">", "</ul></div>");
		String picUrl = StrUtil.subString(pictureContent, "<ul><li style=\"background-image:url(", ");display:block");
		String name = StrUtil.subString(content, "<div class=\"detail-col-right\"><h3>", "</h3><p class=\"detail-desc\">");
		if (StrUtil.isEmpty(name))
			return null;
		
		Product product = new Product();
		product.setPicUrl(picUrl);
		product.setName(name);
		String price = StrUtil.subString(content, "<span class=\"info-detail price\">", "</span>");
		// NZ16.50 / ￥79.04
		String snz = StrUtil.subString(price, "NZD $", "/");
		String scn = StrUtil.subString(price, "￥", null);
		
		try {
			product.setNzPrice(Double.parseDouble(snz));
		} catch (Exception e) {}
		
		try {
			product.setCnPrice(Double.parseDouble(scn));
		} catch (Exception e) {}
		
		return product;
	}

}
