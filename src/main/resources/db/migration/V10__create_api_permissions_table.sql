CREATE TABLE api_permissions (
    id BIGSERIAL PRIMARY KEY,

    http_method VARCHAR(10) NOT NULL,

    endpoint VARCHAR(255) NOT NULL,

    permission_id BIGINT NOT NULL,

    description VARCHAR(255),

    created_at TIMESTAMP,
    created_by BIGINT,
    updated_at TIMESTAMP,
    status CHAR(1) DEFAULT 'Y',
    deleted_at TIMESTAMP,

    CONSTRAINT fk_api_permissions_permission
        FOREIGN KEY (permission_id)
        REFERENCES permissions(id),

    CONSTRAINT uk_api_permission_endpoint
        UNIQUE (http_method, endpoint)
);