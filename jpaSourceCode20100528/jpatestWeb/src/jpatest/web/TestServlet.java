package jpatest.web;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import jpatest.entity.Book;
import jpatest.entity.LineItem;
import jpatest.entity.Order;

@PersistenceContext(name = "jpaTestJtaPC", unitName = "jpaTestJtaPU")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Resource
	private UserTransaction ut;

	// This is not thread safe, just a test
	@PersistenceContext(unitName = "jpaTestJtaPU")
	EntityManager em;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TestServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		testPersistenceContextJndiLookup();
		testPersistenceContextInjection();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		testPersistenceContextJndiLookup();
		testPersistenceContextInjection();
	}

	protected void testPersistenceContextJndiLookup() {
		System.out
				.println("testing persistence context from servlet using jndi lookup");
		InitialContext ctx = null;
		EntityManager em = null;
		try {
			Order order = new Order();
			order.setOrderTime(new Timestamp(new Date().getTime()));

			Book book = new Book();
			book.setIsbn("isbn #");
			book.setTitle("book Title");

			LineItem li = new LineItem();
			li.setPrice(10.00);
			li.setQuantity(10);
			li.setBook(book); // necessary
			li.setOrder(order); // necessary

//			Map<Integer, LineItem> lineItems = new HashMap<Integer, LineItem>();
//			lineItems.put(1, li);
			List<LineItem> lineItems = new ArrayList<LineItem>();
			lineItems.add(li);
			order.setLineItem(lineItems);

			ctx = new InitialContext();
			// Alternatively, it can be looked up directly
			//UserTransaction ut = (UserTransaction)
			//ctx.lookup("java:comp/UserTransaction");
			ut.begin();
		  em = (EntityManager) ctx.lookup("java:comp/env/jpaTestJtaPC");
			em.persist(book);
			em.persist(order);
			ut.commit();

			// finally delete it
			ut.begin();
			em.remove(em.merge(order));
			em.remove(em.merge(book));
			ut.commit();

		} catch (NamingException e) {
			e.printStackTrace();
		} catch (NotSupportedException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (RollbackException e) {
			e.printStackTrace();
		} catch (HeuristicMixedException e) {
			e.printStackTrace();
		} catch (HeuristicRollbackException e) {
			e.printStackTrace();
		} finally {
			if (em != null && em.isOpen()) em.close();
		}

	}

	protected void testPersistenceContextInjection() {
		System.out
				.println("testing persistence context from servlet using dependency injeciton");

		// InitialContext ctx = null;
		try {

			Order order = new Order();
			order.setOrderTime(new Timestamp(new Date().getTime()));

			Book book = new Book();
			book.setIsbn("isbn #");
			book.setTitle("book Title");

			LineItem li = new LineItem();
			li.setPrice(10.00);
			li.setQuantity(10);
			li.setBook(book); // necessary
			li.setOrder(order); // necessary

//			Map<Integer, LineItem> lineItems = new HashMap<Integer, LineItem>();
//			lineItems.put(1, li);
			List<LineItem> lineItems = new ArrayList<LineItem>();
			lineItems.add(li);
			order.setLineItem(lineItems);

			// ctx = new InitialContext();
			// UserTransaction ut = (UserTransaction)
			// ctx.lookup("java:comp/UserTransaction");
			ut.begin();

			em.persist(book);
			em.persist(order);
			ut.commit();

			// finally delete it
			ut.begin();
			em.remove(em.merge(order));
			em.remove(em.merge(book));
			ut.commit();

		} catch (NotSupportedException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (RollbackException e) {
			e.printStackTrace();
		} catch (HeuristicMixedException e) {
			e.printStackTrace();
		} catch (HeuristicRollbackException e) {
			e.printStackTrace();
		}

	}

}
