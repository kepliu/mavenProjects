package jpatest.web;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jpatest.entity.LineItem;
import jpatest.entity.Order;
import jpatest.web.eao.OrderEAO;
import jpatest.web.eao.OrderEAO2;

/**
 * URL http://localhost:8080/jpatestWeb/orderservlet
 */

@PersistenceContext(name = "jpaTestJtaPC", unitName = "jpaTestJtaPU")
@PersistenceUnit(name = "jpaTestPUnit", unitName = "jpaTestPU")
public class OrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
		
	final int loops = 100;
	
	public OrderServlet() {
		super();
	}
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		testOrderEAO();
		testOrderEAO2();
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		testOrderEAO();
		testOrderEAO2();
	}

	protected void testOrderEAO() {
		System.out.println("testing OrderEAO from order servlet");
		try {
			Order order = getOrderAndLineItems(1.5);
	
			OrderEAO orderEao = new OrderEAO();
			
			// timing it for efficiency comparison to OrderEAR2
			long time = System.currentTimeMillis();
			for (int i = 0; i < loops; i++) {
			  orderEao.createOrder(order);
			  Order o = orderEao.queryOrderWithMaxId();
			  orderEao.deleteOrder(order);
			}
			long time2 = System.currentTimeMillis();
			long totalTime = (time2-time)/100;
			System.out.println("Order EAO's in seconds: " + totalTime);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	protected void testOrderEAO2() {
		System.out.println("testing OrderEAO2 from order servlet");
		try {
			Order order = getOrderAndLineItems(2.5);
		
			OrderEAO2 orderEao = new OrderEAO2();
			
			// timing it for efficiency comparison to OrderEAR2
			long time = System.currentTimeMillis();
			for (int i = 0; i < loops; i++) {
			  orderEao.createOrder(order);
			  Order o = orderEao.queryOrderWithMaxId();
			  orderEao.deleteOrder(order);
			}
			
			long time2 = System.currentTimeMillis();
			long totalTime = (time2-time)/100;
			System.out.println("Order EAO2's in seconds: " + totalTime);
		} catch (Throwable t) {
			t.printStackTrace();
		}	
	}
	
	private Order getOrderAndLineItems(double price) {
		Order order = new Order();
		order.setOrderTime(new Timestamp(new Date().getTime()));
		order.setPrice(price);

		LineItem li = new LineItem();
		li.setPrice(10.00);
		li.setQuantity(10);
		li.setBook(null);
		li.setOrder(order);

		List<LineItem> lineItems = new ArrayList<LineItem>();
		lineItems.add(li);
		order.setLineItem(lineItems);

		return order;
	}
}
