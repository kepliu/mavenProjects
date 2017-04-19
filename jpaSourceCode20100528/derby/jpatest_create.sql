-- script to create jpa database schema 

-- 1) CD to your Derby installation bin directory such as

--    cd C:\derby\bin

-- and then type ij at command prompt.

-- 2) Issue the ij command to create db
--    ij> CONNECT 'jdbc:derby:jpatest;create=true;user=jpatest;password=jpatest';

-- 3) Load the SQL script under the ij command prompt.

--    ij> run 'jpatest_create.sql';

-- 4) To make the db available remotly, start server
--    Go to folder C:\derby\bin, type command: startNetworkServer.bat

-- NOTE:
-- Client driver: org.apache.derby.jdbc.ClientDriver
--                org.apache.derby.jdbc.ClientXADataSource
-- ConnectionURL: "jdbc:derby://localhost:1527/jpatest;"



AUTOCOMMIT OFF;
CREATE TABLE SEQUENCE_GENERATOR_TB
   (
      SEQUENCE_NAME VARCHAR(40) PRIMARY KEY, 
      SEQUENCE_VALUE INTEGER
   );

INSERT INTO SEQUENCE_GENERATOR_TB(SEQUENCE_NAME, SEQUENCE_VALUE)
   VALUES ('ADDRESS_SEQ', 1);

INSERT INTO SEQUENCE_GENERATOR_TB(SEQUENCE_NAME, SEQUENCE_VALUE)
   VALUES ('CUSTOMER_SEQ', 1);

INSERT INTO SEQUENCE_GENERATOR_TB(SEQUENCE_NAME, SEQUENCE_VALUE)
   VALUES ('ORDERS_SEQ', 1);

INSERT INTO SEQUENCE_GENERATOR_TB(SEQUENCE_NAME, SEQUENCE_VALUE)
   VALUES ('LINE_ITEM_SEQ', 1);

INSERT INTO SEQUENCE_GENERATOR_TB(SEQUENCE_NAME, SEQUENCE_VALUE)
   VALUES ('BOOK_SEQ', 1);

INSERT INTO SEQUENCE_GENERATOR_TB(SEQUENCE_NAME, SEQUENCE_VALUE)
   VALUES ('CATEGORY_SEQ', 1);


CREATE TABLE VEHICLE
   (
      VIN VARCHAR(17) PRIMARY KEY, 
      MAKE VARCHAR(40),
      MODEL VARCHAR(40),
      MODEL_YEAR INTEGER,
      VERSION INTEGER
   );


CREATE TABLE ADDRESS
   (
      ADDRESS_ID_PK INTEGER PRIMARY KEY, 
      STREET VARCHAR(40),
      CITY VARCHAR(40),
      STATE VARCHAR(40),
      ZIP VARCHAR(9),
      CREATE_USER VARCHAR(15),
      CREATE_TIME TIMESTAMP,
      UPDATE_USER VARCHAR(15),
      UPDATE_TIME TIMESTAMP,
      VERSION INTEGER
   );

CREATE TABLE CUSTOMER
   (
      CUSTOMER_ID_PK INTEGER PRIMARY KEY, 
      CUSTOMER_TYPE CHAR(1),
      NAME VARCHAR(40),
      ADDRESS_ID_FK INTEGER,
      PICTURE BLOB,
      INCOME DOUBLE,
      BANK_NAME VARCHAR(40),
      ACCOUNT_NUMBER VARCHAR(40),
      ROUTING_NUMBER VARCHAR(40),
      CREATE_USER VARCHAR(15),
      CREATE_TIME TIMESTAMP,
      UPDATE_USER VARCHAR(15),
      UPDATE_TIME TIMESTAMP,
      VERSION INTEGER  
   );

ALTER TABLE CUSTOMER
   ADD CONSTRAINT ADDRESS_FK Foreign Key (ADDRESS_ID_FK)
   REFERENCES ADDRESS(ADDRESS_ID_PK);

CREATE TABLE CUST_PICTURE
   (
      CUSTOMER_ID_FPK INTEGER PRIMARY KEY,     
      PICTURE BLOB,
      BOOK_CREATE_USER VARCHAR(15),
      BOOK_CREATE_TIME TIMESTAMP,
      BOOK_UPDATE_USER VARCHAR(15),
      BOOK_UPDATE_TIME TIMESTAMP,
      VERSION INTEGER
   );

ALTER TABLE CUST_PICTURE
   ADD CONSTRAINT CUST_PICTURE_FK Foreign Key (CUSTOMER_ID_FPK)
   REFERENCES CUSTOMER(CUSTOMER_ID_PK);


