package jpatest.test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import jpatest.entity.LineItem_;
import jpatest.entity.OrderDetail;
import jpatest.entity.OrderStatus;

import org.junit.Test;

import jpatest.entity.Address;
import jpatest.entity.Address_;
import jpatest.entity.BankInfo_;
import jpatest.entity.Book;
import jpatest.entity.Category;
import jpatest.entity.Category_;
import jpatest.entity.Customer;
import jpatest.entity.CustomerType;
import jpatest.entity.Customer_;
import jpatest.entity.GoldCustomer;
import jpatest.entity.LineItem;
import jpatest.entity.Order;
import jpatest.entity.Order_;

public class CriteriaQueryTest extends MyJpaTestCase {
	@Test
	public void testCriteriaQuries() {
		// create some objects first
		QueryTest.createEntities(em);
		Object[] created = OrderTest.createOrder(em);
		Order[] orderCreated = (Order[]) created[0];
		Customer[] custCreated = (Customer[]) created[1];
		List<Book> booksCreated = (List<Book>) created[2];

		// do the test
		queryRoots();
		joinQueries();
		pathNavigation();
		selectClause();
		whereClause();

		MiscellaneousTest mt = new MiscellaneousTest();
		Customer c = mt.elementCollectionCreate();
		literals();
		mt.elementCollectionDelete(c);

		parameterExpression();
		expressions();
		functionExpression();
		subquery();
		groupByHavingClause();
		orderByClause();
		criteriaQueriesUsingMetamodel();
		weakerTypedCriteriaQueries();
		queryModification();

		OrderTest.deleteOrder(em, orderCreated, custCreated, booksCreated);
		
		// delete some objects
		QueryTest.runBulkOperations(em);
	}

	protected void queryRoots() {
		System.out.println("testing  queryRoots !!!");
		try {
			{ // joins with two entities
				Query q = em
						.createQuery("SELECT DISTINCT a FROM Address a, Customer c WHERE a = c.address");
				List<Address> resultList = q.getResultList();
				for (Address o : resultList) {
					System.out.println("queryRoots jpql  address: "
							+ o.getZip());
				}

			}
			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Address> cq = cb.createQuery(Address.class);
				Root<Address> a = cq.from(Address.class);
				Root<Customer> c = cq.from(Customer.class);
				cq.select(a).distinct(true);
				cq.where(cb.equal(a, c.get(Customer_.address)));

				TypedQuery<Address> q = em.createQuery(cq);
				List<Address> resultList = q.getResultList();
				for (Address o : resultList) {
					System.out.println("queryRoots cq address: " + o.getZip());
				}
			}

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	protected void joinQueries() {
		System.out.println("testing  joinQueries !!!");
		try {
			{ // joins with two entities
				Query q = em
						.createQuery("SELECT o FROM Order o JOIN o.lineItems i");
				List resultList = q.getResultList();
				for (Object o : resultList) {
					System.out.println("joinQueries jpql o: "
							+ ((Order) o).getOrderId());
				}

			}
			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Order> cq = cb.createQuery(Order.class);
				Root<Order> order = cq.from(Order.class);
				Join<Order, LineItem> item = order.join(Order_.lineItems);
				cq.select(order);

				TypedQuery<Order> q = em.createQuery(cq);
				List<Order> resultList = q.getResultList();
				for (Order o : resultList) {
					System.out.println("joinQueries o: " + o.getOrderId());
				}
			}

			{ // joins with three entities
				Query q = em
						.createQuery("SELECT o FROM Order o JOIN o.lineItems i JOIN i.book b");
				List resultList = q.getResultList();
				for (Object o : resultList) {
					System.out.println("joinQueries 3 jpql o: "
							+ ((Order) o).getOrderId());
				}

			}
			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Order> cq = cb.createQuery(Order.class);
				Root<Order> order = cq.from(Order.class);
				Join<Order, LineItem> item = order.join(Order_.lineItems);
				Join<LineItem, Book> book = item.join(LineItem_.book);
				cq.select(order);

				TypedQuery<Order> q = em.createQuery(cq);
				List<Order> resultList = q.getResultList();
				for (Order o : resultList) {
					System.out.println("joinQueries 3 o: " + o.getOrderId());
				}
			}
			{ // joins can be chained
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Order> cq = cb.createQuery(Order.class);
				Root<Order> order = cq.from(Order.class);
				Join<LineItem, Book> book = order.join(Order_.lineItems).join(
						LineItem_.book);
				cq.select(order);

				TypedQuery<Order> q = em.createQuery(cq);
				List<Order> resultList = q.getResultList();
				for (Order o : resultList) {
					System.out.println("joinQueries 3 chained o: "
							+ o.getOrderId());
				}
			}

			{ // outer joins with two entities with where clause
				Query q = em
						.createQuery("SELECT o FROM Order o LEFT OUTER JOIN o.lineItems i WHERE i.price > 5");
				List resultList = q.getResultList();
				for (Object o : resultList) {
					System.out
							.println("joinQueries outer jpql  with where clause o: "
									+ ((Order) o).getOrderId());
				}

			}
			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Order> cq = cb.createQuery(Order.class);
				Root<Order> o = cq.from(Order.class);
				Join<Order, LineItem> i = o.join(Order_.lineItems,
						JoinType.LEFT);
				cq.select(o);

				Predicate condition = cb.gt(i.get(LineItem_.price), 5);
				cq.where(condition);

				TypedQuery<Order> q = em.createQuery(cq);
				List<Order> resultList = q.getResultList();
				for (Order order : resultList) {
					System.out
							.println("joinQueries outer with where clause o: "
									+ order.getOrderId());
				}
			}
			
