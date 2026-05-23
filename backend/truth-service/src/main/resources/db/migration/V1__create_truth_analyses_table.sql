CREATE TABLE truth_analyses (
    id UUID PRIMARY KEY,
    user_id UUID,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    claim_summary TEXT,
    trust_score INTEGER,
    risk_level VARCHAR(50),
    explanation TEXT,
    suggested_verification_steps TEXT,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);