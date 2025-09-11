CREATE TABLE IF NOT EXISTS local_db_message (
    id BIGSERIAL PRIMARY KEY,
    conversation_id TEXT NOT NULL,
    local_db_role VARCHAR(32) NOT NULL,
    text TEXT NOT NULL,
    creation_date_time TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_local_db_message_conversation_id
    ON local_db_message(conversation_id);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE local_db_message TO pgvector_user;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO pgvector_user;
