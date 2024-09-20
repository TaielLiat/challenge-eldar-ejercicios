---- INICIALIZADOR DE BASE DE DATOS ----
-- mysql -u username -p db_name < data.sql
-- http://localhost:8080/swagger-ui/index.html#/

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    dni VARCHAR(8) UNIQUE NOT NULL,
    birth_date DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS card (
    id UUID PRIMARY KEY,
    card_number VARCHAR(16) NOT NULL UNIQUE,
    brand ENUM('VISA', 'NARA', 'AMEX') NOT NULL,
    expiry_date DATE NOT NULL,
    cvv VARCHAR(3) NOT NULL,
    user_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE -- Si se borra el usuario, la tarjeta también
);

CREATE TABLE IF NOT EXISTS operation (
    id UUID PRIMARY KEY,
    amount DECIMAL(10, 2) NOT NULL,
    date DATE NOT NULL DEFAULT CURRENT_DATE,
    fee DECIMAL(10, 2),
    card_id UUID NOT NULL,
    user_id UUID NOT NULL,
    FOREIGN KEY (card_id) REFERENCES card(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

---- ALGUNOS DATOS PARA PROBAR ----

INSERT INTO users (id, name, email, dni, birth_date) VALUES
  ('e1a1d7b7-f012-4e35-bf6e-6c2e9f7b820a', 'Alice Johnson', 'alice@example.com', '12345678', '1985-05-15'),
  ('f2b1e7b8-a012-4f26-bf9e-7c2f9d7c810b', 'Bob Smith', 'bob@example.com', '87654321', '1990-08-21');

INSERT INTO card (id, card_number, brand, expiry_date, cvv, user_id) VALUES
  ('ca1d2a8c-b512-49b5-9874-fd1f6b7c920a', '1234567812345678', 'VISA', '2025-12-31', '123', 'e1a1d7b7-f012-4e35-bf6e-6c2e9f7b820a'),
  ('cb2e3b9d-c623-49f7-9874-fe2f6b7d930b', '8765432187654321', 'AMEX', '2026-11-30', '456', 'f2b1e7b8-a012-4f26-bf9e-7c2f9d7c810b');

INSERT INTO operation (id, amount, date, fee, card_id, user_id) VALUES
  ('op1d3a9e-d723-4a67-9985-fe3g7c8d830c', 150.00, '2023-09-10', 2.50, 'ca1d2a8c-b512-49b5-9874-fd1f6b7c920a', 'e1a1d7b7-f012-4e35-bf6e-6c2e9f7b820a'),
  ('op2e4b0f-e834-5b89-9996-fg4h8c9e940d', 200.00, '2023-09-12', 3.75, 'cb2e3b9d-c623-49f7-9874-fe2f6b7d930b', 'f2b1e7b8-a012-4f26-bf9e-7c2f9d7c810b');
