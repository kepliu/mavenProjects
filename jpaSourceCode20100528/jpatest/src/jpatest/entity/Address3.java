package jpatest.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ADDRESS2")
public class Address3 extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private Address3PK pk;

	private String city;

	private String state;

	public Address3PK getPk() {
		return pk;
	}

	public Address3() {
		super();
	}
	
	public Address3(Address3PK pk) {
		this.pk = pk;
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
}
