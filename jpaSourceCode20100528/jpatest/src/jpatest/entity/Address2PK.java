package jpatest.entity;

import java.io.Serializable;

public class Address2PK implements Serializable {
	private static final long serialVersionUID = 1L;

	private String street;
	private String zip;

	public Address2PK() {  }
	
	public Address2PK(String street, String zip) {
		this.street = street;
		this.zip = zip;
	}
	
	public String getStreet() {
		return street;
	}
	
	public String getZip() {
		return zip;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Address2PK)) {
			return false;
		}
		Address2PK spk = (Address2PK) obj;
		return this.street.equals(spk.street) && this.zip.equals(spk.zip);

	}

	public int hashCode() {
		return this.street.hashCode() ^ this.zip.hashCode();
	}
}
