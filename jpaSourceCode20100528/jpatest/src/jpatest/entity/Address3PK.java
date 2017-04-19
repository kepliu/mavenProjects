package jpatest.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Address3PK implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(name = "STREET_PK", updatable=false, insertable=true,  nullable=false)
	private String street;     
	
	@Column(name = "ZIP_PK", updatable=false, insertable=true,  nullable=false)
	private String zip;
	
    public Address3PK() {  }
	
	public Address3PK(String street, String zip) {
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
		if (!(obj instanceof Address3PK)) {
			return false;
		}
		Address3PK spk = (Address3PK) obj;
		return this.street.equals(spk.street) && this.zip.equals(spk.zip);

	}

	public int hashCode() {
		return this.street.hashCode() ^ this.zip.hashCode();
	}
}
