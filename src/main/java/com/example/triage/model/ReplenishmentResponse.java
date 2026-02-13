package com.example.triage.model;

public class ReplenishmentResponse {

    private String decision;
    private int recommendedQty;
    private String explanation;

    public ReplenishmentResponse(String decision, int recommendedQty, String explanation) {
        this.decision = decision;
        this.recommendedQty = recommendedQty;
        this.explanation = explanation;
    }

    public String getDecision() {
        return decision;
    }

    public int getRecommendedQty() {
        return recommendedQty;
    }

    public String getExplanation() {
        return explanation;
    }
}
