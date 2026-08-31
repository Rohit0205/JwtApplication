TRUNCATE TABLE refresh_tokens;

ALTER TABLE refresh_tokens
    ADD COLUMN token_hash VARCHAR(64);

ALTER TABLE refresh_tokens
    DROP COLUMN token;

ALTER TABLE refresh_tokens
    ADD CONSTRAINT uk_refresh_tokens_token_hash
    UNIQUE (token_hash);