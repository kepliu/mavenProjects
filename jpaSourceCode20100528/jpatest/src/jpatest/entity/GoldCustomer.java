package jpatest.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * joined-table inheritance mapping
 */

@Entity
@Table(name = "GOLD_CUSTOMER")
@DiscriminatorValue(value = "G")
@PrimaryKeyJoinColumn(name = "CUSTOMER_ID_FPK", referencedColumnName = "CUSTOMER_ID_PK")
public class GoldCustomer extends Customer {

	@Column(name = "CARD_NUMBER")
	protected String cardNumber;

	@Column(name = "CREDIT_LIMIT")
	protected double creditLimit;

	private static final long serialVersionUID = 1L;

	public GoldCustomer() {
		super();
	}

	public String getCardNumber() {
		return this.cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public double getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(double creditLimit) {
		this.creditLimit = creditLimit;
	}
	
}
