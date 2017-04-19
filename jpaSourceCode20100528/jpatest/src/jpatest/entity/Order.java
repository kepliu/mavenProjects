package jpatest.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EntityResult;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ExcludeDefaultListeners;
import javax.persistence.ExcludeSuperclassListeners;
import javax.persistence.FieldResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.QueryHint;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name="ORDERS")
@NamedQuery(name="selectCustomerAndPrice", 
  query="SELECT o.customer, o.price FROM Order o WHERE o.orderTime < ?1", 
  hints={@QueryHint(name="javax.persistence.query.timeout", value="1000"), 
	     @QueryHint(name="eclipselink.cache-usage", value="DoNotCheckCache"),
	     @QueryHint(name="eclipselink.jdbc.fetch-size", value="256"),
	     @QueryHint(name="eclipselink.read-only", value="TRUE") }
)

@NamedQueries({
	@NamedQuery(name="selectOrderPrice", query="SELECT o.price FROM Order o WHERE o.status = :status"),
	@NamedQuery(name="selectCustomerAndPrice2", 
			query="SELECT o.customer, o.price FROM Order o WHERE o.orderTime < ?1", 
			hints={@QueryHint(name="javax.persistence.query.timeout", value="1000"),
			       @QueryHint(name="eclipselink.cache-usage", value="DoNotCheckCache") })
})
@NamedNativeQueries({
	@NamedNativeQuery(name="nativeCustomerSQL", 
			query="SELECT c.customer_id_pk, c.customer_type, c.name, c.income FROM customer c WHERE c.name LIKE ?1",
			resultClass=Customer.class),
	@NamedNativeQuery(name="nativeCustomerSQL2", 
			query="SELECT c.customer_id_pk, c.customer_type, c.name, c.income FROM customer c WHERE c.name LIKE ?1", 
			hints={@QueryHint(name="javax.persistence.query.timeout", value="1000")},
			resultSetMapping="customerRSmapping")
})
@SqlResultSetMappings({
  @SqlResultSetMapping(name="customerRSmapping", 
			 entities =@EntityResult(entityClass=Customer.class, discriminatorColumn="CUSTOMER_TYPE")),
  @SqlResultSetMapping(name="orderRSmapping", 
		 entities =@EntityResult(entityClass=Order.class)),
  @SqlResultSetMapping(name="orderLineItemRSmapping", 
		 entities ={
  		   @EntityResult(entityClass=Order.class),
  		   @EntityResult(entityClass=LineItem.class) }),
  @SqlResultSetMapping(name="orderLineItemRSmapping2", 
  	 entities ={
  		  @EntityResult(entityClass=Order.class, 
  		  		fields={@FieldResult(name="orderId", column="ORDER_ID"),
  		  	          @FieldResult(name="price", column="ORDER_PRICE")} ),
  		  @EntityResult(entityClass=LineItem.class, 
  		  		fields={@FieldResult(name="lineItemId", column="LI_ID"),
                    @FieldResult(name="price", column="LI_PRICE")}) }),
  @SqlResultSetMapping(name="orderLineItemRSmapping3", 
     entities ={@EntityResult(entityClass=Order.class)},
     columns  ={@ColumnResult(name="LI_PRICE") } )
})
@ExcludeDefaultListeners
@ExcludeSuperclassListeners
@EntityListeners(jpatest.util.AuditingFieldListener.class)
public class Order extends BaseEntity {
	@Id
	@TableGenerator(name = "ORDER_SEQ_GEN", table = "SEQUENCE_GENERATOR_TB", pkColumnName = "SEQUENCE_NAME", valueColumnName = "SEQUENCE_VALUE", pkColumnValue = "ORDERS_SEQ")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ORDER_SEQ_GEN")
	@Column(name="ORDER_ID_PK", updatable=false)
	protected int orderId;

	protected double price;

	@Column(name="ORDER_TIME")
	protected Timestamp orderTime;	
	
	@Column(name="STATUS")
	@Enumerated(EnumType.ORDINAL)
	protected OrderStatus status;

	@ManyToOne(optional=false)
	@JoinColumn(name="CUSTOMER_ID_FK", referencedColumnName="CUSTOMER_ID_PK")
	protected  Customer customer;  // owning side of Order to Customer relationship

	@OneToMany(mappedBy="order", cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	@OrderColumn(name="LINE_ITEM_ID_PK")
	protected  List<LineItem> lineItems;

	private static final long serialVersionUID = 1L;

	public Order() {
		super();
	}

	public int getOrderId() {  // no setter, since PK cannot be changed.
		return this.orderId;
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Timestamp getOrderTime() {
		return this.orderTime;
	}

	public void setOrderTime(Timestamp orderTime) {
		this.orderTime = orderTime;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public List<LineItem> getLineItems() {
		return this.lineItems;
	}

	public void setLineItem(List<LineItem> lineItems) {
		this.lineItems = lineItems;
	}
	
	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

}
