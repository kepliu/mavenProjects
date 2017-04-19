package jpatest.eao;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.Query;

import jpatest.entity.Book;
import jpatest.entity.Category;
import jpatest.entity.Content;


public class BookCategoryEAO extends BaseEAO {
	public void createBookCategory(List<Book> books, List<Category> categories) {
		EntityManager em = createEntityManager();

		try {
			em.getTransaction().begin();
			for (Book b : books) {
				em.persist(b);
				//Content c = b.getContent();
				//c.setBookId(b.getBookId());
			}
			for (Category c : categories) {
				em.persist(c);
			}
			
			em.getTransaction().commit();
			
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			closeEntityManager(em);
		}
	}

	public Object[] updateBookCategory(List<Book> books,
			List<Category> categories) {
		List<Book> booksNew = new ArrayList<Book>();
		List<Category> categoriesNew = new ArrayList<Category>();
		
		EntityManager em = createEntityManager();

		try {
			em.getTransaction().begin();
			for (Book b : books) {
				booksNew.add(em.merge(b));
			}
			for (Category c : categories) {
				categoriesNew.add(em.merge(c));
			}
			em.getTransaction().commit();
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			closeEntityManager(em);
		}

		return new Object[] { booksNew, categoriesNew };
	}

	public void deleteBookCategories(List<Book> books, List<Category> categories) {
		EntityManager em = createEntityManager();

		try {
			em.getTransaction().begin();
			for (Book b : books) {
				em.remove(em.merge(b));
			}
			em.getTransaction().commit();
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback();
		}

		try {
			em.getTransaction().begin();

			for (int i = 0; i < categories.size(); i++) {
				Category c = categories.get(i);
				em.remove(em.merge(c));
				em.flush();
				System.out.println("deleting category: " + c.getCategoryName()
						+ " " + c.getCategoryId());
			}
			em.getTransaction().commit();
		} catch (Throwable t) {
			t.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			closeEntityManager(em);
		}
	}
}
