package jpatest.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpatest.entity.Address2;
import jpatest.entity.Address3;
import jpatest.entity.BankInfo;
import jpatest.entity.CustomerType;

@Generated("EclipseLink-2.0.2.v20100323-r6872 @ Wed May 26 16:22:36 EDT 2010")
@StaticMetamodel(Customer5.class)
public class Customer5_ extends BaseEntity_ {

	public static volatile SingularAttribute<Customer5, byte[]> picture;
	public static volatile SingularAttribute<Customer5, Integer> customerId;
	public static volatile SingularAttribute<Customer5, CustomerType> customerType;
	public static volatile SingularAttribute<Customer5, Double> income;
	public static volatile SingularAttribute<Customer5, String> name;
	public static volatile SingularAttribute<Customer5, Address2> address2;
	public static volatile SingularAttribute<Customer5, BankInfo> bank;
	public static volatile SingularAttribute<Customer5, Address3> address3;

}