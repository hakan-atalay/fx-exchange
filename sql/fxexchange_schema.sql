CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    role VARCHAR(20) DEFAULT 'USER',
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE exchange_rates (
    id BIGSERIAL PRIMARY KEY,
    base_currency_code VARCHAR(10) NOT NULL,
    target_currency_code VARCHAR(10) NOT NULL,
    rate DECIMAL(18,8) NOT NULL,
    source VARCHAR(100),
    fetched_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE wallets (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    currency_code VARCHAR(10) NOT NULL,
    balance DECIMAL(18,4) DEFAULT 0,
    UNIQUE(user_id, currency_code)
);

CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    from_currency_code VARCHAR(10) NOT NULL,
    to_currency_code VARCHAR(10) NOT NULL,
    amount_from DECIMAL(18,4) NOT NULL,
    amount_to DECIMAL(18,4) NOT NULL,
    exchange_rate DECIMAL(18,8) NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) DEFAULT 'COMPLETED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE deposits (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    currency_code VARCHAR(10) NOT NULL,
    amount DECIMAL(18,4) NOT NULL,
    method VARCHAR(50),
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE withdrawals (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    currency_code VARCHAR(10) NOT NULL,
    amount DECIMAL(18,4) NOT NULL,
    iban VARCHAR(50),
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE login_history (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    ip_address VARCHAR(50),
    login_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);