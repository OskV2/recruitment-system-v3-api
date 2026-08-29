CREATE TYPE attachment_status AS ENUM (
    'PENDING',
    'ACTIVE',
    'FAILED'
    );

ALTER TABLE attachment
    ADD COLUMN content_type      VARCHAR(255),
    ADD COLUMN size_bytes        BIGINT,
    ADD COLUMN checksum_sha256   VARCHAR(64),
    ADD COLUMN status            attachment_status NOT NULL DEFAULT 'ACTIVE';