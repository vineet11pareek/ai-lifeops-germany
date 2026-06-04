ALTER TABLE ai_queries
ADD COLUMN user_external_id VARCHAR(255);

CREATE INDEX idx_ai_queries_user_external_id_created_at
ON ai_queries (user_external_id, created_at DESC);