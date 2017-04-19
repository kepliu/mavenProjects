package jpatest.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "HOUSE2")
@IdClass(Address2PK.class)
public class House5a extends BaseEntity {

	@Id
	@OneToOne
	@JoinColumns( {
			@JoinColumn(name = "STREET_FPK", referencedColumnName = "STREET_PK"),
			@JoinColumn(name = "ZIP_FPK", referencedColumnName = "ZIP_PK") })
	protected Address2 address;

	@Column(name = "NUM_ROOMS")
	short rooms;

	private static final long serialVersionUID = 1L;

	public House5a() {
		super();
	}

	public Address2 getAddr() {
		return address;
	}

	public void setAddr(Address2 addr) {
		this.address = addr;
	}

	public short getRooms() {
		return rooms;
	}

	public void setRooms(short rooms) {
		this.rooms = rooms;
	}
}
