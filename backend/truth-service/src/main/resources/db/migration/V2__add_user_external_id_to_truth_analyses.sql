ALTER TABLE truth_analyses
ADD COLUMN user_external_id VARCHAR(255);

CREATE INDEX idx_truth_analyses_user_external_id_created_at
ON truth_analyses (user_external_id, created_at DESC);