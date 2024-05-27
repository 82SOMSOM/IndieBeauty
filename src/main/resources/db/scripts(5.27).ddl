
CREATE TABLE Category
(
	categoryId           INTEGER  NOT NULL ,
	name                 VARCHAR2(10)  NOT NULL 
);

CREATE UNIQUE INDEX XPKCategory ON Category
(categoryId   ASC);

ALTER TABLE Category
	ADD CONSTRAINT  XPKCategory PRIMARY KEY (categoryId);

CREATE TABLE Order
(
	orderId              INTEGER  NOT NULL ,
	userId               VARCHAR2(20)  NULL ,
	orderDate            DATE  NOT NULL ,
	totalPrice           INTEGER  NOT NULL ,
	isOrderPaid          INTEGER  NOT NULL ,
	accountNumber        INTEGER  NOT NULL ,
	address              VARCHAR2(100)  NOT NULL 
);

CREATE UNIQUE INDEX XPKOrder ON Order
(orderId   ASC);

ALTER TABLE Order
	ADD CONSTRAINT  XPKOrder PRIMARY KEY (orderId);

CREATE TABLE Product
(
	productId            INTEGER  NOT NULL ,
	categoryId           INTEGER  NOT NULL ,
	name                 VARCHAR2(20)  NOT NULL ,
	description          VARCHAR2(500)  NULL ,
	price                INTEGER  NOT NULL ,
	stock                INTEGER  NOT NULL ,
	date                 DATE  NOT NULL 
);

CREATE UNIQUE INDEX XPKProduct ON Product
(productId   ASC);

ALTER TABLE Product
	ADD CONSTRAINT  XPKProduct PRIMARY KEY (productId);

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

CREATE TABLE ProductImage
(
	imageId              CHAR(18)  NOT NULL ,
	productId            INTEGER  NULL ,
	imageUrl             VARCHAR2(20)  NOT NULL ,
	isTitleImg           INT  NOT NULL 
);

CREATE UNIQUE INDEX XPKProductImage ON ProductImage
(imageId   ASC);

ALTER TABLE ProductImage
	ADD CONSTRAINT  XPKProductImage PRIMARY KEY (imageId);

CREATE TABLE SellerEvents
(
	eventId              INTEGER  NOT NULL ,
	sellerId             VARCHAR2(20)  NOT NULL ,
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
	sellerId             VARCHAR2(20)  NOT NULL ,
	sellerName           VARCHAR2(20)  NOT NULL ,
	passwd               VARCHAR2(20)  NOT NULL ,
	phone                NUMBER(11)  NOT NULL ,
	businessRegistration VARCHAR2(20)  NULL 
);

CREATE UNIQUE INDEX XPKSellerInfo ON SellerInfo
(sellerId   ASC);

ALTER TABLE SellerInfo
	ADD CONSTRAINT  XPKSellerInfo PRIMARY KEY (sellerId);

CREATE TABLE UserInfo
(
	userId               VARCHAR2(20)  NOT NULL ,
	userName             VARCHAR2(20)  NOT NULL ,
	passwd               VARCHAR2(20)  NOT NULL ,
	email                VARCHAR2(30)  NOT NULL ,
	phone                NUMBER(11)  NOT NULL ,
	address              VARCHAR2(100)  NOT NULL 
);

CREATE UNIQUE INDEX XPKUserInfo ON UserInfo
(userId   ASC);

ALTER TABLE UserInfo
	ADD CONSTRAINT  XPKUserInfo PRIMARY KEY (userId);

CREATE TABLE JoinEvent
(
	userId               VARCHAR2(20)  NOT NULL ,
	eventId              INTEGER  NOT NULL 
);

CREATE UNIQUE INDEX XPKJoinEvent ON JoinEvent
(userId   ASC,eventId   ASC);

ALTER TABLE JoinEvent
	ADD CONSTRAINT  XPKJoinEvent PRIMARY KEY (userId,eventId);

CREATE TABLE Review
(
	reviewId             INTEGER  NOT NULL ,
	userId               VARCHAR2(20)  NOT NULL ,
	date                 DATE  NULL ,
	content              VARCHAR2(20)  NULL ,
	image                VARCHAR2(20)  NULL ,
	star                 INTEGER  NULL ,
	productId            INTEGER  NOT NULL 
);

CREATE UNIQUE INDEX XPKReview ON Review
(reviewId   ASC);

ALTER TABLE Review
	ADD CONSTRAINT  XPKReview PRIMARY KEY (reviewId);

CREATE TABLE Favorites
(
	favId                VARCHAR2(20)  NOT NULL ,
	sellerId             VARCHAR2(20)  NOT NULL ,
	userId               VARCHAR2(20)  NOT NULL 
);

CREATE UNIQUE INDEX XPKFavorites ON Favorites
(favId   ASC,userId   ASC);

ALTER TABLE Favorites
	ADD CONSTRAINT  XPKFavorites PRIMARY KEY (favId,userId);

ALTER TABLE Order
	ADD (
CONSTRAINT R_30 FOREIGN KEY (userId) REFERENCES UserInfo (userId) ON DELETE SET NULL);

ALTER TABLE Product
	ADD (
CONSTRAINT R_10 FOREIGN KEY (categoryId) REFERENCES Category (categoryId));

ALTER TABLE Item
	ADD (
CONSTRAINT R_4 FOREIGN KEY (productId) REFERENCES Product (productId));

ALTER TABLE Item
	ADD (
CONSTRAINT R_6 FOREIGN KEY (orderId) REFERENCES Order (orderId));

ALTER TABLE ProductImage
	ADD (
CONSTRAINT R_37 FOREIGN KEY (productId) REFERENCES Product (productId) ON DELETE SET NULL);

ALTER TABLE SellerEvents
	ADD (
CONSTRAINT R_13 FOREIGN KEY (sellerId) REFERENCES SellerInfo (sellerId));

ALTER TABLE JoinEvent
	ADD (
CONSTRAINT R_27 FOREIGN KEY (userId) REFERENCES UserInfo (userId));

ALTER TABLE JoinEvent
	ADD (
CONSTRAINT R_28 FOREIGN KEY (eventId) REFERENCES SellerEvents (eventId));

ALTER TABLE Review
	ADD (
CONSTRAINT R_9 FOREIGN KEY (productId) REFERENCES Product (productId));

ALTER TABLE Review
	ADD (
CONSTRAINT R_14 FOREIGN KEY (userId) REFERENCES UserInfo (userId));

ALTER TABLE Favorites
	ADD (
CONSTRAINT R_31 FOREIGN KEY (sellerId) REFERENCES SellerInfo (sellerId) ON DELETE SET NULL);

ALTER TABLE Favorites
	ADD (
CONSTRAINT R_33 FOREIGN KEY (userId) REFERENCES UserInfo (userId));
