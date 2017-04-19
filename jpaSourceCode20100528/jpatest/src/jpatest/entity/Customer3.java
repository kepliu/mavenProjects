package jpatest.entity;

import javax.persistence.AssociationOverride;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

/**
 * table per class inheritance mapping
 *
 */

@Entity
@Table(name = "CUSTOMER3")
@AssociationOverride(name="address", joinColumns=@JoinColumn(name = "ADDRESS_ID_FK", referencedColumnName="ADDRESS_ID_PK"))
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Customer3 extends User {
	@Id
	//@TableGenerator(name = "CUST_SEQ_GEN", schema="JPATEST", table = "SEQUENCE_GENERATOR_TB", pkColumnName = "SEQUENCE_NAME", valueColumnName = "SEQUENCE_VALUE", pkColumnValue = "CUSTOMER_SEQ")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CUST_SEQ_GEN")
	@Column(name = "CUSTOMER_ID_PK", updatable=false)
	protected int customerId;

	@Embedded
	@AttributeOverrides( {
		@AttributeOverride(name = "bankName", column = @Column(name = "BANK_NAME")),
		@AttributeOverride(name = "accountNumber", column = @Column(name = "ACCOUNT_NUMBER")),
		@AttributeOverride(name = "routingNumber", column = @Column(name = "ROUTING_NUMBER")) 
	})
	protected BankInfo bank;

//	@OneToMany(mappedBy = "customer")
//	protected Set<Order> orders;    // inverse side of Order to Customer relationship

	private static final long serialVersionUID = 1L;

	public Customer3() {
		super();
	}

	public int getCustomerId() {
		return this.customerId;
	}

	/*
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

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
	*/
	
	public BankInfo getBank() {
		return bank;
	}

	public void setBank(BankInfo bank) {
		this.bank = bank;
	}

	/*
	public Set<Order> getOrders() {
		return this.orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}
	*/

}
