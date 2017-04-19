-- script to drop jpa database schema 

-- 1) CD to your Derby installation bin directory such as

--    cd C:\derby\bin

-- and then type ij at command prompt.

-- 2) Issue command to create db
--    ij> CONNECT 'jdbc:derby:jpatest;create=true;user=jpatest;password=jpatest';

-- 3) Load SQL script.

-- ij> run 'jpatest_drop.sql';



AUTOCOMMIT OFF;

DROP TABLE SEQUENCE_GENERATOR_TB;

DROP TABLE VEHICLE;

DROP TABLE BOOK_CATEGORY;

DROP TABLE CATEGORY;

DROP TABLE PREFERRED_CUSTOMER;
DROP TABLE GOLD_CUSTOMER;

DROP TABLE LINE_ITEM;

DROP TABLE CONTENT;

DROP TABLE BOOK;

DROP TABLE ORDERS;

DROP TABLE CUST_NICKNAME;
DROP TABLE CUST_BANK;

DROP TABLE CUST_PICTURE;
DROP TABLE CUSTOMER;

DROP TABLE CUSTOMER2;

DROP TABLE CUSTOMER3;
DROP TABLE PREFERRED_CUSTOMER3;
DROP TABLE GOLD_CUSTOMER3;

DROP TABLE APARTMENT;
DROP TABLE APARTMENT2;
DROP TABLE HOUSE;
DROP TABLE HOUSE2;

DROP TABLE ADDRESS;

DROP TABLE PREFERRED_CUSTOMER5;
DROP TABLE GOLD_CUSTOMER5;
DROP TABLE CUSTOMER5;

DROP TABLE ADDRESS2;



COMMIT;

























COMMIT;

 
