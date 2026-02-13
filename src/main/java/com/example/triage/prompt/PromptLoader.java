package com.example.triage.prompt;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class PromptLoader {

    public String loadIncidentPrompt(String version) {
        Path filePath = Path.of("prompts", "logistics_incident_triage", version, "prompt.md");
        try {
            if (Files.exists(filePath)) {
                return Files.readString(filePath);
            }
            return new String(new ClassPathResource("prompts/logistics_incident_triage/" + version + "/prompt.md").getInputStream().readAllBytes());
        } catch (IOException e) {
            throw new IllegalArgumentException("Prompt version not found: " + version, e);
        }
    }
}
