package jpatest.ejb;

import javax.ejb.Local;

import jpatest.entity.Book;
import jpatest.entity.Category;

@Local
public interface BookSLLocal {
	public Book createBook(Book book, Category cat);

	public Book getBook(int id);
}
