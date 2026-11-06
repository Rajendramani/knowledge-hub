-- V2: Seed admin user for development
INSERT INTO user_service.users (id, email, first_name, last_name, password_hash, role, status)
VALUES (
    'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
    'admin@knowledgehub.com',
    'System',
    'Admin',
    '$2a$12$LJ3m4ks9Yxz.placeholder.hash',  -- Replace with real BCrypt hash
    'ADMIN',
    'ACTIVE'
);
