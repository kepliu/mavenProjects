package jpatest.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;


import org.junit.Test;

import jpatest.eao.BookCategoryEAO;
import jpatest.eao.BookDAO;
import jpatest.entity.Book;
import jpatest.entity.Category;

public class BookCategoryTest extends MyJpaTestCase {

	@Test
	public void testBookCategoryEAO() {

		Object[] created = createBookCategory(em);
		List<Book> books = (List<Book>) created[0];
		List<Category> categories = (List<Category>) created[1];

		Object[] updated = updateBookCategory(books, categories);
		List<Book> booksNew = (List<Book>) updated[0];
		List<Category> categoriesNew = (List<Category>) updated[1];

		deleteBookCategory(em, booksNew, categoriesNew);

		bookDAOTest();
		section2210Test();
	}

	protected static Object[] createBookCategory(EntityManager em) {
		Book book1 = new Book();
		book1.setIsbn("isbn1");
		book1.setTitle("Book1 Title");
		book1.setPrice(20.50);
		String content = "njenfuenmjvnjelenfe";
		byte[] pdf = content.getBytes();
		book1.setPdf(pdf);

//		Content ct = new Content();
//		ct.setPdf(pdf);
//		ct.setBook(book1);
//		book1.setContent(ct);

		Book book2 = new Book();
		book2.setIsbn("isbn2");
		book2.setTitle("Book2 Title");
		book2.setPrice(30.50);

//		Content ct2 = new Content();
//		ct2.setPdf(pdf);
//		ct2.setBook(book2);
//		book2.setContent(ct2);

		Book book3 = new Book();
		book3.setIsbn("isbn3");
		book3.setTitle("Book3 Title");

//		Content ct3 = new Content();
//		ct3.setPdf(pdf);
//		ct3.setBook(book3);
//		book3.setContent(ct3);

		Category cat1 = new Category();
		cat1.setCategoryName("cat1 Social Literature");

		Category cat2 = new Category();
		cat2.setCategoryName("cat2 Novels");

		Category cat3 = new Category();
		cat3.setCategoryName("cat3 Fiction old");

		Category cat4 = new Category();
		cat4.setCategoryName("cat4 Fiction new");

		cat2.setParentCategory(cat1);
		cat3.setParentCategory(cat2);
		cat4.setParentCategory(cat2);
		
		Set<Category> childCategories = new HashSet<Category>();
		childCategories.add(cat2);
		cat1.setChildCategories(childCategories);
		
		childCategories = new HashSet<Category>();
		childCategories.add(cat3);
		childCategories.add(cat4);
		cat2.setChildCategories(childCategories);

		Set<Category> book1Cat = new HashSet<Category>();
		book1Cat.add(cat1);
		book1Cat.add(cat2);
		book1Cat.add(cat3);
		book1.setCategories(book1Cat);

		Set<Category> book2Cat = new HashSet<Category>();
		book2Cat.add(cat1);
		book2Cat.add(cat2);
		book2Cat.add(cat4);
		book2.setCategories(book2Cat);

		Set<Category> book3Cat = new HashSet<Category>();
		book3Cat.add(cat1);
		book3.setCategories(book3Cat);
		
		List<Book> cat1Book = new LinkedList<Book>();
		cat1Book.add(book1);
		cat1Book.add(book2);
		cat1Book.add(book3);
		
		List<Book> cat2Book = new LinkedList<Book>();
		cat2Book.add(book1);
		cat2Book.add(book2);
		
		List<Book> cat3Book = new LinkedList<Book>();
		cat3Book.add(book1);
		
		List<Book> cat4Book = new LinkedList<Book>();
		cat4Book.add(book2);
		
		cat1.setBooks(cat1Book);
		cat2.setBooks(cat2Book);
		cat3.setBooks(cat3Book);
		cat4.setBooks(cat4Book);

		List<Book> books = new ArrayList<Book>();
		books.add(book1);
		books.add(book2);
		books.add(book3);

		List<Category> categories = new ArrayList<Category>();
		categories.add(cat1);
		categories.add(cat2);
		categories.add(cat3);
		categories.add(cat4);
		
		// insert the books and categories into db
		BookCategoryEAO bcEao = new BookCategoryEAO();
		bcEao.createBookCategory(books, categories);

		/**
		 * Test the correctness of the create operations
		 */
		Category cc1 = em.find(Category.class, cat1.getCategoryId());
		List<Book> cat1Books = cc1.getBooks();
		System.out.println("\ncat1 has books: " + cat1Books.size());
		assertTrue(3 == cat1Books.size());

		Query query = em
				.createQuery("SELECT c FROM Category c WHERE c.categoryId = ?1");
		query.setParameter(1, cat1.getCategoryId());
		query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
		Category result = (Category) query.getSingleResult();
		List<Book> cat1Books2 = result.getBooks();
		System.out.println("\ncat1 has books: " + cat1Books2.size());
		assertTrue(3 == cat1Books2.size());

		Set<Category> cat1Children = result.getChildCategories();
		assertTrue(1 == cat1Children.size());

		// it finds the object in cache, not related objects
		Category c3 = em.find(Category.class, cat3.getCategoryId());
		List<Book> cat3Books = c3.getBooks();
		System.out.println("\ncat3 has books: " + cat3Books.size());

		query.setParameter(1, cat3.getCategoryId());
		Category result3 = (Category) query.getSingleResult();
		List<Book> cat3Books2 = result3.getBooks();
		System.out.println("\n\ncat1 has books: " + cat3Books2.size());
		assertTrue(1 == cat3Books2.size());

		Category ca3 = em.find(Category.class, cat3.getCategoryId());
		List<Book> cat3Books3 = ca3.getBooks();
		assertTrue(1 == cat3Books3.size());

		Object[] results = new Object[] { books, categories };
		return results;
	}

