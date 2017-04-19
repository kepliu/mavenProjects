package jpatest.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(Address2PK.class)
public class Address2 extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "STREET_PK", updatable=false, insertable=true,  nullable=false)
	private String street;

	private String city;

	private String state;

	@Id
	@Column(name = "ZIP_PK", updatable=false, insertable=true,  nullable=false)
	private String zip;

	public Address2() {
		super();
	}
	
	public Address2(String street, String zip) {
		this.street = street;
		this.zip = zip;
	}

	public String getStreet() {
		return this.street;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return this.zip;
	}

}
