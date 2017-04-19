package jpatest.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import jpatest.entity.Book;
import jpatest.entity.Category;

// application-managed entity manager with container-managed transaction.
// Session bean creates and close an entity manager in each business method.
@Stateless
public class BookSL implements BookSLLocal {

	@PersistenceUnit(unitName = "jpaTestJtaPU")
	private EntityManagerFactory emf;

	public Book createBook(Book book, Category cat) {
		EntityManager em = emf.createEntityManager();
		em.persist(book);
		
		if (cat != null) em.persist(cat);    // also create category in this example
		
		em.close();
		return book; // remains managed until JTA transaction ends
	}
	
	public void addCategory(Book b, Category c) {
		EntityManager em = emf.createEntityManager();
		em.merge(b);
		b.getCategories().add(c);
		em.close();	
	}
	
	// create a category and add it to the book
	public Category createCategory(Book b, String categoryName) {
		EntityManager em = emf.createEntityManager();
		Category c = new Category();
		c.setCategoryName(categoryName);
		b.getCategories().add(c);
		em.persist(c);
		em.close();
		return c;
	}
	
	public Book getBook(int id) {
		EntityManager em = emf.createEntityManager();
		Book book = em.find(Book.class, id);
		em.close();
		return book;
	}
	
}