CREATE TABLE PREFERRED_CUSTOMER
   (
      CUSTOMER_ID_FPK INTEGER PRIMARY KEY, 
      DISCOUNT_RATE DOUBLE,
      EXPIRATION_DATE DATE,
      CREATE_USER VARCHAR(15),
      CREATE_TIME TIMESTAMP,
      UPDATE_USER VARCHAR(15),
      UPDATE_TIME TIMESTAMP,
      VERSION INTEGER  
   );

ALTER TABLE PREFERRED_CUSTOMER
   ADD CONSTRAINT PREF_CUSTOMER_FK Foreign Key (CUSTOMER_ID_FPK)
   REFERENCES CUSTOMER(CUSTOMER_ID_PK);


CREATE TABLE GOLD_CUSTOMER
   (
      CUSTOMER_ID_FPK INTEGER PRIMARY KEY, 
      CARD_NUMBER VARCHAR(25),
      CREDIT_LIMIT DOUBLE,
      CREATE_USER VARCHAR(15),
      CREATE_TIME TIMESTAMP,
      UPDATE_USER VARCHAR(15),
      UPDATE_TIME TIMESTAMP,
      VERSION INTEGER  
   );

ALTER TABLE GOLD_CUSTOMER
   ADD CONSTRAINT GOLD_CUSTOMER_FK Foreign Key (CUSTOMER_ID_FPK)
   REFERENCES CUSTOMER(CUSTOMER_ID_PK);

CREATE TABLE ORDERS
   (
      ORDER_ID_PK INTEGER PRIMARY KEY, 
      CUSTOMER_ID_FK INTEGER,
      PRICE DOUBLE,
      ORDER_TIME TIMESTAMP,
      STATUS INTEGER,
      CREATE_USER VARCHAR(15),
      CREATE_TIME TIMESTAMP,
      UPDATE_USER VARCHAR(15),
      UPDATE_TIME TIMESTAMP,
      VERSION INTEGER
   );

ALTER TABLE ORDERS
   ADD CONSTRAINT ORDER_FK Foreign Key (CUSTOMER_ID_FK)
   REFERENCES CUSTOMER(CUSTOMER_ID_PK);

CREATE TABLE LINE_ITEM
   (
      LINE_ITEM_ID_PK INTEGER PRIMARY KEY, 
      QUANTITY INTEGER,
      PRICE DOUBLE,
      BOOK_ID_FK INTEGER,
      ORDER_ID_FK INTEGER,
      CREATE_USER VARCHAR(15),
      CREATE_TIME TIMESTAMP,
      UPDATE_USER VARCHAR(15),
      UPDATE_TIME TIMESTAMP,
      VERSION INTEGER
   );

CREATE TABLE BOOK
   (
      BOOK_ID_PK INTEGER PRIMARY KEY, 
      ISBN VARCHAR(40),
      AUTHOR VARCHAR(40),
      TITLE VARCHAR(100),
      PRICE DOUBLE,
      RATING INTEGER,
      BOOK_CREATE_USER VARCHAR(15),
      BOOK_CREATE_TIME TIMESTAMP,
      BOOK_UPDATE_USER VARCHAR(15),
      BOOK_UPDATE_TIME TIMESTAMP,
      VERSION INTEGER
   );

CREATE TABLE CONTENT
   (
      BOOK_ID_FPK INTEGER PRIMARY KEY,      
      PDF BLOB,
      BOOK_CREATE_USER VARCHAR(15),
      BOOK_CREATE_TIME TIMESTAMP,
      BOOK_UPDATE_USER VARCHAR(15),
      BOOK_UPDATE_TIME TIMESTAMP,
      VERSION INTEGER
   );

ALTER TABLE CONTENT
   ADD CONSTRAINT CONTENT_FK Foreign Key (BOOK_ID_FPK)
   REFERENCES BOOK(BOOK_ID_PK);


ALTER TABLE LINE_ITEM
   ADD CONSTRAINT LINE_ITEM_FK Foreign Key (BOOK_ID_FK)
   REFERENCES BOOK(BOOK_ID_PK);

ALTER TABLE LINE_ITEM
   ADD CONSTRAINT LINE_ITEM_FK_2 Foreign Key (ORDER_ID_FK)
   REFERENCES ORDERS(ORDER_ID_PK);


CREATE TABLE CATEGORY
   (
      CATEGORY_ID_PK INTEGER PRIMARY KEY, 
      CATEGORY_NAME VARCHAR(100),
      PARENT_CATEGORY_FK INTEGER,
      CREATE_USER VARCHAR(15),
      CREATE_TIME TIMESTAMP,
      UPDATE_USER VARCHAR(15),
      UPDATE_TIME TIMESTAMP,
      VERSION INTEGER
   );

