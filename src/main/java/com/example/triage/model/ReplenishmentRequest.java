package com.example.triage.model;

import jakarta.validation.constraints.NotNull;

public class ReplenishmentRequest {

    @NotNull
    private Integer stock;
    @NotNull
    private Integer inbound;
    @NotNull
    private Integer forecast7d;

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getInbound() {
        return inbound;
    }

    public void setInbound(Integer inbound) {
        this.inbound = inbound;
    }

    public Integer getForecast7d() {
        return forecast7d;
    }

    public void setForecast7d(Integer forecast7d) {
        this.forecast7d = forecast7d;
    }
}
