CREATE EXTENSION "uuid-ossp";

CREATE TABLE IF NOT EXISTS users (
    user_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(30),
    email VARCHAR(100),
    access_token VARCHAR(250)
);

CREATE TABLE IF NOT EXISTS categories (
    category_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    category_name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    product_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    product_name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    price REAL NOT NULL,
    category_id UUID NOT NULL REFERENCES categories(category_id),
    available INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS carts (
    cart_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS cart_products (
    cart_product_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    product_id UUID NOT NULL REFERENCES products(product_id),
    cart_id UUID NOT NULL REFERENCES carts(cart_id),
    amount INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS payments (
    payment_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    payment_method VARCHAR(30) NOT NULL,
    payment_date BIGINT NOT NULL,
    paid BOOLEAN NOT NULL,
    user_id UUID NOT NULL REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS purchases (
    purchase_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(user_id),
    cash_sum INTEGER NOT NULL,
    payment_id UUID NOT NULL REFERENCES payments(payment_id)
);

CREATE TABLE IF NOT EXISTS purchase_products (
    purchase_product_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    purchase_id UUID NOT NULL REFERENCES purchases(purchase_id),
    product_id UUID NOT NULL REFERENCES products(product_id),
    amount INTEGER NOT NULL
);
