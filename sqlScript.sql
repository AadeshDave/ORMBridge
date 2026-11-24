CREATE TABLE shp_customer (
customer_id INT AUTO_INCREMENT PRIMARY KEY,
customer_name VARCHAR(60) NOT NULL,
email VARCHAR(80) UNIQUE,
city VARCHAR(40)
);
CREATE TABLE shp_product (
product_id INT AUTO_INCREMENT PRIMARY KEY,
product_name VARCHAR(80) NOT NULL,
price DECIMAL(10,2) NOT NULL,
stock INT
);
CREATE TABLE shp_order (
order_id INT AUTO_INCREMENT PRIMARY KEY,
customer_id INT NOT NULL,
order_date DATE,
status VARCHAR(20),
FOREIGN KEY (customer_id) REFERENCES shp_customer(customer_id)
);
CREATE TABLE shp_order_item (
order_id INT NOT NULL,
product_id INT NOT NULL,
quantity INT NOT NULL,
PRIMARY KEY (order_id, product_id),
FOREIGN KEY (order_id) REFERENCES shp_order(order_id),
FOREIGN KEY (product_id) REFERENCES shp_product(product_id)
);