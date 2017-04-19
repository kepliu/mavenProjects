package jpatest.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name = "APARTMENT2")
public class Apartment3b extends BaseEntity {

	@EmbeddedId
	@AttributeOverride(name = "aptNumber", column = @Column(name = "APT_NUM_PK"))
	protected Apartment3PK aptId;

	@MapsId("addrId")      // addrId is a field of ApartmentPK
	@ManyToOne             // that matches PK type of parent
	  @JoinColumns({
		     @JoinColumn(name="STREET_FPK", referencedColumnName="STREET_PK"),
		     @JoinColumn(name="ZIP_FPK", referencedColumnName="ZIP_PK") 
		  })                  
	protected Address3 address;

	@Column(name = "NUM_ROOMS")
	short rooms;

	private static final long serialVersionUID = 1L;

	public Apartment3b() {
		super();
	}

	public Apartment3b(short aprtNumber) {
		super();
		aptId = new Apartment3PK(aprtNumber);
	}

	public Address3 getAddress() {
		return address;
	}

	public void setAddress(Address3 address) {
		this.address = address;
	}

	public Apartment3PK getAptId() {
		return aptId;
	}

	public short getRooms() {
		return rooms;
	}

	public void setRooms(short rooms) {
		this.rooms = rooms;
	}
}
