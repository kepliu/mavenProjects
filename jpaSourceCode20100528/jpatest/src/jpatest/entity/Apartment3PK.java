package jpatest.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Apartment3PK implements Serializable {
	short aptNumber;
	
	Address3PK addrId;   // match PK type of parent

	private static final long serialVersionUID = 1L;

	public Apartment3PK() {
	}
	
	public Apartment3PK(short aptNumber) {
		this.aptNumber = aptNumber;
	}

	public Apartment3PK(short aptNumber, Address3PK addrId) {
		this.aptNumber = aptNumber;
		this.addrId = addrId;
	}

	public short getAptNumber() {
		return aptNumber;
	}

	public Address3PK getAddrId() {
		return addrId;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Apartment3PK)) {
			return false;
		}
		Apartment3PK spk = (Apartment3PK) obj;
		return this.aptNumber == spk.aptNumber
				&& this.addrId.equals(spk.addrId);

	}

	public int hashCode() {
		return this.aptNumber ^ this.addrId.hashCode();
	}
}
