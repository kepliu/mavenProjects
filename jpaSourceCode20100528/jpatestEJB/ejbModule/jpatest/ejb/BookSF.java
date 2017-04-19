package jpatest.ejb;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import jpatest.entity.Book;
import jpatest.entity.Category;

//application-managed entity manager with container-managed transaction in a 
//stateful session bean.

@Stateful
public class BookSF implements BookSFLocal {

	@PersistenceUnit(unitName = "jpaTestJtaPU")
	private EntityManagerFactory emf;
	private EntityManager em;
	
	private Book book;
	private Category category;

	@PostConstruct
	public void postConstruct() {
		em = emf.createEntityManager();
	}
	
	public void init(Book book, Category cat) {				
		this.book = book;
		this.category = cat;
	}

	public Book createBook() {
		em.joinTransaction();   // join current JTA transaction
		em.persist(book);
		if (category != null) em.persist(category);    // also create category in this example

		return book;
	}
	
	public void addCategory(Category c) {
		em.joinTransaction();   // join current JTA transaction
		book.getCategories().add(c);	
	}
	
	// create a category and add it to the book
	public Category createCategory(String categoryName) {
		em.joinTransaction();
		Category c = new Category();
		c.setCategoryName(categoryName);
		book.getCategories().add(c);
		em.persist(c);
		return c;
	}
		
	public Book getBook(int id) {
		Book book = em.find(Book.class, id);
		return book;
	}

	@PreDestroy
	public void destroy() {
		em.close();
	}

}