			{  // Fetch joins with two entities with a parameter
				Query query = em
						.createQuery("SELECT c FROM Customer c LEFT JOIN FETCH c.orders WHERE c.customerType = ?1");
				query.setParameter(1, CustomerType.G);
				List<Customer> resultList = query.getResultList();
				for (Customer result : resultList) {
					System.out.println("customer with left fetch join order: "
							+ result.getName());
				}
			}
			
			{  
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
				Root<Customer> customer = cq.from(Customer.class);
				customer.fetch(Customer_.orders, JoinType.LEFT);
				cq.select(customer);

				Predicate condition = cb.equal(customer.get(Customer_.customerType),
						CustomerType.G);
				cq.where(condition);

				TypedQuery<Customer> q = em.createQuery(cq);

				List<Customer> resultList = q.getResultList();
				for (Customer c : resultList) {
					System.out
							.println("fetch join with where clause customer: "
									+ c.getName());
				}
			}
            /*
			{ // Fetch joins with two entities with a parameter
				Query q = em.createQuery(
				 "SELECT o FROM Order o LEFT OUTER JOIN FETCH o.lineItems WHERE o.orderTime < :today");
			     // "SELECT o FROM Order o LEFT OUTER JOIN o.lineItems i  WHERE o.orderTime < :today");
				q.setParameter("today", new Timestamp(new Date().getTime()));
				
				List<?> resultList = q.getResultList();  
				for (Object o : resultList) {
					System.out
							.println("joinQueries fetch join jpql  with where clause o: "
									+  ((Order)o).getOrderId());
				}

			}
			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Order> cq = cb.createQuery(Order.class);
				Root<Order> order = cq.from(Order.class);
				order.fetch(Order_.lineItems, JoinType.LEFT);
				cq.select(order);

				ParameterExpression<Timestamp> today = cb
						.parameter(Timestamp.class);
				Predicate condition = cb.lessThan(order.get(Order_.orderTime),
						today);
				cq.where(condition);

				TypedQuery<Order> q = em.createQuery(cq);
				q.setParameter(today, new Timestamp(new Date().getTime()));

				List<Order> resultList = q.getResultList();
				for (Order o : resultList) {
					System.out
							.println("joinQueries fetch join with where clause o: "
									+ o.getOrderId());
				}
			}
			*/
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	protected void pathNavigation() {
		System.out.println("testing  pathNavigation !!!");
		try {

			{ // path navigation
				Query q = em
						.createQuery("SELECT c.name FROM Customer c JOIN c.orders o WHERE c.bank.bankName LIKE '%Bank%'");
				List resultList = q.getResultList();
				for (Object o : resultList) {
					System.out.println("pathNavigation jpql customer: " + o);
				}

			}
			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<String> cq = cb.createQuery(String.class);
				Root<Customer> cust = cq.from(Customer.class);
				Join<Customer, Order> item = cust.join(Customer_.orders);
				Path<String> path = cust.get(Customer_.bank).get(
						BankInfo_.bankName);
				cq.where(cb.like(path, "%Bank%"));
				cq.select(cust.get(Customer_.name));

				TypedQuery<String> q = em.createQuery(cq);
				List<String> resultList = q.getResultList();
				for (String o : resultList) {
					System.out.println("pathNavigation cq customer: " + o);
				}
			}

			{ // path navigation with a map attribute
				Query q = em
						.createQuery("SELECT c.name, o.price FROM Customer c JOIN c.orders o WHERE KEY(o) > 2");
				List resultList = q.getResultList();
				for (Object o : resultList) {
					Object[] c = (Object[]) o;
					System.out.println("pathNavigation 2jpql customer: " + c[0]
							+ " price: " + c[1]);
				}

			}
			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
				Root<Customer> c = cq.from(Customer.class);
				MapJoin<Customer, Integer, Order> o = c.join(Customer_.orders);

				cq.where(cb.gt(o.key(), 2));
				cq.multiselect(c.get(Customer_.name), o.get(Order_.price));

				TypedQuery<Tuple> q = em.createQuery(cq);
				List<Tuple> resultList = q.getResultList();
				for (Tuple tuple : resultList) {
					String custName = tuple.get(0, String.class);
					Double price = tuple.get(1, Double.class);
					System.out.println("pathNavigation 2 customer: " + custName
							+ " price: " + price);
				}
			}

			{ // path navigation with path variable re-used
				Query q = em
						.createQuery("SELECT AVG(i.price) FROM LineItem i WHERE i.price > 2.0 AND i.price < 200.0");
				List<Double> resultList = q.getResultList();
				for (Double o : resultList) {
					System.out.println("pathNavigation jpql path2: " + o);
				}

			}
			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Double> cq = cb.createQuery(Double.class);
				Root<LineItem> i = cq.from(LineItem.class);

				Path<Double> price = i.get(LineItem_.price);
				cq.select(cb.avg(price));

				cq.where(cb.and(cb.gt(price, 2.0), cb.lt(price, 200.0)));

