package jpatest.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Apartment2PK implements Serializable {

	short aptNumber;
	Address2PK addrId;        // match PK type of parent

	private static final long serialVersionUID = 1L;

	public Apartment2PK() {
	}
	
	public Apartment2PK(short aptNumber) {
		this.aptNumber = aptNumber;
	}

	public Apartment2PK(short aptNumber, Address2PK addrId) {
		this.aptNumber = aptNumber;
		this.addrId = addrId;
	}

	public short getAptNumber() {
		return aptNumber;
	}

	public Address2PK getAddrId() {
		return addrId;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Apartment2PK)) {
			return false;
		}
		Apartment2PK spk = (Apartment2PK) obj;
		return this.aptNumber == spk.aptNumber
				&& this.addrId.equals(spk.addrId);

	}

	public int hashCode() {
		return this.aptNumber ^ this.addrId.hashCode();
	}
}
