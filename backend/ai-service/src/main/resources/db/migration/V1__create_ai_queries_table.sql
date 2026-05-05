CREATE TABLE ai_queries (
    id UUID PRIMARY KEY,
    user_id UUID,
    question TEXT NOT NULL,
    answer TEXT,
    status VARCHAR(50) NOT NULL,
    provider VARCHAR(50),
    model VARCHAR(100),
    error_message TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);