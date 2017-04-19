package jpatest.entity;

import javax.persistence.AssociationOverride;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

/**
 * Single-table inheritance mapping
 */

@Entity
@Table(name = "CUSTOMER2")
@AssociationOverride(name="address", joinColumns=@JoinColumn(
		name = "ADDRESS_ID_FK", referencedColumnName="ADDRESS_ID_PK"))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CUSTOMER_TYPE", discriminatorType = DiscriminatorType.STRING, length = 1)
@DiscriminatorValue(value = "C")
public class Customer2 extends User {
	@Id
	//@TableGenerator(name = "CUST_SEQ_GEN", schema="JPATEST", table = "SEQUENCE_GENERATOR_TB", pkColumnName = "SEQUENCE_NAME", valueColumnName = "SEQUENCE_VALUE", pkColumnValue = "CUSTOMER_SEQ")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CUST_SEQ_GEN")
	@Column(name = "CUSTOMER_ID_PK", updatable=false)
	protected int customerId;

	@Column(name = "CUSTOMER_TYPE", nullable=false)
	@Enumerated(EnumType.STRING)
	protected CustomerType customerType;

	@Embedded
	@AttributeOverrides( {
		@AttributeOverride(name = "bankName", column = @Column(name = "BANK_NAME")),
		@AttributeOverride(name = "accountNumber", column = @Column(name = "ACCOUNT_NUMBER")),
		@AttributeOverride(name = "routingNumber", column = @Column(name = "ROUTING_NUMBER")) 
	})
	protected BankInfo bank;

	private static final long serialVersionUID = 1L;

	public Customer2() {
		super();
	}

	public int getCustomerId() {
		return this.customerId;
	}

	public CustomerType getCustomerType() {
		return this.customerType;
	}

	public void setCustomerType(CustomerType customerType) {
		this.customerType = customerType;
	}

	public BankInfo getBank() {
		return bank;
	}

	public void setBank(BankInfo bank) {
		this.bank = bank;
	}
}
