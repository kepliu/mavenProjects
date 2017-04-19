package jpatest.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.TableGenerator;

@Entity
@TableGenerator(name = "CATEG_SEQ_GEN", table = "SEQUENCE_GENERATOR_TB", pkColumnName = "SEQUENCE_NAME", valueColumnName = "SEQUENCE_VALUE", pkColumnValue = "CATEGORY_SEQ")
public class Category extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CATEG_SEQ_GEN")
	@Column(name="CATEGORY_ID_PK")
	protected int categoryId;

	@Column(name="CATEGORY_NAME")
	protected String categoryName;

	@ManyToOne
	@JoinColumn(name="PARENT_CATEGORY_FK", referencedColumnName="CATEGORY_ID_PK")
	protected Category parentCategory;

	@OneToMany(mappedBy="parentCategory")
	protected Set<Category> childCategories;

	@ManyToMany(mappedBy="categories")
	@OrderBy("title ASC, price DESC")
	protected List<Book> books;

	private static final long serialVersionUID = 1L;

	public Category() {
		super();
	}

	public int getCategoryId() {
		return this.categoryId;
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Category getParentCategory() {
		return this.parentCategory;
	}

	public void setParentCategory(Category parentCategory) {
		this.parentCategory = parentCategory;
	}

	public Set<Category> getChildCategories() {
		return this.childCategories;
	}

	public void setChildCategories(Set<Category> childCategories) {
		this.childCategories = childCategories;
	}

	public List<Book> getBooks() {
		return this.books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

}
