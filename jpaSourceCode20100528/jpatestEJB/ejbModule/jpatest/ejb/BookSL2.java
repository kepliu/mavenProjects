package jpatest.ejb;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import jpatest.entity.Book;
import jpatest.entity.Category;

//application-managed entity manager with container-managed transaction.
//Session bean creates entity manager in PostContruct method and clears 
//persistence context at the end of each business method.

@Stateless
public class BookSL2 implements BookSL2Local {

	@PersistenceUnit(unitName = "jpaTestJtaPU")
	private EntityManagerFactory emf;
	private EntityManager em;

	@PostConstruct
	public void init() {
		em = emf.createEntityManager();
	}

	public Book createBook(Book book, Category cat) {
		em.joinTransaction(); // join current JTA transaction
		em.persist(book);
		if (cat != null)
			em.persist(cat); // also create category in this example

		em.flush(); // flush to database.
		em.clear(); // managed entities will become detached
		return book; // remains managed until JTA transaction ends
	}

	public void addCategory(Book b, Category c) {
		em.joinTransaction(); // join current JTA transaction
		em.merge(b);
		b.getCategories().add(c);
		em.flush(); // flush to database.
		em.clear(); // managed entities will become detached
	}

	// create a category and add it to the book
	public Category createCategory(Book b, String categoryName) {
		em.joinTransaction();
		Category c = new Category();
		c.setCategoryName(categoryName);
		b.getCategories().add(c);
		em.persist(c);
		em.flush();
		em.clear();
		return c;
	}

	public Book getBook(int id) {
		Book book = em.find(Book.class, id);
		em.clear();
		return book;
	}

	@PreDestroy
	public void destroy() {
		em.close();
	}

}
