-- Rregullo constraints për products table
-- Bëj category_id opsionale
ALTER TABLE products ALTER COLUMN category_id DROP NOT NULL;

-- Bëj city_id opsionale  
ALTER TABLE products ALTER COLUMN city_id DROP NOT NULL;

-- Bëj image_url opsionale (nëse është NOT NULL)
ALTER TABLE products ALTER COLUMN image_url DROP NOT NULL;

-- Kontrollo strukturën e tabelës
\d products

-- Tani provoni përsëri shtimin e produktit
INSERT INTO products (supplier_id, name, description, price, stock_quantity, status) 
VALUES (17, 'Laptop Dell XPS 13', 'Laptop i kualitetit të lartë me Intel Core i7', 1299.99, 15, 'active');

-- Kontrollo rezultatin
SELECT * FROM products ORDER BY id DESC LIMIT 3;
