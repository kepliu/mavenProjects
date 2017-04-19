package jpatest.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpatest.entity.BankInfo;

@Generated("EclipseLink-2.0.2.v20100323-r6872 @ Wed May 26 16:22:36 EDT 2010")
@StaticMetamodel(Customer3.class)
public class Customer3_ extends User_ {

	public static volatile SingularAttribute<Customer3, Integer> customerId;
	public static volatile SingularAttribute<Customer3, BankInfo> bank;

}