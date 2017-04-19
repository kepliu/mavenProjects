
set CLASSPATH=.;c:\jpa\eclipselink\jlib\eclipselink.jar
set CLASSPATH=%CLASSPATH%;c:\jpa\eclipselink\jlib\jpa\javax.persistence_2.0.0.jar
set CLASSPATH=%CLASSPATH%;c:\jpa\eclipselink\jlib\jpa\eclipselink-jpa-modelgen_2.0.2.jar
set CLASSPATH=%CLASSPATH%;c:\derby\lib\derbyclient.jar
set CLASSPATH=%CLASSPATH%;C:\jpa\hibernate-validator-4.0.2.GA\hibernate-validator-4.0.2.GA.jar
set CLASSPATH=%CLASSPATH%;C:\jpa\hibernate-validator-4.0.2.GA\lib\validation-api-1.0.0.GA.jar

@REM generate static metamodel class

javac -processor org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor -proc:only -s ../generated jpatest/entity/*.java


