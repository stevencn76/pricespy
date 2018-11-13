package net.ojava.openkit.pricespy.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "t_products")
@XmlRootElement
public class Product extends BaseEntity {
	private static final long serialVersionUID = 2086040663326998265L;

	private Integer id;
	private Store store;
	private String number;
	private String name;
	private double nzPrice;
	private double cnPrice;
	private String picUrl;
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	@Column(name = "id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name="store_id")
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
	}

	@Column(name = "product_id")
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "nz_price")
	public double getNzPrice() {
		return nzPrice;
	}
	public void setNzPrice(double nzPrice) {
		this.nzPrice = nzPrice;
	}

	@Column(name = "cn_price")
	public double getCnPrice() {
		return cnPrice;
	}
	public void setCnPrice(double cnPrice) {
		this.cnPrice = cnPrice;
	}

	@Column(name = "pic_url")
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	
	public void updateProduct(Product product) {
		this.setName(product.getName());
		this.setNzPrice(product.getNzPrice());
		this.setCnPrice(product.getCnPrice());
		this.setPicUrl(product.getPicUrl());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Product) {
			Product p = (Product)obj;
			
			if (p.getId() == null || this.getId() == null)
				return false;
			
			return p.getId().intValue() == this.getId();
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		if (this.getId() == null)
			return super.hashCode();
		
		return this.getId().hashCode();
	}
}
