CREATE TABLE tasks (
    id UUID PRIMARY KEY,
    user_id UUID,
    source_type VARCHAR(50) NOT NULL,
    source_id UUID,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    recommended_action TEXT,
    risk_level VARCHAR(50),
    status VARCHAR(50) NOT NULL,
    approved_at TIMESTAMP WITH TIME ZONE,
    rejected_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);