				TypedQuery<Double> q = em.createQuery(cq);
				Double result = q.getSingleResult();
				System.out.println("pathNavigation cq path2: " + result);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	protected void selectClause() {
		System.out.println("\ntesting  selectClause !!!");
		try {
			{ // tupe for multiple selection items
				Query q = em
						.createQuery("SELECT c.name, o.price FROM Customer c JOIN c.orders o WHERE o.orderTime < CURRENT_TIMESTAMP");
				List<Object[]> resultList = q.getResultList();
				for (Object[] o : resultList) {
					String bankName = (String) o[0];
					Double price = (Double) o[1];
					System.out.println("selectClause jpql tuple bank: "
							+ bankName + ":" + price);
				}

			}
			{ // use multiselect method
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
				Root<Customer> c = cq.from(Customer.class);
				MapJoin<Customer, Integer, Order> o = c.join(Customer_.orders);
				cq.multiselect(c.get(Customer_.name), o.get(Order_.price));
				cq.where(cb.lessThan(o.get(Order_.orderTime), cb
						.currentTimestamp()));

				TypedQuery<Tuple> q = em.createQuery(cq);
				List<Tuple> resultList = q.getResultList();

				for (Tuple result : resultList) {
					String bankName = result.get(0, String.class);
					Double price = result.get(1, Double.class);
					System.out.println("selectClause cq tuple bank: "
							+ bankName + ":" + price);
				}
			}

			{ // use select method
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
				Root<Customer> c = cq.from(Customer.class);
				MapJoin<Customer, Integer, Order> o = c.join(Customer_.orders);
				cq.select(cb.tuple(c.get(Customer_.name), o.get(Order_.price)));
				cq.where(cb.lessThan(o.get(Order_.orderTime), cb
						.currentTimestamp()));

				TypedQuery<Tuple> q = em.createQuery(cq);
				List<Tuple> resultList = q.getResultList();

				for (Tuple result : resultList) {
					String bankName = result.get(0, String.class);
					Double price = result.get(1, Double.class);
					System.out.println("selectClause cq 2 tuple bank: "
							+ bankName + ":" + price);
				}
			}

			{ // use multiselect method and Object[]
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
				Root<Customer> c = cq.from(Customer.class);
				MapJoin<Customer, Integer, Order> o = c.join(Customer_.orders);
				cq.multiselect(c.get(Customer_.name), o.get(Order_.price));
				cq.where(cb.lessThan(o.get(Order_.orderTime), cb
						.currentTimestamp()));

				TypedQuery<Object[]> q = em.createQuery(cq);
				List<Object[]> resultList = q.getResultList();

				for (Object[] result : resultList) {
					String bankName = (String) result[0];
					Double price = (Double) result[1];
					System.out.println("selectClause cq tuple custname: "
							+ bankName + ":" + price);
				}
			}

			{ // use select method and Object[]
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
				Root<Customer> c = cq.from(Customer.class);
				MapJoin<Customer, Integer, Order> o = c.join(Customer_.orders);
				cq.select(cb.array(c.get(Customer_.name), o.get(Order_.price)));
				cq.where(cb.lessThan(o.get(Order_.orderTime), cb
						.currentTimestamp()));

				TypedQuery<Object[]> q = em.createQuery(cq);
				List<Object[]> resultList = q.getResultList();

				for (Object[] result : resultList) {
					String bankName = (String) result[0];
					Double price = (Double) result[1];
					System.out.println("selectClause cq 2 tuple custname : "
							+ bankName + ":" + price);
				}
			}
			{ // constructor expression
				Query query = em
						.createQuery("SELECT NEW jpatest.entity.OrderDetail(o.customer.name, o.price) FROM Order o WHERE o.orderTime < ?1");
				query.setParameter(1, new Date(), TemporalType.TIMESTAMP);
				List<OrderDetail> resultList = query.getResultList();
				for (OrderDetail od : resultList) {
					System.out.println("selectClause JPQL constructor exp: "
							+ od.customer);
				}
			}
			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<OrderDetail> cq = cb
						.createQuery(OrderDetail.class);
				Root<Order> o = cq.from(Order.class);
				cq.multiselect(o.get(Order_.customer).get(Customer_.name), o
						.get(Order_.price));
				cq.where(cb.lessThan(o.get(Order_.orderTime), cb
						.currentTimestamp()));

				TypedQuery<OrderDetail> q = em.createQuery(cq);
				List<OrderDetail> resultList = q.getResultList();

				for (OrderDetail od : resultList) {
					System.out.println("selectClause cq  constructor exp: "
							+ od.customer);
				}
			}

			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<OrderDetail> cq = cb
						.createQuery(OrderDetail.class);
				Root<Order> o = cq.from(Order.class);
				cq.select(cb.construct(OrderDetail.class, o
						.get(Order_.customer).get(Customer_.name), o
						.get(Order_.price)));
				cq.where(cb.lessThan(o.get(Order_.orderTime), cb
						.currentTimestamp()));

				TypedQuery<OrderDetail> q = em.createQuery(cq);
				List<OrderDetail> resultList = q.getResultList();

				for (OrderDetail od : resultList) {
					System.out.println("selectClause cq  constructor exp: "
							+ od.customer);
				}
			}

