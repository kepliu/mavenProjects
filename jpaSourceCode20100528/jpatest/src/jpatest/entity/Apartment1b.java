package jpatest.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name = "APARTMENT")
public class Apartment1b extends BaseEntity {

	@EmbeddedId
	@AttributeOverride(name = "aptNumber", column = @Column(name = "APT_NUM_PK"))
	protected Apartment1PK aptId;

	@MapsId("addrId")      // addrId is a field of ApartmentPK
	@ManyToOne             // that matches PK type of parent
	@JoinColumn(name = "ADDRESS_ID_FPK", referencedColumnName = "ADDRESS_ID_PK")
	protected Address address;

	@Column(name = "NUM_ROOMS")
	short rooms;

	private static final long serialVersionUID = 1L;

	public Apartment1b() {
		super();
	}

	public Apartment1b(short aprtNumber) {
		super();
		aptId = new Apartment1PK(aprtNumber, 0);
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Apartment1PK getAptId() {
		return aptId;
	}

	public short getRooms() {
		return rooms;
	}

	public void setRooms(short rooms) {
		this.rooms = rooms;
	}
}
