@Rem file name: run.bat for compiling and running the test program


@Rem set CLASSPATH=.;%CLASSPATH%;c:\jpa\eclipselink\jlib\eclipselink.jar
@Rem set CLASSPATH=%CLASSPATH%;c:\jpa\eclipselink\jlib\jpa\javax.persistence_2.0.0.jar
@Rem set CLASSPATH=%CLASSPATH%;c:\jpa\eclipselink\jlib\jpa\eclipselink-jpa-modelgen_2.0.0.jar
@Rem set CLASSPATH=%CLASSPATH%;c:\derby\lib\derbyclient.jar
set CLASSPATH=.;%CLASSPATH%;C:\Users\liuka\jurnalDev\jpaSourceCode20100528\DOS\lib\eclipselink.jar
set CLASSPATH=%CLASSPATH%;C:\Users\liuka\jurnalDev\jpaSourceCode20100528\DOS\lib\javax.persistence_2.1.0.v201304241213.jar
set CLASSPATH=%CLASSPATH%;C:\Users\liuka\jurnalDev\jpaSourceCode20100528\DOS\lib\derbyclient.jar
del jpatest\*.class

@REM generate static metamodel class
javac -processor org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor  jpatest/Vehicle.java

javac jpatest/Vehicle.java jpatest/VehicleTest.java

java  -ea jpatest/VehicleTest
