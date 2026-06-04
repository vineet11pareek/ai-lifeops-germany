ALTER TABLE tasks
ADD COLUMN user_external_id VARCHAR(255);

CREATE INDEX idx_tasks_user_external_id_status_created_at
ON tasks (user_external_id, status, created_at DESC);