package jpatest.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpatest.entity.Book;
import jpatest.entity.Category;

@Generated("EclipseLink-2.0.2.v20100323-r6872 @ Wed May 26 16:22:36 EDT 2010")
@StaticMetamodel(Category.class)
public class Category_ extends BaseEntity_ {

	public static volatile SetAttribute<Category, Category> childCategories;
	public static volatile SingularAttribute<Category, String> categoryName;
	public static volatile ListAttribute<Category, Book> books;
	public static volatile SingularAttribute<Category, Category> parentCategory;
	public static volatile SingularAttribute<Category, Integer> categoryId;

}