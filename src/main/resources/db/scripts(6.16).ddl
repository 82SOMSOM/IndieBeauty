
ALTER TABLE ProductImage
DROP CONSTRAINT R_37;

ALTER TABLE JoinEvent
DROP CONSTRAINT R_27;

ALTER TABLE JoinEvent
DROP CONSTRAINT R_28;

ALTER TABLE SellerEvents
DROP CONSTRAINT R_13;

ALTER TABLE Item
DROP CONSTRAINT R_4;

ALTER TABLE Item
DROP CONSTRAINT R_6;

ALTER TABLE Orders
DROP CONSTRAINT R_30;

ALTER TABLE Favorites
DROP CONSTRAINT R_31;

ALTER TABLE Favorites
DROP CONSTRAINT R_33;

ALTER TABLE Review
DROP CONSTRAINT R_9;

ALTER TABLE Review
DROP CONSTRAINT R_14;

ALTER TABLE Product
DROP CONSTRAINT R_10;

ALTER TABLE ProductImage
DROP PRIMARY KEY CASCADE  DROP INDEX;

ALTER TABLE JoinEvent
DROP PRIMARY KEY CASCADE  DROP INDEX;

ALTER TABLE SellerEvents
DROP PRIMARY KEY CASCADE  DROP INDEX;

ALTER TABLE Item
DROP PRIMARY KEY CASCADE  DROP INDEX;

ALTER TABLE Orders
DROP PRIMARY KEY CASCADE  DROP INDEX;

ALTER TABLE Favorites
DROP PRIMARY KEY CASCADE  DROP INDEX;

ALTER TABLE SellerInfo
DROP PRIMARY KEY CASCADE  DROP INDEX;

ALTER TABLE Review
DROP PRIMARY KEY CASCADE  DROP INDEX;

ALTER TABLE UserInfo
DROP PRIMARY KEY CASCADE  DROP INDEX;

ALTER TABLE Product
DROP PRIMARY KEY CASCADE  DROP INDEX;

ALTER TABLE Category
DROP PRIMARY KEY CASCADE  DROP INDEX;

DROP INDEX XPKProductImage;

DROP INDEX XIF1ProductImage;

DROP TABLE ProductImage CASCADE CONSTRAINTS PURGE;

DROP INDEX XIF1JoinEvent;

DROP INDEX XIF2JoinEvent;

DROP INDEX XPKJoinEvent;

DROP TABLE JoinEvent CASCADE CONSTRAINTS PURGE;

DROP INDEX XIF1SellerEvents;

DROP INDEX XPKSellerEvents;

DROP TABLE SellerEvents CASCADE CONSTRAINTS PURGE;

DROP INDEX XIF1Item;

DROP INDEX XIF2Item;

DROP INDEX XPKItem;

DROP TABLE Item CASCADE CONSTRAINTS PURGE;

DROP INDEX XIF1Order;

DROP INDEX XPKOrder;

DROP TABLE Orders CASCADE CONSTRAINTS PURGE;

DROP INDEX XIF2Favorites;

DROP INDEX XIF3Favorites;

DROP INDEX XPKFavorites;

DROP TABLE Favorites CASCADE CONSTRAINTS PURGE;

DROP INDEX XPKSellerInfo;

DROP TABLE SellerInfo CASCADE CONSTRAINTS PURGE;

DROP INDEX XIF1Review;

DROP INDEX XIF2Review;

DROP INDEX XPKReview;

DROP TABLE Review CASCADE CONSTRAINTS PURGE;

DROP INDEX XPKUserInfo;

DROP TABLE UserInfo CASCADE CONSTRAINTS PURGE;

DROP INDEX XIF1Product;

DROP INDEX XPKProduct;

DROP TABLE Product CASCADE CONSTRAINTS PURGE;

DROP INDEX XPKCategory;

DROP TABLE Category CASCADE CONSTRAINTS PURGE;

