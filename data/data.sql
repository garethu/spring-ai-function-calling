CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS vector_store (
	id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
	content text,
	metadata json,
	embedding vector(1536) // 1536 is the default embedding dimension
);

CREATE INDEX ON vector_store USING HNSW (embedding vector_cosine_ops);

CREATE TABLE stock (
    id SERIAL PRIMARY KEY,
    symbol VARCHAR(3) UNIQUE NOT NULL,
    min_price DECIMAL NOT NULL,
    alert BOOLEAN NOT NULL,
    description TEXT
);

-- Insert data for Bitcoin (BTC)
INSERT INTO stock (code, min_price, alert, description)
VALUES ('BTC', 97000.00, FALSE, 'Bitcoin');

-- Insert data for Tron (TRX)
INSERT INTO stock (code, min_price, alert, description)
VALUES ('TRX', 0.25, TRUE, 'Tron');

