-- 1. Xóa dữ liệu cũ (Theo thứ tự để tránh lỗi khóa ngoại)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE user_roles;
TRUNCATE TABLE roles;
TRUNCATE TABLE product_variants;
TRUNCATE TABLE product_images;
TRUNCATE TABLE products;
TRUNCATE TABLE categories;
TRUNCATE TABLE brands;
TRUNCATE TABLE users;
TRUNCATE TABLE order_statuses;
SET FOREIGN_KEY_CHECKS = 1;

-- 2. Insert Roles
INSERT INTO roles (name, status, description, created_at, updated_at) VALUES
                                                                          ('ROLE_ADMIN', 1, 'Quản trị hệ thống', NOW(), NOW()),
                                                                          ('ROLE_USER', 1, 'Khách hàng', NOW(), NOW());

-- 3. Insert Order Statuses
INSERT INTO order_statuses (name, status, description, created_at, updated_at) VALUES
                                                                                   ('PENDING', 1, 'Chờ xác nhận', NOW(), NOW()),
                                                                                   ('SHIPPING', 1, 'Đang giao hàng', NOW(), NOW()),
                                                                                   ('DELIVERED', 1, 'Đã giao hàng', NOW(), NOW()),
                                                                                   ('CANCELLED', 1, 'Đã hủy', NOW(), NOW());

-- 4. Insert Brands
INSERT INTO brands (name, logo_url, created_at, updated_at) VALUES
                                                                ('Apple', 'apple_logo.png', NOW(), NOW()),
                                                                ('Samsung', 'samsung_logo.png', NOW(), NOW()),
                                                                ('Asus', 'asus_logo.png', NOW(), NOW());

-- 5. Insert Categories (Recursive: Laptop > Gaming)
INSERT INTO categories (name, status, category_id, created_at, updated_at) VALUES
                                                                               ('Laptop', 1, NULL, NOW(), NOW()),
                                                                               ('Smartphone', 1, NULL, NOW(), NOW());

-- Thêm sub-category (Giả sử Laptop ID là 1)
INSERT INTO categories (name, status, category_id, created_at, updated_at) VALUES
                                                                               ('Gaming Laptop', 1, 1, NOW(), NOW()),
                                                                               ('MacBook', 1, 1, NOW(), NOW());

-- 6. Insert Users (Password giả định đã mã hóa hoặc để tạm)
INSERT INTO users (username, password, full_name, email, status, created_at, updated_at) VALUES
                                                                                             ('admin', '$2a$10$xyz', 'Nguyễn Viết Khánh', 'admin@techshop.com', 1, NOW(), NOW()),
                                                                                             ('customer1', '$2a$10$xyz', 'User Test 01', 'user1@gmail.com', 1, NOW(), NOW());

-- Map Role cho User (Admin ID 1 - Role Admin ID 1)
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1), (2, 2);

-- 7. Insert Products (Dùng JSON cho specifications)
INSERT INTO products (name, category_id, brand_id, base_price, status, specifications, created_at, updated_at) VALUES
                                                                                                                   ('iPhone 15 Pro', 2, 1, 25000000, 1, '{"screen": "6.1 inch", "chip": "A17 Pro"}', NOW(), NOW()),
                                                                                                                   ('ROG Strix G16', 3, 3, 35000000, 1, '{"cpu": "i9-13980HX", "gpu": "RTX 4070"}', NOW(), NOW()),
                                                                                                                   ('Galaxy S24 Ultra', 2, 2, 28000000, 1, '{"pen": "S-Pen included", "zoom": "100x"}', NOW(), NOW());

-- 8. Insert Product Variants (Dùng JSON cho attributes)
-- Biến thể cho iPhone 15 Pro (Product ID 1)
INSERT INTO product_variants (product_id, price, stock_quantity, attributes, created_at, updated_at) VALUES
                                                                                                         (1, 25000000, 50, '{"color": "Natural Titanium", "storage": "128GB"}', NOW(), NOW()),
                                                                                                         (1, 28000000, 30, '{"color": "Blue Titanium", "storage": "256GB"}', NOW(), NOW());

-- 9. Tạo thêm nhiều sản phẩm để test phân trang (Loop giả lập)
INSERT INTO products (name, category_id, brand_id, base_price, status, created_at, updated_at)
SELECT CONCAT('Sản phẩm test ', id), 1, 1, 1000000 + (id * 10000), 1, NOW(), NOW()
FROM (SELECT a.id + b.id * 10 + 1 as id FROM (SELECT 0 as id UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) a CROSS JOIN (SELECT 0 as id UNION SELECT 1 UNION SELECT 2) b) t;