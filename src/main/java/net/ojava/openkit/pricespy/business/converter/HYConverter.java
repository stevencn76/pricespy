package net.ojava.openkit.pricespy.business.converter;

import net.ojava.openkit.pricespy.entity.Product;
import net.ojava.openkit.pricespy.util.StrUtil;

public class HYConverter implements IConverter {

	@Override
	public Product convert(String content) {
		String pictureContent = StrUtil.subString(content, "<div class=\"goods-detail-pic\"", "<div class='goods-pic-magnifier'");
		String picUrl = StrUtil.subString(pictureContent, "<img src=\"", "\" alt=\"");
		String name = StrUtil.subString(content, "<h1 class=\"goodsname\">", "</h1>");
		if (StrUtil.isEmpty(name))
			return null;
		
		Product product = new Product();
		product.setPicUrl(picUrl);
		product.setName(name);
		String price = StrUtil.subString(content, "<span class=\"price1\">", "</span>");
		// NZ16.50 / ￥79.04
		String snz = StrUtil.subString(price, "NZ", " ");
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
