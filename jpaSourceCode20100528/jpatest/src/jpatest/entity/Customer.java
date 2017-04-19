package jpatest.entity;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.AssociationOverride;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.CollectionTable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.Valid;
import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;

import jpatest.groups.Basic;
import jpatest.groups.Extended;

/*
import org.eclipse.persistence.annotations.NamedStoredProcedureQueries;
import org.eclipse.persistence.annotations.NamedStoredProcedureQuery;
import org.eclipse.persistence.annotations.StoredProcedureParameter;
import org.eclipse.persistence.annotations.Direction;
*/



/**
 * joined-table inheritance mapping 
 */

@Entity
@Table(name = "CUSTOMER")
@TableGenerator(name = "CUST_SEQ_GEN", table = "SEQUENCE_GENERATOR_TB", pkColumnName = "SEQUENCE_NAME", valueColumnName = "SEQUENCE_VALUE", pkColumnValue = "CUSTOMER_SEQ")
@AssociationOverride(name = "address", joinColumns = @JoinColumn(name = "ADDRESS_ID_FK", referencedColumnName = "ADDRESS_ID_PK"))
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "CUSTOMER_TYPE", discriminatorType = DiscriminatorType.STRING, length = 1)
@DiscriminatorValue(value = "C")
@GroupSequence({Customer.class, Basic.class, Extended.class})

/*
@NamedStoredProcedureQueries({
  @NamedStoredProcedureQuery( name="GET_CUSTOMER_SP", 
    resultClass=Customer.class, 
    procedureName="CUSTOMER_PKG.GET_CUSTOMER_SP", 
    parameters={ 
      @StoredProcedureParameter(queryParameter="customerID",
    		name="CUSTOMER_ID", direction=Direction.IN), 
      @StoredProcedureParameter(queryParameter="CUSTOMER_CURSOR", 
    		name="CUSTOMER_CURSOR", direction=Direction.OUT_CURSOR)}),
  @NamedStoredProcedureQuery( name="GET_CUSTOMER_ORDERS", 
    resultSetMapping="customerOrderRSmapping", 
    procedureName="getCustomerAndOrders", 
    parameters={ 
      @StoredProcedureParameter(queryParameter="custID", name="CUSTOMER_ID",  direction=Direction.IN),
      @StoredProcedureParameter(queryParameter="avgPrice", name="yy", direction=Direction.OUT)
      }),
  @NamedStoredProcedureQuery( name="DELETE_CUSTOMER_SP", 
    resultClass=void.class, 
    procedureName="CUSTOMER_PKG.DELETE_CUSTOMER_SP", 
    parameters={ 
      @StoredProcedureParameter(queryParameter="customerID",
    	  name="CUSTOMER_ID", direction=Direction.IN) })
})
*/
public class Customer extends User {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CUST_SEQ_GEN")
	@Column(name = "CUSTOMER_ID_PK")
	protected int customerId;

	@Column(name = "CUSTOMER_TYPE", nullable = false)
	@Enumerated(EnumType.STRING)
	protected CustomerType customerType;

	// testing with a separate table for improving performance
	@OneToOne(mappedBy="customer", fetch=FetchType.LAZY, 
	  cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE} 
	) 
	protected CustomerPicture custPicture;

	@Valid
	@NotNull(groups={Basic.class, Extended.class})
	@Embedded
	@AttributeOverrides( {
			@AttributeOverride(name = "bankName", column = @Column(name = "BANK_NAME")),
			@AttributeOverride(name = "accountNumber", column = @Column(name = "ACCOUNT_NUMBER")),
			@AttributeOverride(name = "routingNumber", column = @Column(name = "ROUTING_NUMBER")) })
	protected BankInfo bank;

	@OneToMany(mappedBy = "customer")
	//@OneToMany(mappedBy = "customer", cascade = { CascadeType.PERSIST, CascadeType.MERGE})
	@MapKey(name = "orderId")
	protected Map<Integer, Order> orders; // inverse side of Order to Customer relationship

	// testing on element collection
	@ElementCollection
	@CollectionTable(name = "CUST_NICKNAME", joinColumns = @JoinColumn(name = "CUST_ID_FPK", referencedColumnName = "CUSTOMER_ID_PK"))
	protected Set<String> nickNames;

	// testing on element collection
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "CUST_BANK", joinColumns = @JoinColumn(name = "CUST_ID_FPK", referencedColumnName = "CUSTOMER_ID_PK"))
	protected List<BankInfo> bankInfos;
	
	private static final long serialVersionUID = 1L;

	public Customer() {
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

	public Map<Integer, Order> getOrders() {
		return this.orders;
	}

	public void setOrders(Map<Integer, Order> orders) {
		this.orders = orders;
	}

	public Set<String> getNickNames() {
		return nickNames;
	}

	public void setNickNames(Set<String> nickNames) {
		this.nickNames = nickNames;
	}

	public List<BankInfo> getBankInfos() {
		return bankInfos;
	}

	public void setBankInfos(List<BankInfo> bankInfos) {
		this.bankInfos = bankInfos;
	}
	
	public CustomerPicture getCustPicture() {
		return custPicture;
	}

	public void setCustPicture(CustomerPicture custPicture) {
		this.custPicture = custPicture;
	}

}
