package jpatest.ejb;

import javax.ejb.Local;

import jpatest.entity.LineItem;
import jpatest.entity.Order;

@Local
public interface LineItemSLLocal {	
	public void addLineItem(Order order, LineItem li);
}
