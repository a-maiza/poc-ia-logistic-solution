CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE IF NOT EXISTS sop_documents (
    id SERIAL PRIMARY KEY,
    content TEXT,
    embedding vector(1536)
);

INSERT INTO sop_documents (content, embedding)
VALUES
('If cargo damage is reported, isolate affected pallets and notify insurance within 2 hours.', ARRAY(SELECT 0.0 FROM generate_series(1,1536))::vector),
('For customs holds, gather invoice, packing list, and HS code declaration before escalation.', ARRAY(SELECT 0.0 FROM generate_series(1,1536))::vector),
('For port congestion delays over 24h, prioritize rerouting options and customer communication.', ARRAY(SELECT 0.0 FROM generate_series(1,1536))::vector);
