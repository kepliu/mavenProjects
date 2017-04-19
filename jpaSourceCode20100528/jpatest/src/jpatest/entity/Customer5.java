package jpatest.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.FieldResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * This class is for SQL result set mapping with composite foreign key to 
 * Address2 (and Address3).
 * For completeness, we still keep the inheritance hierarchy, with
 * joined-table inheritance mapping
 *
 */

@Entity
@Table(name = "CUSTOMER5")
@TableGenerator(name = "CUST_SEQ_GEN5", schema="JPATEST", table = "SEQUENCE_GENERATOR_TB", pkColumnName = "SEQUENCE_NAME", valueColumnName = "SEQUENCE_VALUE", pkColumnValue = "CUSTOMER_SEQ")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "CUSTOMER_TYPE", discriminatorType = DiscriminatorType.STRING, length = 1)
@DiscriminatorValue(value = "C")
@SqlResultSetMappings({
  @SqlResultSetMapping(name="customer5Address2RSmapping", 
  	 entities ={
  		  @EntityResult(entityClass=Customer5.class, 
  		  		fields={@FieldResult(name="customerId", column="CUSTOMER_ID_PK"),
  		  	          @FieldResult(name="address2.street", column="STREET_PK"),
  		  	          @FieldResult(name="address2.zip", column="ZIP_PK")},
  		  	  discriminatorColumn="CUSTOMER_TYPE"),
  		  @EntityResult(entityClass=Address2.class) }),
  		  
  @SqlResultSetMapping(name="customer5Address3RSmapping", 
  	 entities ={
  		  @EntityResult(entityClass=Customer5.class, 
  		   		 fields={@FieldResult(name="customerId", column="CUSTOMER_ID_PK"),
  		   		  	     @FieldResult(name="address3.pk.street", column="STREET_PK"),
  		   		  	     @FieldResult(name="address3.pk.zip", column="ZIP_PK")},
  		   		 discriminatorColumn="CUSTOMER_TYPE"),
  		  @EntityResult(entityClass=Address3.class) })
})
public class Customer5 extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CUST_SEQ_GEN5")
	@Column(name = "CUSTOMER_ID_PK")
	protected int customerId;

	@Column(name = "CUSTOMER_TYPE", nullable=false)
	@Enumerated(EnumType.STRING)
	protected CustomerType customerType;

	
	protected String name;

	@Lob
	@Basic(fetch=FetchType.LAZY, optional=false)
	@Column(name="PICTURE", columnDefinition="BLOB NOT NULL")
	protected byte[] picture;
	
	protected double income;
	
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	@JoinColumns({
    @JoinColumn(name="STREET_FK", referencedColumnName="STREET_PK"),
    @JoinColumn(name="ZIP_FK", referencedColumnName="ZIP_PK") })
	protected Address2 address2;
	
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	@JoinColumns({
    @JoinColumn(name="STREET_FK", referencedColumnName="STREET_PK", insertable=false, updatable=false),
    @JoinColumn(name="ZIP_FK", referencedColumnName="ZIP_PK", insertable=false, updatable=false) })
	protected Address3 address3;
	

	@Embedded
	@AttributeOverrides( {
		@AttributeOverride(name = "bankName", column = @Column(name = "BANK_NAME")),
		@AttributeOverride(name = "accountNumber", column = @Column(name = "ACCOUNT_NUMBER")),
		@AttributeOverride(name = "routingNumber", column = @Column(name = "ROUTING_NUMBER")) 
	})
	protected BankInfo bank;

	private static final long serialVersionUID = 1L;

	public Customer5() {
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
	
	public Address2 getAddress2() {
		return this.address2;
	}

	public void setAddress2(Address2 address) {
		this.address2 = address;
	}
	
	public Address3 getAddress3() {
		return this.address3;
	}

	public void setAddress3(Address3 address) {
		this.address3 = address;
	}
	
	public BankInfo getBank() {
		return bank;
	}

	public void setBank(BankInfo bank) {
		this.bank = bank;
	}

	
	public double getIncome() {
		return income;
	}

	public void setIncome(double income) {
		this.income = income;
	}

}
