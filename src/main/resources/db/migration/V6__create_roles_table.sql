CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),

    created_at TIMESTAMP,
    created_by BIGINT,
    updated_at TIMESTAMP,
    status CHAR(1) DEFAULT 'Y',
    deleted_at TIMESTAMP
);