CREATE TABLE Category
(
	categoryId           INTEGER  NOT NULL ,
	name                 VARCHAR2(15)  NOT NULL 
);

CREATE UNIQUE INDEX XPKCategory ON Category
(categoryId   ASC);

ALTER TABLE Category
	ADD CONSTRAINT  XPKCategory PRIMARY KEY (categoryId);

CREATE TABLE Orders
(
	orderId              INTEGER  NOT NULL ,
	userid               VARCHAR2(20)  NULL ,
	orderDate            DATE  NOT NULL ,
	totalPrice           INTEGER  NOT NULL ,
	isOrderPaid          INTEGER  NOT NULL ,
	accountNumber        INTEGER  NOT NULL ,
	address              VARCHAR2(100)  NOT NULL 
);

CREATE UNIQUE INDEX XPKOrder ON Orders
(orderId   ASC);

ALTER TABLE Orders
	ADD CONSTRAINT  XPKOrder PRIMARY KEY (orderId);

CREATE TABLE Product
(
	productId            INTEGER  NOT NULL ,
	categoryId           INTEGER  NOT NULL ,
	pname                VARCHAR2(20)  NOT NULL ,
	pdescription         VARCHAR2(500)  NULL ,
	price                INTEGER  NOT NULL ,
	stock                INTEGER  NOT NULL ,
	udate                DATE  NOT NULL 
);

CREATE UNIQUE INDEX XPKProduct ON Product
(productId   ASC);

ALTER TABLE Product
	ADD CONSTRAINT  XPKProduct PRIMARY KEY (productId);

CREATE TABLE ProductImage
(
	imageId              CHAR(18)  NOT NULL ,
	productId            INTEGER  NULL ,
	imageUrl             VARCHAR2(200)  NOT NULL ,
	isTitleImg           INT  NOT NULL 
);

CREATE UNIQUE INDEX XPKProductImage ON ProductImage
(imageId   ASC);

ALTER TABLE ProductImage
	ADD CONSTRAINT  XPKProductImage PRIMARY KEY (imageId);

CREATE TABLE Item
(
	itemId               INTEGER  NOT NULL ,
	productId            INTEGER  NOT NULL ,
	price                INTEGER  NOT NULL ,
	quantity             INTEGER  NOT NULL ,
	orderId              INTEGER  NOT NULL 
);

CREATE UNIQUE INDEX XPKItem ON Item
(itemId   ASC);

ALTER TABLE Item
	ADD CONSTRAINT  XPKItem PRIMARY KEY (itemId);

CREATE TABLE SellerEvents
(
	eventId              INTEGER  NOT NULL ,
	sellerid             VARCHAR2(20)  NOT NULL ,
	title                VARCHAR2(100)  NOT NULL ,
	content              VARCHAR2(300)  NOT NULL ,
	eventImage           VARCHAR2(20)  NULL ,
	eventDate            DATE  NOT NULL ,
	joinCount            INTEGER  NOT NULL 
);

CREATE UNIQUE INDEX XPKSellerEvents ON SellerEvents
(eventId   ASC);

ALTER TABLE SellerEvents
	ADD CONSTRAINT  XPKSellerEvents PRIMARY KEY (eventId);

CREATE TABLE SellerInfo
(
	sellerid             VARCHAR2(20)  NOT NULL ,
	sellername           VARCHAR2(20)  NOT NULL ,
	passwd               VARCHAR2(20)  NOT NULL ,
	phone                NUMBER(11)  NOT NULL ,
	business             VARCHAR2(20)  NULL 
);

CREATE UNIQUE INDEX XPKSellerInfo ON SellerInfo
(sellerid   ASC);

ALTER TABLE SellerInfo
	ADD CONSTRAINT  XPKSellerInfo PRIMARY KEY (sellerid);

