package jpatest.ejb;
import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Local;

import jpatest.entity.Book;
import jpatest.entity.Order;

@Local
public interface OrderSFLocal {
	public void init(Order order, Book book) ;
	public void createOrder();
	public List<Order> retrieveOrders(Timestamp orderTime) ;
	public void deleteOrder();
}
