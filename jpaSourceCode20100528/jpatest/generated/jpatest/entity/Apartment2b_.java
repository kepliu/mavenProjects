package jpatest.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpatest.entity.Address2;
import jpatest.entity.Apartment2PK;

@Generated("EclipseLink-2.0.2.v20100323-r6872 @ Wed May 26 16:22:36 EDT 2010")
@StaticMetamodel(Apartment2b.class)
public class Apartment2b_ extends BaseEntity_ {

	public static volatile SingularAttribute<Apartment2b, Apartment2PK> aptId;
	public static volatile SingularAttribute<Apartment2b, Address2> address;
	public static volatile SingularAttribute<Apartment2b, Short> rooms;

}