	protected Object[] updateBookCategory(List<Book> books,
			List<Category> categories) {

		books.get(0).setTitle("Book1 Title New");
		books.get(1).setTitle("Book2 Title New");
		books.get(2).setTitle("Book2 Title New");
		categories.get(0).setCategoryName("cat1 Social Literature New");
		categories.get(1).setCategoryName("cat2 Novels New");
		categories.get(2).setCategoryName("cat3 Fiction old New");
		categories.get(3).setCategoryName("cat4 Fiction new New");

		BookCategoryEAO bcEao = new BookCategoryEAO();
		Object[] bc = bcEao.updateBookCategory(books, categories);

		List<Book> booksNew = (List<Book>) bc[0];
		List<Category> categoriesNew = (List<Category>) bc[1];
		assertTrue(booksNew.get(0).getVersion() == books.get(0).getVersion() + 1);
		assertTrue(categoriesNew.get(0).getVersion() == categories.get(0)
				.getVersion() + 1);

		return bc;
	}

	protected static void deleteBookCategory(EntityManager em,
			List<Book> books, List<Category> categories) {

		List<Category> deleteCategories = new ArrayList<Category>();
		// due to parent-child relationship, delete in order:
		// delete cat4 first, then cat3, then cat2, and finally delete cat1
		deleteCategories.add(0, categories.get(3));
		deleteCategories.add(1, categories.get(2));
		
		categories.get(1).setChildCategories(null);
		deleteCategories.add(2, categories.get(1));
		categories.get(0).setChildCategories(null);
		deleteCategories.add(3, categories.get(0));

		BookCategoryEAO bcEao = new BookCategoryEAO();
		bcEao.deleteBookCategories(books, deleteCategories);

		Query query = em
				.createQuery("SELECT b FROM Book b WHERE b.bookId = ?1");
		query.setParameter(1, books.get(0).getBookId());
		query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
		Book boo1 = null;
		try {
			boo1 = (Book) query.getSingleResult();
		} catch (NoResultException e) {
			System.out.println("Book1 not found after delete. Correct");
		}
		assertTrue(boo1 == null);

		Book b1 = em.find(Book.class, books.get(0).getBookId());
		// However, it is possible. Book1 is still in cache.
		if (b1 == null) {
		  System.out.print("book1 no longer existed in cache: \n");
		} else {
		  System.out.print("cached book1: " + b1.getBookId() + "\n");
		}

	}

	public void bookDAOTest() {
		BookDAO bookDao = new BookDAO();

		Book book1 = new Book();
		book1.setIsbn("isbn1");
		book1.setTitle("Book1 Title");
		book1.setPrice(20.50);
		String content = "njenfuenmjvnjelenfe";
		byte[] pdf = content.getBytes();
		book1.setPdf(pdf);

		bookDao.createBook(book1);

		int bookID = book1.getBookId();
		Book b = bookDao.findBook(bookID);
		System.out.println("Book found: " + b.getTitle());

		double ap = bookDao.averagePrice(b.getTitle());
		System.out.println("average book price: " + ap);

		List<Book> books = bookDao.retrieveBooks(b.getTitle());
		System.out.println("# Book retrieved: " + books.size());
		for (Book bb : books) {
			bookDao.deleteBook(bb);
		}

	}

	public void section2210Test() {
		// test for JPA 2.0 speck Section 2.2.10
		em.getTransaction().begin();
		Book b = new Book();
		b.setIsbn("isbn1");
		b.setTitle("Book1 Title");
		b.setPrice(20.50);
		b.setPdf("samplecontents".getBytes());

		em.persist(b);

		// auto ID is available after persist operation
		int bookId = b.getBookId();
		System.out.println("book id is: " + bookId);

		// set flush mode to COMMIT
		em.setFlushMode(FlushModeType.COMMIT);

		// programmatically call flush
		em.flush();

		em.getTransaction().commit();
	}

}
