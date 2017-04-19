package jpatest.ejb.eao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Cache;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import jpatest.ejb.util.MyOptimisticLockException;
import jpatest.entity.Book;
import jpatest.entity.LineItem;
import jpatest.entity.Order;

/**
 * An example EAO with container-managed entity manager and JTA transaction.
 */
@Stateless(mappedName = "OrderEAO")
// @Interceptors({jpatest.ejb.util.RequestContextInterceptor.class})
public class OrderEAO implements OrderEAOLocal {
	@PersistenceContext(unitName = "jpaTestJtaPU")
	EntityManager em;

	// @Resource
	// EJBContext context;

	public void createOrder(Order order) {
		/*
		 * Principal p = context.getCallerPrincipal(); String user = "ejbuser";
		 * if (p != null) { user = p.getName(); RequestContext rc =
		 * RequestContext.getLocalInstance(); rc.initRequestContext(user); }
		 * 
		 * System.out.println("User from ejb:" + user);
		 */

		em.persist(order);
	}

	public void addLineItem(Order order, LineItem li) {
		em.merge(order);
		order.getLineItems().add(li);
	}

	public void addLineItem(int orderId, int quantity, double price) {
		Order order = em.find(Order.class, orderId);
		em.lock(order, LockModeType.OPTIMISTIC_FORCE_INCREMENT);

		LineItem li = new LineItem();
		li.setQuantity(quantity);
		li.setPrice(price);
		order.getLineItems().add(li);
		li.setOrder(order);		
	}
	
	public void test(int orderId){
		Order order = em.find(Order.class, orderId);
		
		em.setProperty("javax.persistence.cache.retrieveMode", CacheRetrieveMode.USE);
		em.setProperty("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
		em.setProperty("javax.persistence.cache.storeMode", CacheStoreMode.USE);
		
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH);
		em.refresh(order, p);
		
		EntityManagerFactory emf = em.getEntityManagerFactory();
		Cache secondLevelCache = emf.getCache();
		boolean b = secondLevelCache.contains(Order.class, 12345); // Is the object cached?
		secondLevelCache.evict(Book.class);          // evict all cached objects of Book entity
		secondLevelCache.evict(Book.class, 12345);   // evict a specific object
		secondLevelCache.evictAll();	
		
		Query q = em.createQuery("SELECT o FROM Order o ");
		q.setHint("javax.persistence.cache.storeMode",  CacheStoreMode.REFRESH);
		q.setHint("javax.persistence.cache.retrieveMode",  CacheStoreMode.BYPASS);
		List result = q.getResultList();	
	}
	
	public void discountOrder(int orderId, double percentage) {
		Order order = em.find(Order.class, orderId);
		double price = order.getPrice();
		price *= percentage;
		order.setPrice(price);
		
		try {
			em.flush();
		} catch(OptimisticLockException e) {
			throw new MyOptimisticLockException(e);
		}
	}

	public void performDiscount(int orderId, double percentage) {
		Order order = em.find(Order.class, orderId);
		//em.refresh(order, LockModeType.PESSIMISTIC_WRITE);
		List<LineItem> items = order.getLineItems();
		double price = 0;
		for (LineItem i : items) {
			price += i.getPrice();
		}
		price *= percentage;
		order.setPrice(price);
	}

	public void deleteOrder(Order order) {
		em.remove(em.merge(order));
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Order queryOrderWithMaxId() {
		Query query = em.createQuery("SELECT o FROM Order o "
				+ "WHERE o.orderId = (SELECT MAX(o2.orderId) FROM Order o2)");
		Order order = (Order) query.getSingleResult();
		return order;
	}
}