CREATE TABLE UserInfo
(
	userid               VARCHAR2(20)  NOT NULL ,
	username             VARCHAR2(20)  NOT NULL ,
	passwd               VARCHAR2(20)  NOT NULL ,
	email                VARCHAR2(30)  NOT NULL ,
	phone                NUMBER(11)  NOT NULL ,
	address              VARCHAR2(100)  NOT NULL 
);

CREATE UNIQUE INDEX XPKUserInfo ON UserInfo
(userid   ASC);

ALTER TABLE UserInfo
	ADD CONSTRAINT  XPKUserInfo PRIMARY KEY (userid);

CREATE TABLE JoinEvent
(
	userid               VARCHAR2(20)  NOT NULL ,
	eventid              INTEGER  NOT NULL 
);

CREATE UNIQUE INDEX XPKJoinEvent ON JoinEvent
(userid   ASC,eventid   ASC);

ALTER TABLE JoinEvent
	ADD CONSTRAINT  XPKJoinEvent PRIMARY KEY (userid,eventid);

CREATE TABLE Favorites
(
	favid                VARCHAR2(20)  NOT NULL ,
	sellerid             VARCHAR2(20)  NOT NULL ,
	userid               VARCHAR2(20)  NOT NULL 
);

CREATE UNIQUE INDEX XPKFavorites ON Favorites
(favid   ASC,userid   ASC);

ALTER TABLE Favorites
	ADD CONSTRAINT  XPKFavorites PRIMARY KEY (favid,userid);

CREATE TABLE Review
(
	reviewId             INTEGER  NOT NULL ,
	userid               VARCHAR2(20)  NOT NULL ,
	reviewDate           DATE  NULL ,
	content              VARCHAR2(20)  NULL ,
	image                VARCHAR2(20)  NULL ,
	star                 INTEGER  NULL ,
	productId            INTEGER  NOT NULL 
);

CREATE UNIQUE INDEX XPKReview ON Review
(reviewId   ASC);

ALTER TABLE Review
	ADD CONSTRAINT  XPKReview PRIMARY KEY (reviewId);

ALTER TABLE Orders
	ADD (
CONSTRAINT R_30 FOREIGN KEY (userid) REFERENCES UserInfo (userid) ON DELETE SET NULL);

ALTER TABLE Product
	ADD (
CONSTRAINT R_10 FOREIGN KEY (categoryId) REFERENCES Category (categoryId));

ALTER TABLE ProductImage
	ADD (
CONSTRAINT R_37 FOREIGN KEY (productId) REFERENCES Product (productId) ON DELETE SET NULL);

ALTER TABLE Item
	ADD (
CONSTRAINT R_4 FOREIGN KEY (productId) REFERENCES Product (productId));

ALTER TABLE Item
	ADD (
CONSTRAINT R_6 FOREIGN KEY (orderId) REFERENCES Orders (orderId));

ALTER TABLE SellerEvents
	ADD (
CONSTRAINT R_13 FOREIGN KEY (sellerid) REFERENCES SellerInfo (sellerid));

ALTER TABLE JoinEvent
	ADD (
CONSTRAINT R_27 FOREIGN KEY (userid) REFERENCES UserInfo (userid));

ALTER TABLE JoinEvent
	ADD (
CONSTRAINT R_28 FOREIGN KEY (eventid) REFERENCES SellerEvents (eventId));

ALTER TABLE Favorites
	ADD (
CONSTRAINT R_31 FOREIGN KEY (sellerid) REFERENCES SellerInfo (sellerid) ON DELETE SET NULL);

ALTER TABLE Favorites
	ADD (
CONSTRAINT R_33 FOREIGN KEY (userid) REFERENCES UserInfo (userid));

ALTER TABLE Review
	ADD (
CONSTRAINT R_9 FOREIGN KEY (productId) REFERENCES Product (productId));

ALTER TABLE Review
	ADD (
CONSTRAINT R_14 FOREIGN KEY (userid) REFERENCES UserInfo (userid));
