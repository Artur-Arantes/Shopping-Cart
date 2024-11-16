CREATE TABLE person (
name VARCHAR(50) NOT NULL,
email VARCHAR(50) PRIMARY KEY
);

CREATE TABLE permission (
id_permission BIGINT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(10) NOT NULL
);

CREATE TABLE users (
id_user BIGINT PRIMARY KEY AUTO_INCREMENT,
password VARCHAR(255) NOT NULL,
id_person VARCHAR(50) NOT NULL,
active TINYINT(1) NOT NULL,
role VARCHAR(50) DEFAULT 'ROLE_USER',
user_created_at datetime(6),
user_updated_at datetime(6),
user_version INTEGER,
FOREIGN KEY (id_person) REFERENCES person(email)
);

CREATE TABLE user_permission(
id_permission BIGINT NOT NULL,
id_user BIGINT NOT NULL,
PRIMARY KEY(id_user, id_permission),
FOREIGN KEY (id_user) REFERENCES users(id_user),
FOREIGN KEY (id_permission) REFERENCES permission(id_permission)
);

CREATE TABLE product(
sku BIGINT PRIMARY KEY,
name VARCHAR(50) NOT NULL,
price DECIMAL(38, 2) NOT NULL,
description VARCHAR(255),
product_created_at datetime(6),
product_updated_at datetime(6),
product_version INTEGER
);

CREATE TABLE inventory(
sku BIGINT NOT NULL,
quantity DECIMAL(38, 2) NOT NULL DEFAULT 0,
inventory_created_at datetime(6),
inventory_updated_at datetime(6),
inventory_version INTEGER,
FOREIGN KEY(sku) REFERENCES product(sku)
);

CREATE TABLE cart(
id_cart BIGINT PRIMARY KEY AUTO_INCREMENT,
id_user BIGINT NOT NULL,
total_amount DECIMAL(38, 2) DEFAULT 0.00,
cart_created_at datetime(6),
cart_updated_at datetime(6),
cart_version INTEGER,
FOREIGN KEY (id_user) REFERENCES users(id_user)
);

CREATE TABLE product_cart(
id_cart BIGINT NOT NULL,
sku BIGINT NOT NULL,
quantity DECIMAL(38,2) NOT NULL,
total_item DECIMAL(38,2) NOT NULL,
PRIMARY KEY (id_cart, sku),
FOREIGN KEY (id_cart) REFERENCES cart(id_cart),
FOREIGN KEY (sku) REFERENCES product(sku)
);

CREATE TABLE orders(
id_order BIGINT PRIMARY KEY AUTO_INCREMENT,
id_user BIGINT NOT NULL,
id_cart BIGINT NOT NULL,
total_amount DECIMAL(38, 2) DEFAULT 0.00,
order_created_at datetime(6),
order_updated_at datetime(6),
order_version INTEGER,
FOREIGN KEY (id_user) REFERENCES users(id_user),
FOREIGN KEY (id_cart) REFERENCES cart(id_cart)
);

CREATE TABLE product_order(
id_order BIGINT NOT NULL,
sku BIGINT NOT NULL,
quantity DECIMAL(38,2) NOT NULL,
total_item DECIMAL(38,2) NOT NULL,
PRIMARY KEY (id_order, sku),
FOREIGN KEY (id_order) REFERENCES orders(id_order),
FOREIGN KEY (sku) REFERENCES product(sku)
);

