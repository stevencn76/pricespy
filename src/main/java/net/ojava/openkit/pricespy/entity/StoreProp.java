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
@Table(name = "t_storeprops")
@XmlRootElement
public class StoreProp extends BaseEntity {
	private static final long serialVersionUID = 2086040663326998265L;
	
	public static final String PROP_LOGIN_URL = "login_url";
	public static final String PROP_LOGIN_FORM = "login_form";
	public static final String PROP_PRODUCT_URL = "product_url";
	public static final String PROP_MIN_PRODUCTID = "min_productid";
	public static final String PROP_MAX_PRODUCTID = "max_productid";
	public static final String PROP_PID_LENGTH = "pid_length";

	private Integer id;
	private Store store;
	private String name;
	private String value;
	
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

	@Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "value")
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
