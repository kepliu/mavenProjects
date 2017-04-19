package jpatest.entity;

import javax.persistence.AssociationOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

/**
 * table per class inheritance mapping
 *
 */

@Entity
@Table(name="GOLD_CUSTOMER3")
@AssociationOverride(name="address", joinColumns=@JoinColumn(name = "ADDRESS_ID_FK", referencedColumnName="ADDRESS_ID_PK"))
public class GoldCustomer3 extends Customer3 {

	@Column(name = "CARD_NUMBER")
	protected String cardNumber;

	@Column(name = "CREDIT_LIMIT")
	protected double creditLimit;

	private static final long serialVersionUID = 1L;

	public GoldCustomer3() {
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
