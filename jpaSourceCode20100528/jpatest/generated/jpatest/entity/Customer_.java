package jpatest.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpatest.entity.BankInfo;
import jpatest.entity.CustomerPicture;
import jpatest.entity.CustomerType;
import jpatest.entity.Order;

@Generated("EclipseLink-2.0.2.v20100323-r6872 @ Wed May 26 16:22:36 EDT 2010")
@StaticMetamodel(Customer.class)
public class Customer_ extends User_ {

	public static volatile SingularAttribute<Customer, Integer> customerId;
	public static volatile SingularAttribute<Customer, CustomerType> customerType;
	public static volatile ListAttribute<Customer, BankInfo> bankInfos;
	public static volatile SingularAttribute<Customer, CustomerPicture> custPicture;
	public static volatile SingularAttribute<Customer, BankInfo> bank;
	public static volatile MapAttribute<Customer, Integer, Order> orders;
	public static volatile SetAttribute<Customer, String> nickNames;

}