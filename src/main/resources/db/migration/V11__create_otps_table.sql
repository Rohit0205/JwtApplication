CREATE TABLE otps (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,

    otp_hash VARCHAR(64) NOT NULL,

    purpose VARCHAR(30) NOT NULL DEFAULT 'LOGIN',

    expires_at TIMESTAMP NOT NULL,

    attempts INTEGER NOT NULL DEFAULT 0,

    max_attempts INTEGER NOT NULL DEFAULT 5,

    verified BOOLEAN NOT NULL DEFAULT FALSE,

    verified_at TIMESTAMP,

    revoked BOOLEAN NOT NULL DEFAULT FALSE,

    revoked_at TIMESTAMP,

    created_at TIMESTAMP NOT NULL,

    created_by BIGINT,

    updated_at TIMESTAMP,

    deleted_at TIMESTAMP,

    status CHAR(1) DEFAULT 'Y',

    CONSTRAINT fk_otps_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);

CREATE INDEX idx_otps_user_purpose
    ON otps (user_id, purpose);