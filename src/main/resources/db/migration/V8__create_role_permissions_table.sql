CREATE TABLE role_permissions (
    id BIGSERIAL PRIMARY KEY,

    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,

    created_at TIMESTAMP,
    created_by BIGINT,
    updated_at TIMESTAMP,
    status CHAR(1) DEFAULT 'Y',
    deleted_at TIMESTAMP,

    CONSTRAINT fk_role_permissions_role
        FOREIGN KEY (role_id)
        REFERENCES roles(id),

    CONSTRAINT fk_role_permissions_permission
        FOREIGN KEY (permission_id)
        REFERENCES permissions(id),

    CONSTRAINT uk_role_permission
        UNIQUE (role_id, permission_id)
);