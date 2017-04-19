package jpatest.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpatest.entity.Book;
import jpatest.entity.Order;

@Generated("EclipseLink-2.0.2.v20100323-r6872 @ Wed May 26 16:22:36 EDT 2010")
@StaticMetamodel(LineItem.class)
public class LineItem_ extends BaseEntity_ {

	public static volatile SingularAttribute<LineItem, Double> price;
	public static volatile SingularAttribute<LineItem, Order> order;
	public static volatile SingularAttribute<LineItem, Book> book;
	public static volatile SingularAttribute<LineItem, Integer> quantity;
	public static volatile SingularAttribute<LineItem, Integer> lineItemId;

}