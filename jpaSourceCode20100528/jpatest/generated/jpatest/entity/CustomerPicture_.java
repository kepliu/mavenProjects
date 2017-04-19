package jpatest.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpatest.entity.Customer;

@Generated("EclipseLink-2.0.2.v20100323-r6872 @ Wed May 26 16:22:36 EDT 2010")
@StaticMetamodel(CustomerPicture.class)
public class CustomerPicture_ { 

	public static volatile SingularAttribute<CustomerPicture, byte[]> picture;
	public static volatile SingularAttribute<CustomerPicture, Integer> customerId;
	public static volatile SingularAttribute<CustomerPicture, Customer> customer;

}