package jpatest.test;

import java.util.Set;

import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Type;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.PluralAttribute.CollectionType;

import org.junit.Test;

import jpatest.entity.LineItem;
import jpatest.entity.Order;


public class MetamodelTest extends MyJpaTestCase {
	@Test
	public void testMetamodel() {
		Metamodel mm = em.getMetamodel();
		EntityType<Order> orderMM = mm.entity(Order.class);

		// Get ID type for this entity: Integer
		Type<?> idType = orderMM.getIdType();
		System.out.println("id type = " + idType.getJavaType());

		SingularAttribute<? super Order, Double> price = orderMM
				.getSingularAttribute("price", Double.class);

		// Persistence attribute type for "price": BASIC
		String priceAttibuteName = price.getPersistentAttributeType().name();
		System.out.println("price attibute type name: " + priceAttibuteName);

		// Declaring class Java type: jpatest.entity.Order
		Class c = price.getDeclaringType().getJavaType();
		System.out.println("price attibute declaring Java type: " + c);

		// Get the version attribute
		SingularAttribute<? super Order, Integer> version = orderMM
				.getVersion(int.class);

		// version
		System.out.println("version attibute name: " + version.getName());

		// Get the plural attribute for "lineItems"
		//MapAttribute<? super Order, Integer, LineItem> lineItems = orderMM
		//		.getMap("lineItems", Integer.class, LineItem.class);
		
		ListAttribute<? super Order, LineItem> lineItems = orderMM.getList("lineItems", LineItem.class);

		// PLURAL_ATTRIBUTE
		System.out.println("line item attibute BindableType: "
				+ lineItems.getBindableType());

		// Get the set of singular attributes for the Order entity
		Set<SingularAttribute<? super Order, ?>> sa = orderMM
				.getSingularAttributes();
		for (SingularAttribute<? super Order, ?> s : sa) {
			String attributeName = s.getName();
			// printout: price, customer,
			System.out.println("singular attibute name = " + attributeName);

			Boolean isId = s.isId();
			System.out.println("singular attibute is Id: " + isId);

			Boolean isVersion = s.isVersion();
			System.out.println("singular attibute is version: " + isVersion);

			PersistentAttributeType ap = s.getPersistentAttributeType();

			// printout: BASIC, ONE_TO_MANY, EMBEDDED, ELEMENT_COLLECTION
			System.out.println("singular attibute type = " + ap.name());

			ManagedType<? super Order> amt = s.getDeclaringType();

			// printout: jpatest.entity.Order
			System.out.println("singular managed type = " + amt.getJavaType());
		}

		// Get the set of plural attributes (eg, Set, Map) for the Order entity
		Set<PluralAttribute<? super Order, ?, ?>> pa = orderMM
				.getPluralAttributes();
		for (PluralAttribute<? super Order, ?, ?> p : pa) {
			CollectionType ct = p.getCollectionType();
			System.out.println("plural attribute collection type: "
					+ ct.getDeclaringClass());

			Type<?> elemType = p.getElementType();

			// jpatest.entity.LineItem
			System.out.println("plural attribute elemType = "
					+ elemType.getJavaType());
		}

		// Get the set of all attributes for the Order entity
		Set<Attribute<? super Order, ?>> attributes = orderMM.getAttributes();
		for (Attribute<? super Order, ?> a : attributes) {
			String attributeName = a.getName();
			// printout: price, customer, lineItems
			System.out.println("attibute name = " + attributeName);

			Boolean isAssociation = a.isAssociation();
			System.out.println("attibute is an association: " + isAssociation);

			Boolean isCollection = a.isCollection();
			System.out.println("attibute is a Collection: " + isCollection);

			PersistentAttributeType ap = a.getPersistentAttributeType();

			// printout: BASIC, ONE_TO_MANY, EMBEDDED, ELEMENT_COLLECTION
			System.out.println("attibute type = " + ap.name());

			ManagedType<? super Order> amt = a.getDeclaringType();

			// printout: jpatest.entity.Order
			System.out.println("managed type = " + amt.getJavaType());
		}

	}

}
