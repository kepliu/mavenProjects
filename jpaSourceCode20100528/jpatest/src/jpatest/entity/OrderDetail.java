package jpatest.entity;

// for testing constructor expression in SELECT clauses
public class OrderDetail {
	
	public String customer;
	public double price;

	public OrderDetail(String customerNmae, double price) {
		this.customer = customerNmae;
		this.price = price;
	}

}