-- Test queries for debugging
-- First check if tables exist and have data
SELECT COUNT(*) FROM categories;
SELECT COUNT(*) FROM suppliers; 
SELECT COUNT(*) FROM cities;

-- Try to insert with NULL values first (should work with nullable=true)
INSERT INTO products (name, description, price, stock_quantity, status) 
VALUES ('Test Produkt NULL', 'Test me NULL foreign keys', 99.99, 10, 'active');

-- If that works, try with valid IDs
INSERT INTO products (supplier_id, category_id, city_id, name, description, price, stock_quantity, status) 
VALUES (1, 1, 1, 'Test Produkt me IDs', 'Test me foreign keys', 199.99, 20, 'active');

-- Check results
SELECT * FROM products ORDER BY id DESC LIMIT 5;
