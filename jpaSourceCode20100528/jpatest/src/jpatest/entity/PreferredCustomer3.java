package jpatest.entity;

import java.util.Date;

import javax.persistence.AssociationOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * table per class inheritance mapping
 */

@Entity
@Table(name="PREFERRED_CUSTOMER3")
@AssociationOverride(name="address", joinColumns=@JoinColumn(name = "ADDRESS_ID_FK", referencedColumnName="ADDRESS_ID_PK"))
public class PreferredCustomer3 extends Customer3 {

	@Column(name="DISCOUNT_RATE")
	protected double discountRate;

	@Column(name="EXPIRATION_DATE")
	@Temporal(TemporalType.DATE)
	protected Date expirationDate;

	private static final long serialVersionUID = 1L;

	public PreferredCustomer3() {
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
