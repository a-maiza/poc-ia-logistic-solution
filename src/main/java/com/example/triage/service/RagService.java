package com.example.triage.service;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RagService {

    private final EmbeddingModel embeddingModel;
    private final JdbcTemplate jdbcTemplate;

    public RagService(EmbeddingModel embeddingModel, JdbcTemplate jdbcTemplate) {
        this.embeddingModel = embeddingModel;
        this.jdbcTemplate = jdbcTemplate;
    }

    public String retrieveSopChunks(String query) {
        if (query == null || query.isBlank()) {
            return "";
        }
        Embedding embedding = embeddingModel.embed(query).content();
        String vectorLiteral = toPgVectorLiteral(embedding.vectorAsList());

        List<String> rows = jdbcTemplate.query(
                "SELECT content FROM sop_documents ORDER BY embedding <-> CAST(? AS vector) LIMIT 3",
                (rs, rowNum) -> rs.getString("content"),
                vectorLiteral
        );
        return String.join("\n---\n", rows);
    }

    private String toPgVectorLiteral(List<Float> values) {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < values.size(); i++) {
            builder.append(values.get(i));
            if (i < values.size() - 1) {
                builder.append(',');
            }
        }
        builder.append(']');
        return builder.toString();
    }
}
