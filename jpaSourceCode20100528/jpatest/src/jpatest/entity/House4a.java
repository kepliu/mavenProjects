package jpatest.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "HOUSE")
public class House4a extends BaseEntity {
	@Id
	@OneToOne
	@JoinColumn(name = "ADDRESS_ID_FPK", referencedColumnName = "ADDRESS_ID_PK")
	protected Address addr;

	@Column(name = "NUM_ROOMS")
	short rooms;

	private static final long serialVersionUID = 1L;

	public House4a() {
		super();
	}

	public Address getAddr() {
		return addr;
	}

	public void setAddr(Address addr) {
		this.addr = addr;
	}

	public short getRooms() {
		return rooms;
	}

	public void setRooms(short rooms) {
		this.rooms = rooms;
	}
}
