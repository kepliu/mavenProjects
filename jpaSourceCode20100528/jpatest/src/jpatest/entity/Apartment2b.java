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
public class Apartment2b extends BaseEntity {

	@EmbeddedId
	@AttributeOverride(name = "aptNumber", column = @Column(name = "APT_NUM_PK"))
	protected Apartment2PK aptId;

	@MapsId("addrId")      // addrId is a field of ApartmentPK
	@ManyToOne             // that matches PK type of parent
	  @JoinColumns({
		     @JoinColumn(name="STREET_FPK", referencedColumnName="STREET_PK"),
		     @JoinColumn(name="ZIP_FPK", referencedColumnName="ZIP_PK") 
		  })                  
	protected Address2 address;

	@Column(name = "NUM_ROOMS")
	short rooms;

	private static final long serialVersionUID = 1L;

	public Apartment2b() {
		super();
	}

	public Apartment2b(short aprtNumber) {
		super();
		aptId = new Apartment2PK(aprtNumber);
	}

	public Address2 getAddress() {
		return address;
	}

	public void setAddress(Address2 address) {
		this.address = address;
	}

	public Apartment2PK getAptId() {
		return aptId;
	}

	public short getRooms() {
		return rooms;
	}

	public void setRooms(short rooms) {
		this.rooms = rooms;
	}
}
