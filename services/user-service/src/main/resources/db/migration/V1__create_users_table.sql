-- ═══════════════════════════════════════════════════
-- V1: Create Users Table
-- Knowledge-Hub User Service
-- ═══════════════════════════════════════════════════


CREATE SCHEMA IF NOT EXISTS user_service;


CREATE TABLE user_service.users (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email           VARCHAR(255) NOT NULL,
    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,
    password_hash   VARCHAR(255) NOT NULL,
    role            VARCHAR(20)  NOT NULL DEFAULT 'VIEWER',
    status          VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    bio             TEXT,
    avatar_url      VARCHAR(500),
    metadata        JSONB DEFAULT '{}',
    last_login_at   TIMESTAMP WITH TIME ZONE,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    version         BIGINT NOT NULL DEFAULT 0
);


-- ═══ INDEXES ═══
CREATE UNIQUE INDEX idx_users_email ON user_service.users(email);
CREATE INDEX idx_users_role ON user_service.users(role);
CREATE INDEX idx_users_status ON user_service.users(status);
CREATE INDEX idx_users_name ON user_service.users(first_name, last_name);
CREATE INDEX idx_users_created_at ON user_service.users(created_at DESC);
CREATE INDEX idx_users_metadata ON user_service.users USING GIN(metadata);


-- ═══ COMMENTS ═══
COMMENT ON TABLE user_service.users IS 'Core user accounts table';
COMMENT ON COLUMN user_service.users.metadata IS 'Flexible JSONB for preferences, settings';
COMMENT ON COLUMN user_service.users.version IS 'Optimistic locking version';