ALTER TABLE CATEGORY
   ADD CONSTRAINT CATEGORY_FK Foreign Key (PARENT_CATEGORY_FK)
   REFERENCES CATEGORY(CATEGORY_ID_PK);

CREATE TABLE BOOK_CATEGORY
   (
      BOOK_ID_FPK INTEGER NOT NULL,
      CATEGORY_ID_FPK INTEGER NOT NULL
   );

ALTER TABLE BOOK_CATEGORY
   ADD CONSTRAINT BOOK_CATEGORY_PK Primary Key (BOOK_ID_FPK, CATEGORY_ID_FPK);

ALTER TABLE BOOK_CATEGORY
   ADD CONSTRAINT BOOK_CATEGORY_FK Foreign Key (BOOK_ID_FPK)
   REFERENCES BOOK(BOOK_ID_PK);

ALTER TABLE BOOK_CATEGORY
   ADD CONSTRAINT BOOK_CATEGORY_FK_2 Foreign Key (CATEGORY_ID_FPK)
   REFERENCES CATEGORY(CATEGORY_ID_PK);

-- for composite primary key testing 

CREATE TABLE ADDRESS2
   (
      STREET_PK VARCHAR(80) NOT NULL,
      CITY VARCHAR(40),
      STATE VARCHAR(40),
      ZIP_PK VARCHAR(9)  NOT NULL,
      CREATE_USER VARCHAR(15),
      CREATE_TIME TIMESTAMP,
      UPDATE_USER VARCHAR(15),
      UPDATE_TIME TIMESTAMP,
      VERSION INTEGER
   );

ALTER TABLE ADDRESS2
   ADD CONSTRAINT ADDRESS2_PK Primary Key (STREET_PK, ZIP_PK);



-- for single table inheritance strategy

CREATE TABLE CUSTOMER2
   (
      CUSTOMER_ID_PK INTEGER PRIMARY KEY, 
      CUSTOMER_TYPE CHAR(1),
      NAME VARCHAR(40),
      ADDRESS_ID_FK INTEGER,
      PICTURE BLOB,
      INCOME DOUBLE,
      BANK_NAME VARCHAR(40),
      ACCOUNT_NUMBER VARCHAR(40),
      ROUTING_NUMBER VARCHAR(40),
      DISCOUNT_RATE DOUBLE,
      EXPIRATION_DATE DATE,
      CARD_NUMBER VARCHAR(25), 
      CREDIT_LIMIT DOUBLE,         
      CREATE_USER VARCHAR(15),
      CREATE_TIME TIMESTAMP,
      UPDATE_USER VARCHAR(15),
      UPDATE_TIME TIMESTAMP,
      VERSION INTEGER  
   );

ALTER TABLE CUSTOMER2
   ADD CONSTRAINT ADDRESS_FK2 Foreign Key (ADDRESS_ID_FK)
   REFERENCES ADDRESS(ADDRESS_ID_PK);


-- for table per class inheritance strategy

CREATE TABLE CUSTOMER3
   (
      CUSTOMER_ID_PK INTEGER PRIMARY KEY, 
      NAME VARCHAR(40),
      ADDRESS_ID_FK INTEGER,
      PICTURE BLOB,
      INCOME DOUBLE,
      BANK_NAME VARCHAR(40),
      ACCOUNT_NUMBER VARCHAR(40),
      ROUTING_NUMBER VARCHAR(40),
      DISCOUNT_RATE DOUBLE,
      EXPIRATION_DATE DATE,
      CARD_NUMBER VARCHAR(25), 
      CREDIT_LIMIT DOUBLE,         
      CREATE_USER VARCHAR(15),
      CREATE_TIME TIMESTAMP,
      UPDATE_USER VARCHAR(15),
      UPDATE_TIME TIMESTAMP,
      VERSION INTEGER  
   );

ALTER TABLE CUSTOMER3
   ADD CONSTRAINT ADDRESS_FK3 Foreign Key (ADDRESS_ID_FK)
   REFERENCES ADDRESS(ADDRESS_ID_PK);

CREATE TABLE PREFERRED_CUSTOMER3
   (
      CUSTOMER_ID_PK INTEGER PRIMARY KEY,
      NAME VARCHAR(40),
      ADDRESS_ID_FK INTEGER,
      PICTURE BLOB,
      INCOME DOUBLE,
      BANK_NAME VARCHAR(40),
      ACCOUNT_NUMBER VARCHAR(40),
      ROUTING_NUMBER VARCHAR(40),
      DISCOUNT_RATE DOUBLE,
      EXPIRATION_DATE DATE,
      CREATE_USER VARCHAR(15),
      CREATE_TIME TIMESTAMP,
      UPDATE_USER VARCHAR(15),
      UPDATE_TIME TIMESTAMP,
      VERSION INTEGER  
   );

