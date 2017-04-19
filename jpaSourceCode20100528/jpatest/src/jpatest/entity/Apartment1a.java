package jpatest.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@IdClass(Apartment1PK.class)
@Table(name = "APARTMENT")
public class Apartment1a extends BaseEntity {

	@Id
	@Column(name="APT_NUM_PK")
	short aptNumber; // match name & type of ApartmentPK.aptNumber

	@Id
	@ManyToOne
	@JoinColumn(name = "ADDRESS_ID_FPK", referencedColumnName = "ADDRESS_ID_PK")
	protected Address addrId; // match name of ApartmentPK.addrId
	
	@Column(name="NUM_ROOMS")
	short rooms;

	private static final long serialVersionUID = 1L;

	public Apartment1a() {
		super();
	}
	
	public Apartment1a(short aprtNumber) {
		super();
		this.aptNumber = aprtNumber;
	}

	public Address getAddrId() {
		return addrId;
	}

	public void setAddrId(Address address) {
		this.addrId = address;
	}

	public short getAptNumber() {
		return aptNumber;
	}

	public short getRooms() {
		return rooms;
	}

	public void setRooms(short rooms) {
		this.rooms = rooms;
	}
}
