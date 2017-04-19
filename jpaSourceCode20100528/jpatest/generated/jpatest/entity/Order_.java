package jpatest.entity;

import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpatest.entity.Customer;
import jpatest.entity.LineItem;
import jpatest.entity.OrderStatus;

@Generated("EclipseLink-2.0.2.v20100323-r6872 @ Wed May 26 16:22:36 EDT 2010")
@StaticMetamodel(Order.class)
public class Order_ extends BaseEntity_ {

	public static volatile SingularAttribute<Order, Double> price;
	public static volatile SingularAttribute<Order, OrderStatus> status;
	public static volatile SingularAttribute<Order, Timestamp> orderTime;
	public static volatile SingularAttribute<Order, Customer> customer;
	public static volatile ListAttribute<Order, LineItem> lineItems;
	public static volatile SingularAttribute<Order, Integer> orderId;

}