package jpatest.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Single-table inheritance mapping
 */

@Entity
@DiscriminatorValue(value = "G")
public class GoldCustomer2 extends Customer2 {

	@Column(name = "CARD_NUMBER")
	protected String cardNumber;

	@Column(name = "CREDIT_LIMIT")
	protected double creditLimit;

	private static final long serialVersionUID = 1L;

	public GoldCustomer2() {
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
