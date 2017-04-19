package jpatest.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpatest.entity.Category;
import jpatest.entity.LineItem;

@Generated("EclipseLink-2.0.2.v20100323-r6872 @ Wed May 26 16:22:36 EDT 2010")
@StaticMetamodel(Book.class)
public class Book_ extends BaseEntity_ {

	public static volatile SingularAttribute<Book, String> author;
	public static volatile SingularAttribute<Book, String> title;
	public static volatile SingularAttribute<Book, Double> price;
	public static volatile SingularAttribute<Book, byte[]> pdf;
	public static volatile SingularAttribute<Book, String> isbn;
	public static volatile SingularAttribute<Book, Integer> bookId;
	public static volatile SetAttribute<Book, Category> categories;
	public static volatile SingularAttribute<Book, Integer> rating;
	public static volatile ListAttribute<Book, LineItem> lineItems;

}