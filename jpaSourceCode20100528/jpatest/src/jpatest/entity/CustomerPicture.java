package jpatest.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="CUST_PICTURE")
public class CustomerPicture {
	@Id
	protected int customerId;
	
	@MapsId
	@OneToOne
	@JoinColumn(name = "CUSTOMER_ID_FPK", referencedColumnName="CUSTOMER_ID_PK" )
	protected Customer customer;
	
	@Lob
	@Column(name="PICTURE", columnDefinition="BLOB NOT NULL")
	protected byte[] picture;
	
	public int getCustomerId() {
		return customerId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}
}
