package jpatest.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpatest.entity.Address;
import jpatest.entity.Apartment1PK;

@Generated("EclipseLink-2.0.2.v20100323-r6872 @ Wed May 26 16:22:36 EDT 2010")
@StaticMetamodel(Apartment1b.class)
public class Apartment1b_ extends BaseEntity_ {

	public static volatile SingularAttribute<Apartment1b, Apartment1PK> aptId;
	public static volatile SingularAttribute<Apartment1b, Address> address;
	public static volatile SingularAttribute<Apartment1b, Short> rooms;

}