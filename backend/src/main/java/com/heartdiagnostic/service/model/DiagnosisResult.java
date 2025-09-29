package com.heartdiagnostic.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class DiagnosisResult {
    
    private String label;
    private Map<String, Double> scores;
    private String explanation;
    private String modelVersion;
    private long timestamp;

    public DiagnosisResult() {
        this.timestamp = System.currentTimeMillis();
        this.modelVersion = "v1.0";
    }

    public DiagnosisResult(String label, Map<String, Double> scores, String explanation) {
        this();
        this.label = label;
        this.scores = scores;
        this.explanation = explanation;
    }

    // Getters and Setters
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public Map<String, Double> getScores() { return scores; }
    public void setScores(Map<String, Double> scores) { this.scores = scores; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public String getModelVersion() { return modelVersion; }
    public void setModelVersion(String modelVersion) { this.modelVersion = modelVersion; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
