package jpatest.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpatest.entity.Address;

@Generated("EclipseLink-2.0.2.v20100323-r6872 @ Wed May 26 16:22:36 EDT 2010")
@StaticMetamodel(User.class)
public class User_ extends BaseEntity_ {

	public static volatile SingularAttribute<User, byte[]> picture;
	public static volatile SingularAttribute<User, Address> address;
	public static volatile SingularAttribute<User, Double> income;
	public static volatile SingularAttribute<User, String> name;

}