ALTER TABLE PREFERRED_CUSTOMER3
   ADD CONSTRAINT PREF_CUSTOMER_ADDRESS_FK Foreign Key (ADDRESS_ID_FK)
   REFERENCES ADDRESS(ADDRESS_ID_PK);


CREATE TABLE GOLD_CUSTOMER3
   (
      CUSTOMER_ID_PK INTEGER PRIMARY KEY,
      NAME VARCHAR(40),
      ADDRESS_ID_FK INTEGER,
      PICTURE BLOB,
      INCOME DOUBLE,
      BANK_NAME VARCHAR(40),
      ACCOUNT_NUMBER VARCHAR(40),
      ROUTING_NUMBER VARCHAR(40),
      CARD_NUMBER VARCHAR(25),
      CREDIT_LIMIT DOUBLE,
      CREATE_USER VARCHAR(15),
      CREATE_TIME TIMESTAMP,
      UPDATE_USER VARCHAR(15),
      UPDATE_TIME TIMESTAMP,
      VERSION INTEGER  
   );

ALTER TABLE GOLD_CUSTOMER3
   ADD CONSTRAINT GOLD_CUSTOMER_ADDRESS_FK Foreign Key (ADDRESS_ID_FK)
   REFERENCES ADDRESS(ADDRESS_ID_PK);


-- for composite foreign key to address2 using joined table inheritance strategy

CREATE TABLE CUSTOMER5
   (
      CUSTOMER_ID_PK INTEGER PRIMARY KEY, 
      CUSTOMER_TYPE CHAR(1),
      NAME VARCHAR(40),
      STREET_FK VARCHAR(80),
      ZIP_FK VARCHAR(9),
      PICTURE BLOB,
      INCOME DOUBLE,
      BANK_NAME VARCHAR(40),
      ACCOUNT_NUMBER VARCHAR(40),
      ROUTING_NUMBER VARCHAR(40),
      CREATE_USER VARCHAR(15),
      CREATE_TIME TIMESTAMP,
      UPDATE_USER VARCHAR(15),
      UPDATE_TIME TIMESTAMP,
      VERSION INTEGER  
   );

ALTER TABLE CUSTOMER5
   ADD CONSTRAINT ADDRESS2_FK5 Foreign Key (STREET_FK, ZIP_FK)
   REFERENCES ADDRESS2(STREET_PK, ZIP_PK);


CREATE TABLE PREFERRED_CUSTOMER5
   (
      CUSTOMER_ID_FPK INTEGER PRIMARY KEY, 
      DISCOUNT_RATE DOUBLE,
      EXPIRATION_DATE DATE,
      CREATE_USER VARCHAR(15),
      CREATE_TIME TIMESTAMP,
      UPDATE_USER VARCHAR(15),
      UPDATE_TIME TIMESTAMP,
      VERSION INTEGER  
   );

ALTER TABLE PREFERRED_CUSTOMER5
   ADD CONSTRAINT PREF_CUSTOMER5_FK Foreign Key (CUSTOMER_ID_FPK)
   REFERENCES CUSTOMER5(CUSTOMER_ID_PK);


CREATE TABLE GOLD_CUSTOMER5
   (
      CUSTOMER_ID_FPK INTEGER PRIMARY KEY, 
      CARD_NUMBER VARCHAR(25),
      CREDIT_LIMIT DOUBLE,
      CREATE_USER VARCHAR(15),
      CREATE_TIME TIMESTAMP,
      UPDATE_USER VARCHAR(15),
      UPDATE_TIME TIMESTAMP,
      VERSION INTEGER  
   );

ALTER TABLE GOLD_CUSTOMER5
   ADD CONSTRAINT GOLD_CUSTOMER5_FK Foreign Key (CUSTOMER_ID_FPK)
   REFERENCES CUSTOMER5(CUSTOMER_ID_PK);



-- for element collection testing 


CREATE TABLE CUST_NICKNAME
   (
      CUST_ID_FPK INTEGER NOT NULL, 
      NICKNAMES VARCHAR(80) NOT NULL,
      CREATE_USER VARCHAR(15),
      CREATE_TIME TIMESTAMP,
      UPDATE_USER VARCHAR(15),
      UPDATE_TIME TIMESTAMP,
      VERSION INTEGER  
   );

