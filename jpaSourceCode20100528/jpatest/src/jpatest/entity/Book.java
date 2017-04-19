package jpatest.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "BOOK")
@SecondaryTable(name = "CONTENT", pkJoinColumns = @PrimaryKeyJoinColumn(name = "BOOK_ID_FPK", referencedColumnName = "BOOK_ID_PK"))
@AttributeOverrides( {
		@AttributeOverride(name = "createUser", column = @Column(name = "BOOK_CREATE_USER")),
		@AttributeOverride(name = "createTime", column = @Column(name = "BOOK_CREATE_TIME")),
		@AttributeOverride(name = "updateUser", column = @Column(name = "BOOK_UPDATE_USER")),
		@AttributeOverride(name = "updateTime", column = @Column(name = "BOOK_UPDATE_TIME")) })
@TableGenerator(name = "BOOK_SEQ_GEN", table = "SEQUENCE_GENERATOR_TB", pkColumnName = "SEQUENCE_NAME", valueColumnName = "SEQUENCE_VALUE", pkColumnValue = "BOOK_SEQ")
public class Book extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "BOOK_SEQ_GEN")
	@Column(name = "BOOK_ID_PK")
	protected int bookId;

	protected String isbn;
	protected String author;
	protected String title;
	protected int rating;

	@Column(name = "PRICE", precision = 12, scale = 2)
	protected double price;

	@OneToMany(mappedBy = "book", cascade = CascadeType.PERSIST)
	protected List<LineItem> lineItems;

	@ManyToMany
	@JoinTable(name = "BOOK_CATEGORY", joinColumns = @JoinColumn(name = "BOOK_ID_FPK", referencedColumnName = "BOOK_ID_PK"), inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID_FPK", referencedColumnName = "CATEGORY_ID_PK"))
	protected Set<Category> categories;

	@Basic(fetch = FetchType.LAZY)
	@Lob
	@Column(table = "CONTENT", name = "PDF", columnDefinition = "BLOB NOT NULL")
	protected byte[] pdf;
	
	//testing Content using a one-to-one relationship instead of a field mapped to a secondary table
	//@OneToOne(mappedBy="book", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	//protected Content content;

	private static final long serialVersionUID = 1L;

	public Book() {
		super();
	}

	public int getBookId() {
		return this.bookId;
	}

	public String getIsbn() {
		return this.isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<LineItem> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<LineItem> lineItems) {
		this.lineItems = lineItems;
	}

	public Set<Category> getCategories() {
		return this.categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	
	public byte[] getPdf() {
		return pdf;
	}

	public void setPdf(byte[] pdf) {
		this.pdf = pdf;
	}
//	
//	public Content getContent() {
//		return content;
//	}
//
//	public void setContent(Content content) {
//		this.content = content;
//	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

}
