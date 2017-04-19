package jpatest.ejb.eao;

import javax.ejb.Local;

import jpatest.entity.Order;

@Local
public interface OrderEAOLocal {
	public void createOrder(Order order);

	public void deleteOrder(Order order);

	public Order queryOrderWithMaxId();
}
