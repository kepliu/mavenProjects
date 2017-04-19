package jpatest.entity;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import jpatest.groups.Basic;
import jpatest.groups.Extended;  

@Entity
@Table (name="ADDRESS")
@TableGenerator(name = "ADDRESS_SEQ_GEN", table = "SEQUENCE_GENERATOR_TB", pkColumnName = "SEQUENCE_NAME", valueColumnName = "SEQUENCE_VALUE", pkColumnValue = "ADDRESS_SEQ")
public class Address extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ADDRESS_SEQ_GEN")
	@Column(name = "ADDRESS_ID_PK")
	protected int addressId;
	
	@NotNull(groups=Basic.class)
	protected String street;

	@NotNull(groups={Basic.class, Extended.class})
	protected String city;

	@NotNull
	@Size(max=2, min=2, groups=Extended.class)
	protected String state;

	@Pattern(regexp="^\\d{5}(-\\d{4})?$", message="Zip not in correct format")
	@Column(name = "ZIP", length=5, nullable=false)
	protected String zip;
	
    @Transient
    protected boolean isResidential;    // this field is not persisted to database
  
    @OneToOne(mappedBy="address")
    protected Customer customer;        // an imaginable bidirectional relationship

    private static final long serialVersionUID = 1L;
    
	public Address() {
		super();
	}

	public int getAddressId() {
		return this.addressId;
	}

	@Access(AccessType.PROPERTY)    
	@Column(name = "STREET", updatable=true, insertable=true,  nullable=false)
	public String getStreet() {
		return this.street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return this.zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
}
