INSERT INTO users (enabled, locked, id, email,  first_name, last_name, password, role, username)
VALUES (true, false,1, '230103246@sdu.edu.kz', 'Bekarys', 'Absattar', 12345, 'USER', 'undermax88');

-- CATEGORIES
INSERT INTO category (id, name) VALUES
                                    (1, 'Shoes'),
                                    (2, 'Clothes'),
                                    (3, 'Electronics');

-- BRANDS
INSERT INTO brand (id, name) VALUES
                                 (1, 'Nike'),
                                 (2, 'Adidas'),
                                 (3, 'Apple');

-- PRODUCTS
INSERT INTO product (id, name, price, category_id, brand_id) VALUES
                                                                 (1, 'Nike Air Max', 120.0, 1, 1),
                                                                 (2, 'Adidas Yeezy', 200.0, 1, 2),
                                                                 (3, 'T-Shirt White', 25.0, 2, 2),
                                                                 (4, 'iPhone 14', 999.0, 3, 3);

-- CART
INSERT INTO cart (id, user_id) VALUES
    (1, 1);

-- CART ITEMS
INSERT INTO cart_item (id, cart_id, product_id, quantity) VALUES
(1, 1, 1, 2),  -- 2 Nike Air Max
(2, 1, 3, 1);  -- 1 T-Shirt