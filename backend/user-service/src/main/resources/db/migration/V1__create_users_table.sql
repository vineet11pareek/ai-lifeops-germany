CREATE TABLE users (
    id UUID PRIMARY KEY,
    external_id VARCHAR(255),
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    country VARCHAR(100),
    provider VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (
    id,
    external_id,
    full_name,
    email,
    country,
    provider
) VALUES (
    '11111111-1111-1111-1111-111111111111',
    'demo-google-user',
    'Vineet Pareek',
    'vineet@example.com',
    'Germany',
    'GOOGLE'
);