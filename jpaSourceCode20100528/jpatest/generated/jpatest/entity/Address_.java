package jpatest.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpatest.entity.Customer;

@Generated("EclipseLink-2.0.2.v20100323-r6872 @ Wed May 26 16:22:36 EDT 2010")
@StaticMetamodel(Address.class)
public class Address_ extends BaseEntity_ {

	public static volatile SingularAttribute<Address, String> zip;
	public static volatile SingularAttribute<Address, String> street;
	public static volatile SingularAttribute<Address, String> state;
	public static volatile SingularAttribute<Address, Customer> customer;
	public static volatile SingularAttribute<Address, Integer> addressId;
	public static volatile SingularAttribute<Address, String> city;

}