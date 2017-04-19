package jpatest.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpatest.entity.Address;

@Generated("EclipseLink-2.0.2.v20100323-r6872 @ Wed May 26 16:22:36 EDT 2010")
@StaticMetamodel(House4b.class)
public class House4b_ extends BaseEntity_ {

	public static volatile SingularAttribute<House4b, Address> addr;
	public static volatile SingularAttribute<House4b, Integer> houseId;
	public static volatile SingularAttribute<House4b, Short> rooms;

}