package jpatest.eao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import jpatest.entity.Book;


public class BookDAO {
	private EntityManagerFactory emf = null;

	public BookDAO() {
		Map<String, String> props = new HashMap<String, String>();
		props.put("javax.persistence.jdbc.user", "jpatest");
		props.put("javax.persistence.jdbc.password", "jpatest");		
		emf = Persistence.createEntityManagerFactory("jpaTestPU", props);		
	}

	public void createBook(Book b) {
		EntityManager em = emf.createEntityManager();
				
		try {
			em.getTransaction().begin();
			em.persist(b);
			em.getTransaction().commit();
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
	}

	public Book findBook(int bookID) {
		EntityManager em = emf.createEntityManager();
		Book b = null;

		try {
			b = em.find(Book.class, bookID);
		} finally {
			em.close();
		}

		return b;
	}
	
	public List<Book> retrieveBooks(String title) {
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createQuery("SELECT b FROM Book b WHERE b.title = :bookTitle");
		query.setParameter("bookTitle", title);
		List<Book> result = query.getResultList();
		
		em.close();
		return result;
	}
	
	public double averagePrice(String title) {
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createQuery("SELECT AVG(b.price) FROM Book b WHERE b.title = :bookTitle");
		query.setParameter("bookTitle", title);
		Double ap = (Double) query.getSingleResult();
		
		em.close();
		return ap;
	}
	
	public void deleteBook(Book b) {
		EntityManager em = emf.createEntityManager();	
		
		try {
			em.getTransaction().begin();
			em.remove( em.merge(b));
			em.getTransaction().commit();
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
	}
	

	// make sure this method is called after use to clean resources
	public void destroy() {
		if (emf != null && emf.isOpen())
			emf.close();
	}

}
