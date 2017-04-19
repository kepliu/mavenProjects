package jpatest.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Single-table inheritance mapping
 */

@Entity
@DiscriminatorValue(value = "P")
public class PreferredCustomer2 extends Customer2 {

	@Column(name="DISCOUNT_RATE")
	protected double discountRate;

	@Column(name="EXPIRATION_DATE")
	@Temporal(TemporalType.DATE)
	protected Date expirationDate;


	private static final long serialVersionUID = 1L;

	public PreferredCustomer2() {
		super();
	}

	public double getDiscountRate() {
		return this.discountRate;
	}

	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}

	public Date getExpirationDate() {
		return this.expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
}
