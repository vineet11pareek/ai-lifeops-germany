ALTER TABLE documents
ADD COLUMN user_external_id VARCHAR(255);

CREATE INDEX idx_documents_user_external_id_created_at
ON documents (user_external_id, created_at DESC);