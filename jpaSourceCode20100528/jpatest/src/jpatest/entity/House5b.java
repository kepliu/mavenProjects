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
public class House5b extends BaseEntity {

	@EmbeddedId
	Address2PK housePK; // @AttributeOverride not allowed here

	@MapsId
	@OneToOne
	@JoinColumns( {
			@JoinColumn(name = "STREET_FPK", referencedColumnName = "STREET_PK"),
			@JoinColumn(name = "ZIP_FPK", referencedColumnName = "ZIP_PK") })
	protected Address2 addr;

	@Column(name = "NUM_ROOMS")
	short rooms;

	private static final long serialVersionUID = 1L;

	public House5b() {
		super();
	}

	public Address2PK getHousePK() {
		return housePK;
	}

	public Address2 getAddr() {
		return addr;
	}

	public void setAddr(Address2 addr) {
		this.addr = addr;
	}

	public short getRooms() {
		return rooms;
	}

	public void setRooms(short rooms) {
		this.rooms = rooms;
	}
}
