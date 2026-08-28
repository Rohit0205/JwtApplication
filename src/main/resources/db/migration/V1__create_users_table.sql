CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,

    email VARCHAR(255) NOT NULL UNIQUE,

    mobile_number VARCHAR(20) UNIQUE,

    password VARCHAR(255) NOT NULL,

    first_name VARCHAR(100) NOT NULL,

    last_name VARCHAR(100),

    account_locked BOOLEAN NOT NULL DEFAULT FALSE,

    email_verified BOOLEAN NOT NULL DEFAULT FALSE,

    mobile_verified BOOLEAN NOT NULL DEFAULT FALSE,

    failed_login_attempts INTEGER NOT NULL DEFAULT 0,

    last_login_at TIMESTAMP,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    created_by BIGINT,

    deleted_at TIMESTAMP,

    status CHARACTER NOT NULL
);