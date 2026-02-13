package com.example.triage.model;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;

public class IncidentRequest {

    @NotBlank
    private String incidentText;
    private Map<String, Object> incidentJson;
    private String sopQuery;

    public String getIncidentText() {
        return incidentText;
    }

    public void setIncidentText(String incidentText) {
        this.incidentText = incidentText;
    }

    public Map<String, Object> getIncidentJson() {
        return incidentJson;
    }

    public void setIncidentJson(Map<String, Object> incidentJson) {
        this.incidentJson = incidentJson;
    }

    public String getSopQuery() {
        return sopQuery;
    }

    public void setSopQuery(String sopQuery) {
        this.sopQuery = sopQuery;
    }
}
