package jpatest.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

// alternative to using a secondary table. 
@Entity
@Table(name = "CONTENT")
public class Content {
	@Id
	@Column(name = "BOOK_ID_FPK", updatable=false)
	protected int bookId;
	
	@OneToOne
	@PrimaryKeyJoinColumn(name = "BOOK_ID_FPK", referencedColumnName = "BOOK_ID_PK")
	protected Book book;
	
	@Lob
	@Column(name = "PDF")
	protected byte[] pdf;

	private static final long serialVersionUID = 1L;

	public Content() {
		super();
	}

	public int getBookId() {
		return this.bookId;
	}
	
	public void setBookId(int id) {
		this.bookId = id;
	}

	
	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public byte[] getPdf() {
		return pdf;
	}

	public void setPdf(byte[] pdf) {
		this.pdf = pdf;
	}
}
