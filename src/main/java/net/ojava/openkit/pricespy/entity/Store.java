package net.ojava.openkit.pricespy.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "t_stores")
@XmlRootElement
public class Store extends BaseEntity {
	private static final long serialVersionUID = 2086040663326998265L;

	private Integer id;
	private String name;
	private String website;
	private boolean requireLogin;
	private boolean retired;
	private Timestamp lastSyncTime;
	private long productCount;
	private String converterName;
	
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

	@Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "website")
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}

	@Column(name = "require_login")
	public boolean isRequireLogin() {
		return requireLogin;
	}
	public void setRequireLogin(boolean requireLogin) {
		this.requireLogin = requireLogin;
	}

	@Column(name = "retired")
	public boolean isRetired() {
		return retired;
	}
	public void setRetired(boolean retired) {
		this.retired = retired;
	}

	@Column(name = "last_sync_time")
	public Timestamp getLastSyncTime() {
		return lastSyncTime;
	}
	public void setLastSyncTime(Timestamp lastSyncTime) {
		this.lastSyncTime = lastSyncTime;
	}

	@Column(name = "product_count")
	public long getProductCount() {
		return productCount;
	}
	public void setProductCount(long productCount) {
		this.productCount = productCount;
	}

	@Column(name = "converter_name")
	public String getConverterName() {
		return converterName;
	}
	public void setConverterName(String converterName) {
		this.converterName = converterName;
	}
	
	@Override
	public int hashCode() {
		if (name == null)
			return super.hashCode();
		
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Store) {
			Store s = (Store)obj;
			
			if(s.getId() == null || this.id == null)
				return false;
			
			return s.getId().intValue() == id;
		}
		return false;
	}
	
	
}
