package jpatest.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "HOUSE2")
public class House6a extends BaseEntity {

	@EmbeddedId
	Address3PK pk; // @AttributeOverride not allowed here

	@MapsId
	@OneToOne
	@JoinColumns( {
			@JoinColumn(name = "STREET_FPK", referencedColumnName = "STREET_PK"),
			@JoinColumn(name = "ZIP_FPK", referencedColumnName = "ZIP_PK") })
	protected Address3 address;

	@Column(name = "NUM_ROOMS")
	short rooms;

	private static final long serialVersionUID = 1L;

	public House6a() {
		super();
	}

	public Address3 getAddr() {
		return address;
	}

	public void setAddr(Address3 addr) {
		this.address = addr;
	}

	public short getRooms() {
		return rooms;
	}

	public void setRooms(short rooms) {
		this.rooms = rooms;
	}
}
