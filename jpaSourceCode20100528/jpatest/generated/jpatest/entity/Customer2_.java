package jpatest.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpatest.entity.BankInfo;
import jpatest.entity.CustomerType;

@Generated("EclipseLink-2.0.2.v20100323-r6872 @ Wed May 26 16:22:36 EDT 2010")
@StaticMetamodel(Customer2.class)
public class Customer2_ extends User_ {

	public static volatile SingularAttribute<Customer2, Integer> customerId;
	public static volatile SingularAttribute<Customer2, CustomerType> customerType;
	public static volatile SingularAttribute<Customer2, BankInfo> bank;

}