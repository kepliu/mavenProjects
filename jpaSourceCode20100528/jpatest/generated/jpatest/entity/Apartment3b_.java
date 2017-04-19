package jpatest.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpatest.entity.Address3;
import jpatest.entity.Apartment3PK;

@Generated("EclipseLink-2.0.2.v20100323-r6872 @ Wed May 26 16:22:36 EDT 2010")
@StaticMetamodel(Apartment3b.class)
public class Apartment3b_ extends BaseEntity_ {

	public static volatile SingularAttribute<Apartment3b, Apartment3PK> aptId;
	public static volatile SingularAttribute<Apartment3b, Address3> address;
	public static volatile SingularAttribute<Apartment3b, Short> rooms;

}