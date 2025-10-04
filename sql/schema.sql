CREATE TABLE IF NOT EXISTS local_db_message (
    id BIGSERIAL PRIMARY KEY,
    conversation_id TEXT NOT NULL,
    local_db_role VARCHAR(32) NOT NULL,
    text TEXT NOT NULL,
    creation_date_time TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS local_db_statistic (
    id BIGSERIAL PRIMARY KEY,
    question TEXT NOT NULL,
    answer TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS local_db_statistic_context (
    id BIGSERIAL PRIMARY KEY,
    statistic_id BIGINT NOT NULL,
    context TEXT NOT NULL,
    FOREIGN KEY (statistic_id) REFERENCES local_db_statistic(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_local_db_message_conversation_id
    ON local_db_message(conversation_id);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE local_db_message TO pgvector_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE local_db_statistic TO pgvector_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE local_db_statistic_context TO pgvector_user;
GRANT USAGE, SELECT ON SEQUENCE local_db_statistic_id_seq TO pgvector_user;
GRANT USAGE, SELECT ON SEQUENCE local_db_statistic_context_id_seq TO pgvector_user;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO pgvector_user;
