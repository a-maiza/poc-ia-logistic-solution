package com.example.triage.controller;

import com.example.triage.model.IncidentRequest;
import com.example.triage.model.IncidentResponse;
import com.example.triage.service.IncidentTriageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1/incidents")
public class IncidentController {

    private final IncidentTriageService incidentTriageService;

    public IncidentController(IncidentTriageService incidentTriageService) {
        this.incidentTriageService = incidentTriageService;
    }

    @PostMapping("/triage")
    public IncidentResponse triage(@RequestParam(defaultValue = "v1") String promptVersion,
                                   @Valid @RequestBody IncidentRequest request) {
        return incidentTriageService.triage(request, promptVersion);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleError(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }
}
