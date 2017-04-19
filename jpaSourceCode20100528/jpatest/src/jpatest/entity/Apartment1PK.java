package jpatest.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Apartment1PK implements Serializable {
	short aptNumber;
	int addrId;          // match PK type of parent

	private static final long serialVersionUID = 1L;
	
	public Apartment1PK() {}
	
	public Apartment1PK(short aptNumber, int addrId){
		this.aptNumber = aptNumber;
		this.addrId = addrId;
	}

	public short getAptNumber() {
		return aptNumber;
	}

	public int getAddrId() {
		return addrId;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Apartment1PK)) {
			return false;
		}
		Apartment1PK spk = (Apartment1PK) obj;
		return this.aptNumber == spk.aptNumber && this.addrId == spk.addrId;

	}

	public int hashCode() {
		return this.aptNumber ^ this.addrId;
	}
}
