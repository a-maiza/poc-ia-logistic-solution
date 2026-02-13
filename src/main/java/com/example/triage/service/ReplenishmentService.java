package com.example.triage.service;

import com.example.triage.model.ReplenishmentRequest;
import com.example.triage.model.ReplenishmentResponse;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.stereotype.Service;

@Service
public class ReplenishmentService {

    private final ChatLanguageModel chatModel;

    public ReplenishmentService(ChatLanguageModel chatModel) {
        this.chatModel = chatModel;
    }

    public ReplenishmentResponse recommend(ReplenishmentRequest request) {
        int projected = request.getStock() + request.getInbound() - request.getForecast7d();
        String decision;
        int recommendedQty;

        if (projected < 0) {
            decision = "ORDER";
            recommendedQty = Math.abs(projected) + 100;
        } else {
            decision = "NO_ACTION";
            recommendedQty = 0;
        }

        String explanationPrompt = String.format(
                "Provide a short logistics explanation in one sentence based on this deterministic decision. " +
                        "Do not change numbers. decision=%s, recommendedQty=%d, stock=%d, inbound=%d, forecast7d=%d, projected=%d",
                decision,
                recommendedQty,
                request.getStock(),
                request.getInbound(),
                request.getForecast7d(),
                projected
        );

        String explanation = chatModel.generate(explanationPrompt);
        return new ReplenishmentResponse(decision, recommendedQty, explanation);
    }
}
