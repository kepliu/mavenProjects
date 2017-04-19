package jpatest.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpatest.entity.Address3;

@Generated("EclipseLink-2.0.2.v20100323-r6872 @ Wed May 26 16:22:36 EDT 2010")
@StaticMetamodel(Apartment3a.class)
public class Apartment3a_ extends BaseEntity_ {

	public static volatile SingularAttribute<Apartment3a, Address3> addrId;
	public static volatile SingularAttribute<Apartment3a, Short> aptNumber;
	public static volatile SingularAttribute<Apartment3a, Short> rooms;

}