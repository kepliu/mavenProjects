package jpatest.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpatest.entity.Book;

@Generated("EclipseLink-2.0.2.v20100323-r6872 @ Wed May 26 16:22:36 EDT 2010")
@StaticMetamodel(Content.class)
public class Content_ { 

	public static volatile SingularAttribute<Content, byte[]> pdf;
	public static volatile SingularAttribute<Content, Book> book;
	public static volatile SingularAttribute<Content, Integer> bookId;

}