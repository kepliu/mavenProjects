package jpatest.test;

import junit.framework.Test;
import junit.framework.TestSuite;


public class JpaTestSuite {
	
	public static Test suite() {

        TestSuite suite = new TestSuite();
   
        suite.addTestSuite(AddressTest.class);
        suite.addTestSuite(BookCategoryTest.class);
        suite.addTestSuite(CustomerEAOTest.class);
        suite.addTestSuite(CustomerEAO23Test.class);
        suite.addTestSuite(OrderTest.class);
        suite.addTestSuite(VehicleEAOTest.class);
        suite.addTestSuite(MiscellaneousTest.class);
        suite.addTestSuite(QueryTest.class); 
        suite.addTestSuite(DerivedIdentityTest.class);
        suite.addTestSuite(MetamodelTest.class);
        suite.addTestSuite(CriteriaQueryTest.class);
        
        // add a suite
        //suite.addTest(AnotherTestSuite.suite());

        return suite;
    }

    /**
     * Runs the test suite using the textual runner.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }


}
