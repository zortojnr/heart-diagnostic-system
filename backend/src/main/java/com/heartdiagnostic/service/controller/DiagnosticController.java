package com.heartdiagnostic.service.controller;

import com.heartdiagnostic.service.model.DiagnosisResult;
import com.heartdiagnostic.service.model.SymptomInput;
import com.heartdiagnostic.service.service.WekaModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class DiagnosticController {
    
    @Autowired
    private WekaModelService wekaModelService;
    
    @PostMapping("/diagnose")
    public ResponseEntity<DiagnosisResult> diagnose(@Valid @RequestBody SymptomInput input) {
        try {
            if (!wekaModelService.isModelLoaded()) {
                return ResponseEntity.internalServerError()
                    .body(createErrorResult("Model not loaded. Please try again later."));
            }
            
            DiagnosisResult result = wekaModelService.classify(input);
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            System.err.println("Error during diagnosis: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body(createErrorResult("Diagnosis failed: " + e.getMessage()));
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("modelLoaded", wekaModelService.isModelLoaded());
        health.put("modelInfo", wekaModelService.getModelInfo());
        health.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(health);
    }
    
    @GetMapping("/model/info")
    public ResponseEntity<Map<String, String>> modelInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("modelType", wekaModelService.getModelInfo());
        info.put("status", wekaModelService.isModelLoaded() ? "loaded" : "not loaded");
        info.put("version", "v1.0");
        
        return ResponseEntity.ok(info);
    }
    
    private DiagnosisResult createErrorResult(String errorMessage) {
        DiagnosisResult errorResult = new DiagnosisResult();
        errorResult.setLabel("Error");
        errorResult.setExplanation(errorMessage);
        
        Map<String, Double> errorScores = new HashMap<>();
        errorScores.put("Healthy", 0.0);
        errorScores.put("Moderate Risk", 0.0);
        errorScores.put("Severe Risk", 0.0);
        errorResult.setScores(errorScores);
        
        return errorResult;
    }
}
