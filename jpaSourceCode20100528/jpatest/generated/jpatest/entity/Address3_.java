package jpatest.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpatest.entity.Address3PK;

@Generated("EclipseLink-2.0.2.v20100323-r6872 @ Wed May 26 16:22:36 EDT 2010")
@StaticMetamodel(Address3.class)
public class Address3_ extends BaseEntity_ {

	public static volatile SingularAttribute<Address3, String> state;
	public static volatile SingularAttribute<Address3, Address3PK> pk;
	public static volatile SingularAttribute<Address3, String> city;

}