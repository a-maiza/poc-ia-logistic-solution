package com.example.triage.service;

import com.example.triage.model.IncidentRequest;
import com.example.triage.model.IncidentResponse;
import com.example.triage.prompt.PromptLoader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

@Service
public class IncidentTriageService {

    private final ChatLanguageModel chatModel;
    private final PromptLoader promptLoader;
    private final RagService ragService;
    private final ObjectMapper objectMapper;
    private final JsonSchema schema;

    public IncidentTriageService(ChatLanguageModel chatModel,
                                 PromptLoader promptLoader,
                                 RagService ragService,
                                 ObjectMapper objectMapper) throws IOException {
        this.chatModel = chatModel;
        this.promptLoader = promptLoader;
        this.ragService = ragService;
        this.objectMapper = objectMapper;
        this.schema = loadSchema();
    }

    public IncidentResponse triage(IncidentRequest request, String promptVersion) {
        String promptTemplate = promptLoader.loadIncidentPrompt(promptVersion);
        String sopChunks = ragService.retrieveSopChunks(request.getSopQuery());
        String incidentJson = toJson(request.getIncidentJson());

        String prompt = promptTemplate
                .replace("{{incident_text}}", request.getIncidentText())
                .replace("{{incident_json}}", incidentJson)
                .replace("{{sop_chunks}}", sopChunks);

        return callAndValidate(prompt, true);
    }

    private IncidentResponse callAndValidate(String prompt, boolean retryAllowed) {
        try {
            String raw = chatModel.generate(prompt);
            JsonNode jsonNode = objectMapper.readTree(raw);
            Set<ValidationMessage> errors = schema.validate(jsonNode);
            if (!errors.isEmpty()) {
                if (retryAllowed) {
                    return callAndValidate(prompt + "\n\nIMPORTANT: fix JSON to match schema exactly.", false);
                }
                throw new IllegalStateException("LLM output schema validation failed: " + errors);
            }
            return objectMapper.treeToValue(jsonNode, IncidentResponse.class);
        } catch (Exception e) {
            if (retryAllowed) {
                return callAndValidate(prompt + "\n\nIMPORTANT: return valid JSON only.", false);
            }
            throw new IllegalStateException("Failed to triage incident", e);
        }
    }

    private JsonSchema loadSchema() throws IOException {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);
        try (InputStream in = new ClassPathResource("schema/triage_output.schema.json").getInputStream()) {
            return factory.getSchema(in);
        }
    }

    private String toJson(Object input) {
        try {
            return input == null ? "{}" : objectMapper.writeValueAsString(input);
        } catch (Exception e) {
            return "{}";
        }
    }
}
