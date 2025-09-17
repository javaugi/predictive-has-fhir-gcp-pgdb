CREATE TABLE IF NOT EXISTS medicalDocuments (
    id VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid()::text,
    version BIGINT DEFAULT 0,
    title TEXT NOT NULL,
    text_content TEXT NOT NULL,
    specialty TEXT,
    document_type TEXT,
    publication_date TIMESTAMP WITH TIME ZONE,
    embedding REAL[],
    pdf_content BYTEA
)
