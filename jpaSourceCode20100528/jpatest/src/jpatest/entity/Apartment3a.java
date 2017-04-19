package jpatest.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@IdClass(Apartment3PK.class)
@Table(name = "APARTMENT2")
public class Apartment3a extends BaseEntity {
	@Column(name = "APT_NUM_PK")
	@Id
	short aptNumber;    // match ApartmentPK.aptNumber

	@Id
	@ManyToOne
	@JoinColumns( {
			@JoinColumn(name = "STREET_FPK", referencedColumnName = "STREET_PK"),
			@JoinColumn(name = "ZIP_FPK", referencedColumnName = "ZIP_PK") })
	protected Address3 addrId; // match name of ApartmentPK.addrId

	@Column(name = "NUM_ROOMS")
	short rooms;

	private static final long serialVersionUID = 1L;

	public Apartment3a() {
		super();
	}

	public Apartment3a(short aprtNumber) {
		super();
		this.aptNumber = aprtNumber;
	}

	public Address3 getAddrId() {
		return addrId;
	}

	public void setAddrId(Address3 address) {
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
