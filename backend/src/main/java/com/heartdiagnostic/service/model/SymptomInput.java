package com.heartdiagnostic.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public class SymptomInput {
    
    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 120, message = "Age must be at most 120")
    private Integer age;
    
    @NotBlank(message = "Sex is required")
    @Pattern(regexp = "^(male|female)$", message = "Sex must be either 'male' or 'female'")
    private String sex;
    
    @NotBlank(message = "Chest pain type is required")
    @Pattern(regexp = "^(typical|atypical|non-anginal|asymptomatic)$", 
             message = "Chest pain must be one of: typical, atypical, non-anginal, asymptomatic")
    @JsonProperty("chestPain")
    private String chestPain;
    
    @NotNull(message = "Blood pressure is required")
    @Min(value = 50, message = "Blood pressure must be at least 50")
    @Max(value = 300, message = "Blood pressure must be at most 300")
    @JsonProperty("bloodPressure")
    private Integer bloodPressure;
    
    @NotNull(message = "Cholesterol is required")
    @Min(value = 100, message = "Cholesterol must be at least 100")
    @Max(value = 600, message = "Cholesterol must be at most 600")
    private Integer cholesterol;
    
    @NotNull(message = "Fasting blood sugar is required")
    @Min(value = 0, message = "Fasting blood sugar must be 0 or 1")
    @Max(value = 1, message = "Fasting blood sugar must be 0 or 1")
    @JsonProperty("fastingBS")
    private Integer fastingBS;
    
    @NotBlank(message = "Resting ECG is required")
    @Pattern(regexp = "^(normal|st-t-abnormality|left-ventricular-hypertrophy)$",
             message = "Resting ECG must be one of: normal, st-t-abnormality, left-ventricular-hypertrophy")
    @JsonProperty("restECG")
    private String restECG;
    
    @NotNull(message = "Maximum heart rate is required")
    @Min(value = 60, message = "Maximum heart rate must be at least 60")
    @Max(value = 220, message = "Maximum heart rate must be at most 220")
    @JsonProperty("maxHeartRate")
    private Integer maxHeartRate;
    
    @NotBlank(message = "Exercise angina is required")
    @Pattern(regexp = "^(yes|no)$", message = "Exercise angina must be 'yes' or 'no'")
    @JsonProperty("exerciseAngina")
    private String exerciseAngina;
    
    @NotNull(message = "Oldpeak is required")
    @Min(value = 0, message = "Oldpeak must be at least 0")
    @Max(value = 10, message = "Oldpeak must be at most 10")
    private Double oldpeak;
    
    @NotBlank(message = "Thallium scan is required")
    @Pattern(regexp = "^(normal|fixed-defect|reversible-defect)$",
             message = "Thallium scan must be one of: normal, fixed-defect, reversible-defect")
    private String thallium;
    
    @NotNull(message = "Height is required")
    @Min(value = 0, message = "Height must be positive")
    @Max(value = 3, message = "Height must be at most 3 meters")
    @JsonProperty("height_m")
    private Double heightM;
    
    @NotNull(message = "Weight is required")
    @Min(value = 10, message = "Weight must be at least 10 kg")
    @Max(value = 500, message = "Weight must be at most 500 kg")
    @JsonProperty("weight_kg")
    private Double weightKg;

    // Default constructor
    public SymptomInput() {}

    // Getters and Setters
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }

    public String getChestPain() { return chestPain; }
    public void setChestPain(String chestPain) { this.chestPain = chestPain; }

    public Integer getBloodPressure() { return bloodPressure; }
    public void setBloodPressure(Integer bloodPressure) { this.bloodPressure = bloodPressure; }

    public Integer getCholesterol() { return cholesterol; }
    public void setCholesterol(Integer cholesterol) { this.cholesterol = cholesterol; }

    public Integer getFastingBS() { return fastingBS; }
    public void setFastingBS(Integer fastingBS) { this.fastingBS = fastingBS; }

    public String getRestECG() { return restECG; }
    public void setRestECG(String restECG) { this.restECG = restECG; }

    public Integer getMaxHeartRate() { return maxHeartRate; }
    public void setMaxHeartRate(Integer maxHeartRate) { this.maxHeartRate = maxHeartRate; }

    public String getExerciseAngina() { return exerciseAngina; }
    public void setExerciseAngina(String exerciseAngina) { this.exerciseAngina = exerciseAngina; }

    public Double getOldpeak() { return oldpeak; }
    public void setOldpeak(Double oldpeak) { this.oldpeak = oldpeak; }

    public String getThallium() { return thallium; }
    public void setThallium(String thallium) { this.thallium = thallium; }

    public Double getHeightM() { return heightM; }
    public void setHeightM(Double heightM) { this.heightM = heightM; }

    public Double getWeightKg() { return weightKg; }
    public void setWeightKg(Double weightKg) { this.weightKg = weightKg; }

    // Calculate BMI
    public Double getBmi() {
        if (heightM != null && weightKg != null && heightM > 0) {
            return weightKg / (heightM * heightM);
        }
        return null;
    }
}
