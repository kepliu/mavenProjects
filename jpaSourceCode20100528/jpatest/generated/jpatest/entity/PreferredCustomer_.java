package jpatest.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated("EclipseLink-2.0.2.v20100323-r6872 @ Wed May 26 16:22:36 EDT 2010")
@StaticMetamodel(PreferredCustomer.class)
public class PreferredCustomer_ extends Customer_ {

	public static volatile SingularAttribute<PreferredCustomer, Date> expirationDate;
	public static volatile SingularAttribute<PreferredCustomer, Double> discountRate;

}