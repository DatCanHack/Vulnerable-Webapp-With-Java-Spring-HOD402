-- Password is '123456' encoded with fixed salt '$2a$10$fixedSaltForDevEnvironment'
SET @encoded_password = '$2a$10$fixedSaltForDevEnvironment.XJ6QFr2EwNTh3V0H0G/ku/WLkQQ2';

-- Admin user
INSERT INTO users (username, password, role, email, full_name) 
VALUES ('admin', @encoded_password, 'ADMIN', 'admin@example.com', 'Administrator')
ON DUPLICATE KEY UPDATE username=username;

-- Sellers
INSERT INTO users (username, password, role, email, full_name) VALUES
('seller1', @encoded_password, 'SELLER', 'seller1@example.com', 'John Seller'),
('seller2', @encoded_password, 'SELLER', 'seller2@example.com', 'Jane Seller'),
('seller3', @encoded_password, 'SELLER', 'seller3@example.com', 'Mike Seller')
ON DUPLICATE KEY UPDATE username=username;

-- Regular users
INSERT INTO users (username, password, role, email, full_name) VALUES
('user', @encoded_password, 'USER', 'user1@example.com', 'Alice User'),
('user2', @encoded_password, 'USER', 'user2@example.com', 'Bob User'),
('user3', @encoded_password, 'USER', 'user3@example.com', 'Carol User')
ON DUPLICATE KEY UPDATE username=username;

-- Products for seller1
INSERT INTO products (name, description, price, seller_id) 
SELECT 'Gaming Laptop', 'High-performance gaming laptop with RTX 3080, 32GB RAM', 1499.99, id 
FROM users WHERE username = 'seller1';

INSERT INTO products (name, description, price, seller_id)
SELECT 'Mechanical Keyboard', 'RGB mechanical keyboard with Cherry MX switches', 129.99, id
FROM users WHERE username = 'seller1';

INSERT INTO products (name, description, price, seller_id)
SELECT 'Gaming Mouse', 'Wireless gaming mouse with 20000 DPI sensor', 79.99, id
FROM users WHERE username = 'seller1';

-- Products for seller2
INSERT INTO products (name, description, price, seller_id)
SELECT 'Smartphone', 'Latest model with 6.7" OLED display and 5G', 899.99, id
FROM users WHERE username = 'seller2';

INSERT INTO products (name, description, price, seller_id)
SELECT 'Wireless Earbuds', 'True wireless earbuds with active noise cancellation', 199.99, id
FROM users WHERE username = 'seller2';

INSERT INTO products (name, description, price, seller_id)
SELECT 'Smart Watch', 'Fitness tracking smartwatch with heart rate monitor', 249.99, id
FROM users WHERE username = 'seller2';

-- Products for seller3
INSERT INTO products (name, description, price, seller_id)
SELECT '4K Monitor', '32" 4K HDR monitor for professional use', 699.99, id
FROM users WHERE username = 'seller3'; 