			{ // aliasing in select clause
				Query q = em
						.createQuery("SELECT o.price AS price, i.quantity AS q FROM Order o JOIN o.lineItems i WHERE SIZE(o.lineItems) < 5 ");
				List<Object[]> resultList = q.getResultList();
				for (Object[] o : resultList) {
					Double price = (Double) o[0];
					Integer quantity = (Integer) o[1];
					System.out.println("selectClause jpql oi: " + price + ":"
							+ quantity);
				}
			}
			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
				Root<Order> o = cq.from(Order.class);
				Join<Order, LineItem> i = o.join(Order_.lineItems);

				cq.multiselect(o.get(Order_.price).alias("price"), i.get(
						LineItem_.quantity).alias("q"));

				cq.where(cb.lessThan(cb.size(o.get(Order_.lineItems)), 5));

				TypedQuery<Tuple> query = em.createQuery(cq);
				List<Tuple> resultList = query.getResultList();

				for (Tuple result : resultList) {
					Double price = result.get("price", Double.class);
					Integer quantity = result.get("q", Integer.class);
					System.out.println("selectClause cq  oi: " + +price + ":"
							+ quantity);
				}
			}

		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	protected void whereClause() {
		System.out.println("testing  whereClause !!!");
		try {

			{
				Query q = em
						.createQuery("SELECT i FROM Order o JOIN o.lineItems i WHERE o.price > 2 AND INDEX(i) BETWEEN 0 AND 1000");
				List resultList = q.getResultList();
				for (Object o : resultList) {
					System.out.println("whereClause jpql lineitem: "
							+ ((LineItem) o).getQuantity());
				}

			}
			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<LineItem> cq = cb.createQuery(LineItem.class);
				Root<Order> o = cq.from(Order.class);
				ListJoin<Order, LineItem> i = o.join(Order_.lineItems);
				cq.select(i);
				cq.where(cb.and(cb.gt(o.get(Order_.price), 2), cb.between(i
						.index(), 0, 1000)));

				TypedQuery<LineItem> q = em.createQuery(cq);
				List<LineItem> resultList = q.getResultList();
				for (LineItem r : resultList) {
					System.out.println("whereClause  lineitem: "
							+ r.getLineItemId());
				}
			}
			{ // use conjunction
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<LineItem> cq = cb.createQuery(LineItem.class);
				Root<Order> o = cq.from(Order.class);
				ListJoin<Order, LineItem> i = o.join(Order_.lineItems);
				cq.select(i);

				Predicate condition = cb.conjunction();
				condition = cb.and(condition, cb.gt(o.get(Order_.price), 2));
				condition = cb.and(condition, cb.between(i.index(), 0, 1000));
				cq.where(condition);

				// or equivalently:
				// cq.where(cb.and(cb.gt(o.get(Order_.price), 2), cb.between(i
				// .index(), 0, 1000)));

				TypedQuery<LineItem> q = em.createQuery(cq);
				List<LineItem> resultList = q.getResultList();
				for (LineItem r : resultList) {
					System.out
							.println("whereClause conjunction method lineitem: "
									+ r.getLineItemId());
				}
			}

			{
				Query q = em
						.createQuery("SELECT o FROM Order o WHERE o.lineItems IS NOT EMPTY");
				List resultList = q.getResultList();
				for (Object o : resultList) {
					System.out.println("whereClause 2 jpql order: "
							+ ((Order) o).getLineItems().size());
				}

			}
			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Order> cq = cb.createQuery(Order.class);
				Root<Order> o = cq.from(Order.class);
				cq.select(o);
				cq.where(cb.isNotEmpty(o.get(Order_.lineItems)));

				TypedQuery<Order> q = em.createQuery(cq);
				List<Order> resultList = q.getResultList();
				for (Order r : resultList) {
					System.out.println("whereClause  2 order: "
							+ r.getLineItems().size());
				}
			}

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	protected void literals() {
		System.out.println("testing  literals !!!");
		try {
			{
				Query q = em
						.createQuery("SELECT c FROM Customer c WHERE 'Tony' MEMBER OF c.nickNames");
				List resultList = q.getResultList();
				for (Object o : resultList) {
					Customer c = (Customer) o;
					System.out.println("literals jpql element collection: "
							+ c.getName());
				}
			}
			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
				Root<Customer> c = cq.from(Customer.class);
				cq.select(c);
				cq.where(cb.isMember(cb.literal("Tony"), c
						.get(Customer_.nickNames)));

				TypedQuery<Customer> q = em.createQuery(cq);
				List<Customer> resultList = q.getResultList();
				for (Customer o : resultList) {
					System.out.println("literals cq element collection: "
							+ o.getName());
				}
			}

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	protected void parameterExpression() {
		System.out.println("testing  parameterExpression !!!");
		try {
			{ // one parameter
				Query query = em
						.createQuery("SELECT AVG(o.price) FROM Order o WHERE o.status = :status");
				query.setParameter("status", OrderStatus.BILLED);
				Double price = (Double) query.getSingleResult();

				System.out.println("parameterExpression JPQL average price : "
						+ price);
			}
			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Double> cq = cb.createQuery(Double.class);
				Root<Order> order = cq.from(Order.class);
				cq.select(cb.avg(order.get(Order_.price)));

				ParameterExpression<OrderStatus> status = cb
						.parameter(OrderStatus.class);
				cq.where(cb.equal(order.get(Order_.status), status));

				TypedQuery<Double> q = em.createQuery(cq);
				q.setParameter(status, OrderStatus.BILLED);
				Double price = q.getSingleResult();
				System.out.println("parameterExpression cq average price : "
						+ price);
			}
			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Double> cq = cb.createQuery(Double.class);
				Root<Order> order = cq.from(Order.class);
				cq.select(cb.avg(order.get(Order_.price)));

				ParameterExpression<OrderStatus> status = cb.parameter(
						OrderStatus.class, "status");
				cq.where(cb.equal(order.get(Order_.status), status));

				TypedQuery<Double> q = em.createQuery(cq);
				q.setParameter(status, OrderStatus.BILLED);
				Double price = q.getSingleResult();
				System.out.println("parameterExpression cq2 average price : "
						+ price);
			}

			{ // two parameters
				Query query = em
						.createQuery("SELECT AVG(o.price) FROM Order o WHERE o.status = :stat1 OR o.status = :stat2");
				query.setParameter("stat1", OrderStatus.BILLED);
				query.setParameter("stat2", OrderStatus.NEW);

				Double price = (Double) query.getSingleResult();

				System.out.println("parameterExpression JPQL2 average price : "
						+ price);
			}
			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Double> cq = cb.createQuery(Double.class);
				Root<Order> order = cq.from(Order.class);
				cq.select(cb.avg(order.get(Order_.price)));

				ParameterExpression<OrderStatus> s1 = cb.parameter(
						OrderStatus.class, "stat1");
				ParameterExpression<OrderStatus> s2 = cb.parameter(
						OrderStatus.class, "stat2");

				Path<OrderStatus> status = order.get(Order_.status);
				cq.where(cb.or(cb.equal(status, s1), cb.equal(status, s2)));

				TypedQuery<Double> q = em.createQuery(cq);
				q.setParameter("stat1", OrderStatus.BILLED);
				q.setParameter("stat2", OrderStatus.NEW);

				Double price = q.getSingleResult();
				System.out
						.println("parameterExpression cq2param average price : "
								+ price);
			}

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	protected void expressions() {
		System.out.println("testing  expressions !!!");
		try {

			{ // use the prod and sqrt methods
				Query q = em
						.createQuery("SELECT VALUE(o).price*2.2, SQRT(i.quantity) FROM Customer c JOIN c.orders o JOIN o.lineItems i WHERE TYPE(c) <> GoldCustomer OR i.quantity < 22");
				List resultList = q.getResultList();
				for (Object o : resultList) {
					Object[] result = (Object[]) o;
					System.out.println("expressions jpql order: " + result[0]
							+ ":" + result[1]);
				}

			}
			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
				Root<Customer> c = cq.from(Customer.class);
				MapJoin<Customer, Integer, Order> o = c.join(Customer_.orders);
				Join<Order, LineItem> i = o.join(Order_.lineItems);

				cq.where(cb.or(cb.notEqual(c.type(), GoldCustomer.class), cb
						.lt(i.get(LineItem_.quantity), 22)));
				cq.multiselect(cb.prod(o.value().get(Order_.price), 2.2), cb
						.sqrt(i.get(LineItem_.quantity)));

				TypedQuery<Tuple> q = em.createQuery(cq);
				List<Tuple> resultList = q.getResultList();
				for (Tuple tuple : resultList) {
					Double price = tuple.get(0, Double.class);
					Double p = tuple.get(1, Double.class);
					System.out.println("expressions order: " + ":" + price
							+ ":" + p);
				}
			}

			{ // type and in methods with parameters
				TypedQuery<Customer> q = em
						.createQuery(
								"SELECT c FROM Customer c WHERE TYPE(c) IN (Customer, GoldCustomer)",
								Customer.class);
				// TypedQuery<Customer> q = em
				// .createQuery(
				// "SELECT c FROM Customer c WHERE TYPE(c) IN (:custType1,:custType2)",
				// Customer.class);
				// q.setParameter("custType1", Customer.class);
				// q.setParameter("custType2", GoldCustomer.class);

				List<Customer> resultList = q.getResultList();
				for (Customer o : resultList) {
					System.out
							.println("expressions JPQL type and in customer: "
									+ o.getCustomerType());
				}
			}
			{ // how do I pass the parameters in this case?
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
				Root<Customer> c = cq.from(Customer.class);
				cq.select(c);

				ParameterExpression<Customer> cType1 = cb.parameter(
						Customer.class, "custType1");
				// ParameterExpression<Customer> cType2 = cb
				// .parameter(Customer.class, "custType2");

				cq.where(cb.in(c.type()).value(Customer.class).value(
						GoldCustomer.class));
				// cq.where( c.type().in(cType1).in(cType2));

				TypedQuery<Customer> q = em.createQuery(cq);
				// q.setParameter(cType1, Customer.class);
				// q.setParameter(cType1, PreferredCustomer.class);

				List<Customer> resultList = q.getResultList();
				for (Customer o : resultList) {
					System.out.println("expressions type and in cq customer: "
							+ o.getCustomerType());
				}
			}

			{ // the case method
				Query q = em
						.createQuery("SELECT o.customer.name, i.book, CASE WHEN i.quantity > 10 THEN 'Large Order' WHEN i.quantity > 5  THEN 'Medium Order' ELSE 'Small or No Order' END FROM Order o JOIN o.lineItems i");
				List resultList = q.getResultList();
				for (Object o : resultList) {
					Object[] result = (Object[]) o;
					String name = (String) result[0];
					Book book = (Book) result[1];
					String size = (String) result[2];
					System.out.println("expressions 2jpql: " + name + ":"
							+ book.getIsbn() + ":" + size);
				}

			}
			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
				Root<Order> o = cq.from(Order.class);
				Join<Order, LineItem> i = o.join(Order_.lineItems);

				cq.multiselect(o.get(Order_.customer).get(Customer_.name), i
						.get(LineItem_.book), cb.selectCase().when(
						cb.gt(i.get(LineItem_.quantity), 10), "Large Order")
						.when(cb.gt(i.get(LineItem_.quantity), 5),
								"Midium Order").otherwise("Small or No Order"));

				TypedQuery<Tuple> q = em.createQuery(cq);
				List<Tuple> resultList = q.getResultList();
				for (Tuple tuple : resultList) {
					String name = tuple.get(0, String.class);
					Book book = tuple.get(1, Book.class);
					String quantity = tuple.get(2, String.class);
					System.out.println("expressions 2order: " + name + ":"
							+ book.getIsbn() + ":" + quantity);
				}
			}

			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				Expression<Double> a = cb.literal(2.9);
				Expression<Integer> b = cb.literal(5);
				Expression<?> c = cb.sum(a, b);
				Class type = c.getJavaType(); // it returns java.lang.Double
				System.out.println("expressions type: " + type);

			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	protected void functionExpression() {
		System.out.flush();
		System.out.println("\ntesting  functionExpression !!!");
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
			Root<Order> o = cq.from(Order.class);
			Join<Order, LineItem> i = o.join(Order_.lineItems);

			Expression<Double> priceCeiling = cb.function("CEILING",
					Double.class, o.get(Order_.price));

			cq.multiselect(priceCeiling, o.get(Order_.customer).get(
					Customer_.name), i.get(LineItem_.quantity));

			TypedQuery<Object[]> q = em.createQuery(cq);
			List<Object[]> resultList = q.getResultList();
			for (Object[] tuple : resultList) {
				Double ceiling = (Double) tuple[0];
				String custName = (String) tuple[1];
				Integer bookQuantity = (Integer) tuple[2];
				System.out.println("functionExpression : " + ceiling + ":"
						+ custName + ":" + bookQuantity);
			}

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	protected void subquery() {
		System.out.flush();
		System.out.println("\ntesting  subquery !!!");
		try {
			{ // non-correlated subquery
				Query q = em
						.createQuery("SELECT richCustomer FROM Customer richCustomer WHERE richCustomer.income*0.8 > (SELECT AVG(c.income) FROM Customer c) ");
				List<Customer> resultList = q.getResultList();
				for (Customer result : resultList) {
					System.out.println("subquery jpql income: "
							+ result.getIncome());
				}
			}
			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
				Root<Customer> richCustomer = cq.from(Customer.class);

				// create subquery. The type parameter is the query result type
				Subquery<Double> sq = cq.subquery(Double.class);
				Root<Customer> c = sq.from(Customer.class);

				cq.where(cb.gt(
						cb.prod(richCustomer.get(Customer_.income), 0.8), sq
								.select(cb.avg(c.get(Customer_.income)))));

				cq.select(richCustomer);

				TypedQuery<Customer> q = em.createQuery(cq);
				List<Customer> resultList = q.getResultList();
				for (Customer result : resultList) {
					System.out.println("subquery cq income: "
							+ result.getIncome());
				}
			}

			{ // correlated subquery
				Query query = em
						.createQuery("SELECT DISTINCT c FROM Category c WHERE NOT EXISTS (SELECT parentCategory FROM Category parentCategory WHERE parentCategory = c.parentCategory)");
				List<Category> resultList = query.getResultList();
				for (Category result : resultList) {
					System.out.println("subquery JPQL Category: "
							+ result.getCategoryName());
				}
			}
			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Category> cq = cb.createQuery(Category.class);
				Root<Category> c = cq.from(Category.class);

				// create subquery. The type parameter is the query result type
				Subquery<Category> sq = cq.subquery(Category.class);
				Root<Category> parentCategory = sq.from(Category.class);
				sq.select(parentCategory);
				sq.where(cb.equal(parentCategory, c
						.get(Category_.parentCategory)));

				cq.select(c).distinct(true);
				cq.where(cb.not(cb.exists(sq)));

				TypedQuery<Category> q = em.createQuery(cq);
				List<Category> resultList = q.getResultList();
				for (Category result : resultList) {
					System.out.println("subquery cq Category: "
							+ result.getCategoryName());
				}
			}

			{ // correlated subquery with correlate method
				TypedQuery<Customer> query = em
						.createQuery(
								"SELECT c FROM Customer c WHERE (SELECT COUNT(o) FROM c.orders o WHERE SIZE(o.lineItems) >= 1) > :count",
								Customer.class);
				CriteriaBuilder cb = em.getCriteriaBuilder();
				Parameter<Integer> c = cb.parameter(Integer.class, "count");
				query.setParameter(c, 0);
				List<Customer> resultList = query.getResultList();
				for (Customer result : resultList) {
					System.out.println("subquery JPQL c.orders: "
							+ result.getName());
				}
			}
			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
				Root<Customer> c = cq.from(Customer.class);

				Subquery<Long> sq = cq.subquery(Long.class);
				Root<Customer> customerSub = sq.correlate(c);
				Join<Customer, Order> o = customerSub.join(Customer_.orders);
				sq.where(cb.ge(cb.size(o.get(Order_.lineItems)), 1));

				cq.select(c);
				cq.where(cb.gt(sq.select(cb.count(o)), cb.parameter(
						Integer.class, "count")));

				TypedQuery<Customer> q = em.createQuery(cq);
				q.setParameter("count", 0);
				List<Customer> resultList = q.getResultList();
				for (Customer result : resultList) {
					System.out.println("subquery cq c.orders: "
							+ result.getName());
				}
			}

			{ // correlated subquery with correlate method on Join
				TypedQuery<Customer> query = em
						.createQuery(
								"SELECT c FROM Customer c JOIN c.orders o WHERE :qt <=  ALL (SELECT i.quantity FROM o.lineItems i WHERE i.price > 5)",
								Customer.class);
				CriteriaBuilder cb = em.getCriteriaBuilder();
				Parameter<Integer> q = cb.parameter(Integer.class, "qt");
				query.setParameter(q, 2);
				List<Customer> resultList = query.getResultList();
				for (Customer result : resultList) {
					System.out.println("subquery JPQL c.orders.lineItems: "
							+ result.getName());
				}
			}
			{ // This does not run, although it compiles. Debug it later
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
				Root<Customer> c = cq.from(Customer.class);
				MapJoin<Customer, Integer, Order> o = c.join(Customer_.orders);

				Subquery<Integer> sq = cq.subquery(Integer.class);
				MapJoin<Customer, Integer, Order> orderSub = sq.correlate(o);
				Join<Order, LineItem> i = orderSub.join(Order_.lineItems);
				sq.where(cb.gt(i.get(LineItem_.price), 5));

				cq.select(c);
				cq.where(cb.ge(cb.all(sq.select(i.get(LineItem_.quantity))), cb
						.parameter(Integer.class, "qt")));

				TypedQuery<Customer> q = em.createQuery(cq);
				q.setParameter("qt", 2);
				
				// It does not run with EclipseLink 2.0.0
//				List<Customer> resultList = q.getResultList();
//				for (Customer result : resultList) {
//					System.out.println("subquery cq c.orders.lineItems: "
//							+ result.getName());
//				}

			}

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	protected void groupByHavingClause() {
		System.out.flush();
		System.out.println("\ntesting  groupByHavingClause !!!");
		try {
			{
				TypedQuery<Object[]> query = em
						.createQuery(
								"SELECT o.status, COUNT(c), AVG(o.price) FROM Customer c JOIN c.orders o WHERE o.price > 10 GROUP BY o.status HAVING o.status IN (:s, :s2)",
								Object[].class);
				CriteriaBuilder cb = em.getCriteriaBuilder();
				Parameter<OrderStatus> s = cb.parameter(OrderStatus.class, "s");
				Parameter<OrderStatus> t = cb
						.parameter(OrderStatus.class, "s2");
				query.setParameter(s, OrderStatus.BILLED);
				query.setParameter(t, OrderStatus.NEW);

				List<Object[]> resultList = query.getResultList();
				for (Object[] result : resultList) {
					OrderStatus status = (OrderStatus) result[0];
					Long count = (Long) result[1];
					Double avgPrice = (Double) result[2];
					System.out.println("groupByHavingClause JPQL custOrder: "
							+ status + ":" + count + ":" + avgPrice);
				}
			}
			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
				Root<Customer> c = cq.from(Customer.class);
                Join<Customer, Order> o = c.join(Customer_.orders);
                cq.multiselect(o.get(Order_.status), cb.count(c), cb.avg(o.get(Order_.price)));
                cq.where(cb.gt(o.get(Order_.price), 10));
                cq.groupBy(o.get(Order_.status));
                ParameterExpression<OrderStatus> os = cb.parameter(OrderStatus.class);
                ParameterExpression<OrderStatus> os2 = cb.parameter(OrderStatus.class);
                cq.having(cb.in(o.get(Order_.status)).value(os).value(os2));
                TypedQuery<Object[]> q = em.createQuery(cq);
                q.setParameter(os, OrderStatus.BILLED);
                q.setParameter(os2, OrderStatus.NEW);
				List<Object[]> resultList = q.getResultList();
				for (Object[] result : resultList) {
					OrderStatus status = (OrderStatus) result[0];
					Long count = (Long) result[1];
					Double avgPrice = (Double) result[2];
					System.out.println("groupByHavingClause cq custOrder: "
							+ status + ":" + count + ":" + avgPrice);
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	protected void orderByClause() {
		System.out.flush();
		System.out.println("\ntesting  orderByClause !!!");
		try {
			{
				TypedQuery<Object[]> query = em
						.createQuery(
								"SELECT o.price, i.price AS itemPrice FROM Customer c JOIN c.orders o JOIN o.lineItems i WHERE c.name LIKE 'John%' AND o.price > 10 ORDER BY o.price, itemPrice DESC",
								Object[].class);
				List<Object[]> resultList = query.getResultList();
				for (Object[] result : resultList) {
					Double orderPrice = (Double) result[0];
					Double itemPrice = (Double) result[1];
					System.out.println("orderByClause JPQL Order: "
							+ orderPrice + ":" + itemPrice);
				}
			}
			{
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
				Root<Customer> c = cq.from(Customer.class);
				Join<Customer, Order> o = c.join(Customer_.orders);
				Join<Order, LineItem> i = o.join(Order_.lineItems);

				Expression<Double> orderExpr = o.get(Order_.price);
				Expression<Double> itemExpr = i.get(LineItem_.price);
				cq.multiselect(orderExpr, cb.prod(itemExpr, 2));
				cq.where(cb.like(c.get(Customer_.name), "John%"), cb.gt(
						orderExpr, 10));
				cq.orderBy(cb.asc(orderExpr), cb.desc(cb.prod(itemExpr, 2)));

				TypedQuery<Object[]> q = em.createQuery(cq);
				List<Object[]> resultList = q.getResultList();
				for (Object[] result : resultList) {
					Double orderPrice = (Double) result[0];
					Double itemPrice = (Double) result[1];
					System.out.println("orderByClause cq custOrder: "
							+ orderPrice + ":" + itemPrice);
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	protected void criteriaQueriesUsingMetamodel() {
		System.out.flush();
		System.out.println("\ntesting  criteriaQueriesUsingMetamodel !!!");
		try {

			// SELECT o.status, COUNT(c), AVG(o.price) FROM Customer c JOIN
			// c.orders o
			// WHERE o.price > 10 GROUP BY o.status HAVING o.status IN (:s, :s2)

			Metamodel mm = em.getMetamodel();
			EntityType<Customer> customer_ = mm.entity(Customer.class);
			EntityType<Order> order_ = mm.entity(Order.class);

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
			Root<Customer> c = cq.from(Customer.class);
			MapJoin<Customer, Integer, Order> o = c.join(customer_.getMap(
					"orders", Integer.class, Order.class));

			Expression<OrderStatus> status = o.get(order_.getSingularAttribute(
					"status", OrderStatus.class));
			Expression<Double> price = o.get(order_.getSingularAttribute(
					"price", Double.class));

			cq.multiselect(status, cb.count(c), cb.avg(price));
			cq.where(cb.gt(price, 10));
			cq.groupBy(status);
			cq.having(cb.in(status).value(OrderStatus.BILLED).value(
					OrderStatus.NEW));

			TypedQuery<Object[]> query = em.createQuery(cq);
			List<Object[]> resultList = query.getResultList();
			for (Object[] result : resultList) {
				OrderStatus st = (OrderStatus) result[0];
				Long count = (Long) result[1];
				Double avgPrice = (Double) result[2];
				System.out
						.println("criteriaQueriesUsingMetamodel cq custOrder: "
								+ st + ":" + count + ":" + avgPrice);
			}

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	protected void weakerTypedCriteriaQueries() {
		System.out.flush();
		System.out.println("\ntesting  weakerTypedCriteriaQueries !!!");
		try {

			// SELECT o.status, COUNT(c), AVG(o.price) FROM Customer c JOIN
			// c.orders o
			// WHERE o.price > 10 GROUP BY o.status HAVING o.status IN (:s, :s2)

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
			Root<Customer> c = cq.from(Customer.class);
			Join<Customer, Order> o = c.join("orders");

			Expression s = o.get("price");

			cq.multiselect(o.get("status"), cb.count(c), cb.avg(o
					.<Double> get("price")));
			cq.where(cb.gt(o.<Double> get("price"), 10));
			cq.groupBy(o.get("status"));
			cq.having(cb.in(o.get("status")).value(OrderStatus.BILLED).value(
					OrderStatus.NEW));

			TypedQuery<Object[]> query = em.createQuery(cq);
			List<Object[]> resultList = query.getResultList();
			for (Object[] result : resultList) {
				OrderStatus st = (OrderStatus) result[0];
				Long count = (Long) result[1];
				Double avgPrice = (Double) result[2];
				System.out.println("weakerTypedCriteriaQueries cq custOrder: "
						+ st + ":" + count + ":" + avgPrice);
			}

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	protected void queryModification() {
		System.out.flush();
		System.out.println("\ntesting  queryModification !!!");
		try {
			
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
			Root<Customer> c = cq.from(Customer.class);
			Join<Customer, Address> a = c.join(Customer_.address);
			cq.select(c);
			cq.where(cb.like(c.get(Customer_.name), "John%"));
			cq.orderBy(cb.asc(a.get(Address_.zip)));
			
			TypedQuery<Customer> query = em.createQuery(cq);
			List<Customer> resultList = query.getResultList();
			for (Customer result : resultList) {
				System.out.println("queryModification cq customer: "
						+ result.getName() +":" + result.getAddress().getZip());
			}
			
			List<javax.persistence.criteria.Order> orders = cq.getOrderList();
			List<javax.persistence.criteria.Order> orders2 = new ArrayList<javax.persistence.criteria.Order>(orders);
			orders2.add(cb.desc(a.get(Address_.state)));
			cq.orderBy(orders2);
			
			TypedQuery<Customer> query2 = em.createQuery(cq);
			List<Customer> resultList2 = query.getResultList();
			for (Customer result : resultList2) {
				System.out.println("queryModification cq customer 2: "
						+ result.getName() +":" + result.getAddress().getZip());
			}

			
			
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
