package jpatest.ejb;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Local;

import jpatest.entity.Book;
import jpatest.entity.LineItem;
import jpatest.entity.Order;

@Local
public interface OrderSLLocal {

	public void createOrder(Order v,  Book book);
	public List<Order> retrieveOrders(Timestamp orderTime);	
	public void addLineItem(Order order, LineItem li);
	public Order addLineItem(int orderID, LineItem li);
	public void deleteOrder(Order o, Book book);	
	public void createBookAndOrder(double price);

}
