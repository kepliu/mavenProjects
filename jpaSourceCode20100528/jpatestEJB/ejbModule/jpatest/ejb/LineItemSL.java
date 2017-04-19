package jpatest.ejb;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jpatest.entity.LineItem;
import jpatest.entity.Order;

// persistence propagation with transaction 

@Stateless
public class LineItemSL implements LineItemSLLocal {
	@PersistenceContext(unitName = "jpaTestJtaPU")
	EntityManager em;
	
	// when this method is called from OrderSL.addLineItem. The persistence
	// context created inside that method is propagated to this method.
	
	// It would not work if we do 
	//@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	// since order and em will be merged into two different persistence contexts.
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void addLineItem(Order order, LineItem li) {
		em.merge(li);
		li.setOrder(order);
		order.getLineItems().add(li);
	}
	
}
