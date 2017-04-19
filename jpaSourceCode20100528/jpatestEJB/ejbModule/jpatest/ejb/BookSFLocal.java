package jpatest.ejb;
import javax.ejb.Local;

import jpatest.entity.Book;
import jpatest.entity.Category;

@Local
public interface BookSFLocal {
	public void init(Book book, Category cat);
	public Book createBook();

	public Book getBook(int id);

}
