CREATE TABLE documents (
    id UUID PRIMARY KEY,
    user_id UUID,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    summary TEXT,
    deadline_text TEXT,
    required_action TEXT,
    risk_level VARCHAR(50),
    suggested_next_step TEXT,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);