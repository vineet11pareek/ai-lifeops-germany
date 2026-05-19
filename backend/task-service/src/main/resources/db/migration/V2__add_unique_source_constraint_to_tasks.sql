CREATE UNIQUE INDEX ux_tasks_source_type_source_id
ON tasks (source_type, source_id)
WHERE source_id IS NOT NULL;