ALTER TABLE CUST_NICKNAME
   ADD CONSTRAINT CUST_NICKNAME_PK Primary Key (CUST_ID_FPK, NICKNAMES);

ALTER TABLE CUST_NICKNAME
   ADD CONSTRAINT CUST_NICKNAME_FK Foreign Key (CUST_ID_FPK)
   REFERENCES CUSTOMER(CUSTOMER_ID_PK);

CREATE TABLE CUST_BANK
   (
      CUST_ID_FPK INTEGER NOT NULL, 
      BANK_NAME_X VARCHAR(80) NOT NULL,
      BANK_ACCOUNT_NUMBER VARCHAR(80),
      BANK_ROUTING_NUMBER VARCHAR(80),
      CREATE_USER VARCHAR(15),
      CREATE_TIME TIMESTAMP,
      UPDATE_USER VARCHAR(15),
      UPDATE_TIME TIMESTAMP,
      VERSION INTEGER  
   );

ALTER TABLE CUST_BANK
   ADD CONSTRAINT CUST_BANK_PK Primary Key (CUST_ID_FPK, BANK_NAME_X);

ALTER TABLE CUST_BANK
   ADD CONSTRAINT CUST_BANK_FK Foreign Key (CUST_ID_FPK)
   REFERENCES CUSTOMER(CUSTOMER_ID_PK);


-- for derived identity testing 

CREATE TABLE APARTMENT
   (
      APT_NUM_PK SMALLINT NOT NULL, 
      ADDRESS_ID_FPK INTEGER NOT NULL,
      NUM_ROOMS SMALLINT,
      CREATE_USER VARCHAR(15),
      CREATE_TIME TIMESTAMP,
      UPDATE_USER VARCHAR(15),
      UPDATE_TIME TIMESTAMP,
      VERSION INTEGER  
   );

ALTER TABLE APARTMENT
   ADD CONSTRAINT APARTMENT_PK Primary Key (APT_NUM_PK, ADDRESS_ID_FPK);

ALTER TABLE APARTMENT
   ADD CONSTRAINT APARTMENT_FK Foreign Key (ADDRESS_ID_FPK)
   REFERENCES ADDRESS(ADDRESS_ID_PK);


CREATE TABLE APARTMENT2
   (
      APT_NUM_PK SMALLINT NOT NULL, 
      STREET_FPK VARCHAR(80) NOT NULL,
      ZIP_FPK VARCHAR(9) NOT NULL,
      NUM_ROOMS SMALLINT,
      CREATE_USER VARCHAR(15),
      CREATE_TIME TIMESTAMP,
      UPDATE_USER VARCHAR(15),
      UPDATE_TIME TIMESTAMP,
      VERSION INTEGER  
   );

ALTER TABLE APARTMENT2
   ADD CONSTRAINT APARTMENT2_PK Primary Key (APT_NUM_PK, STREET_FPK, ZIP_FPK);

ALTER TABLE APARTMENT2
   ADD CONSTRAINT APARTMENT2_FK Foreign Key (STREET_FPK, ZIP_FPK)
   REFERENCES ADDRESS2(STREET_PK, ZIP_PK);

CREATE TABLE HOUSE
   (
      ADDRESS_ID_FPK INTEGER PRIMARY KEY,
      NUM_ROOMS SMALLINT,
      CREATE_USER VARCHAR(15),
      CREATE_TIME TIMESTAMP,
      UPDATE_USER VARCHAR(15),
      UPDATE_TIME TIMESTAMP,
      VERSION INTEGER  
   );

ALTER TABLE HOUSE
   ADD CONSTRAINT HOUSE_FK Foreign Key (ADDRESS_ID_FPK )
   REFERENCES ADDRESS(ADDRESS_ID_PK);


CREATE TABLE HOUSE2
   (
      STREET_FPK VARCHAR(80) NOT NULL,
      ZIP_FPK VARCHAR(9) NOT NULL,
      NUM_ROOMS SMALLINT,
      CREATE_USER VARCHAR(15),
      CREATE_TIME TIMESTAMP,
      UPDATE_USER VARCHAR(15),
      UPDATE_TIME TIMESTAMP,
      VERSION INTEGER  
   );

ALTER TABLE HOUSE2
   ADD CONSTRAINT HOUSE2_PK Primary Key (STREET_FPK, ZIP_FPK);

ALTER TABLE HOUSE2
   ADD CONSTRAINT HOUSE2_FK Foreign Key (STREET_FPK, ZIP_FPK)
   REFERENCES ADDRESS2(STREET_PK, ZIP_PK);


COMMIT;
 