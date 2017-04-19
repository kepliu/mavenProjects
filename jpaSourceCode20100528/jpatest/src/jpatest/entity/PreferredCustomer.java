package jpatest.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * joined-table inheritance mapping
 */

@Entity
@Table(name="PREFERRED_CUSTOMER")
@DiscriminatorValue(value = "P")
@PrimaryKeyJoinColumn(name = "CUSTOMER_ID_FPK", referencedColumnName = "CUSTOMER_ID_PK")
public class PreferredCustomer extends Customer {

	@Column(name="DISCOUNT_RATE")
	protected double discountRate;

	@Column(name="EXPIRATION_DATE")
	@Temporal(TemporalType.DATE)
	protected Date expirationDate;


	private static final long serialVersionUID = 1L;

	public PreferredCustomer() {
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
