package jpatest.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name="LINE_ITEM")
public class LineItem extends BaseEntity {
	@Id
	@TableGenerator(name = "LM_SEQ_GEN", table = "SEQUENCE_GENERATOR_TB", pkColumnName = "SEQUENCE_NAME", valueColumnName = "SEQUENCE_VALUE", pkColumnValue = "LINE_ITEM_SEQ")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "LM_SEQ_GEN")
	@Column(name="LINE_ITEM_ID_PK")
	protected int lineItemId;

	protected int quantity;

	protected double price;

	@ManyToOne
	@JoinColumn(name="BOOK_ID_FK", referencedColumnName="BOOK_ID_PK")
	protected Book book;

	@ManyToOne
	@JoinColumn(name="ORDER_ID_FK", referencedColumnName="ORDER_ID_PK")
	protected Order order;   // owning side of Order to LineItem relationship

	private static final long serialVersionUID = 1L;

	public LineItem() {
		super();
	}

	public int getLineItemId() {
		return this.lineItemId;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Book getBook() {
		return this.book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Order getOrder() {
		return this.order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

}
