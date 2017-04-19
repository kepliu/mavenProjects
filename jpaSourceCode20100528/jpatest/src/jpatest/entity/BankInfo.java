package jpatest.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BankInfo {

	@Column(name = "BANK_NAME_X")
	private String bankName;

	@Column(name = "BANK_ACCOUNT_NUMBER")
	private String accountNumber;

	@Column(name = "BANK_ROUTING_NUMBER")
	private String routingNumber;

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccountNumber() {
		return this.accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBankRoutingNumber() {
		return this.routingNumber;
	}

	public void setRoutingNumber(String routingNumber) {
		this.routingNumber = routingNumber;
	}

}
