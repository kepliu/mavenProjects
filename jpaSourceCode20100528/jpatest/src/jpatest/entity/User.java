package jpatest.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

@MappedSuperclass
@Access(AccessType.FIELD)
public class User extends BaseEntity {
	
	protected String name;
	
	protected byte[] picture;

	@OneToOne(cascade={CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	@JoinColumn(name = "ADDRESS_ID", referencedColumnName="ADDRESS_ID_PK")
	protected Address address;
	
	protected double income;
	
	private static final long serialVersionUID = 1L;

	public User() {
		super();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Access(AccessType.PROPERTY)
	@Lob
	@Basic(fetch=FetchType.LAZY, optional=false)
	@Column(name="PICTURE", columnDefinition="BLOB NOT NULL")
	public byte[] getPicture() {
		return this.picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}
	
	public Address getAddress() {
		return this.address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}	
	
	public double getIncome() {
		return income;
	}

	public void setIncome(double income) {
		this.income = income;
	}

}
