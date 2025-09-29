package com.heartdiagnostic.service.service;

import com.heartdiagnostic.service.model.DiagnosisResult;
import com.heartdiagnostic.service.model.SymptomInput;
import weka.classifiers.Classifier;
import weka.core.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.*;

@Service
public class WekaModelService {
    
    private Classifier classifier;
    private Instances dataStructure;
    private ArrayList<Attribute> attributes;
    
    @Value("${weka.model.path:classpath:models/heart-model.model}")
    private String modelPath;
    
    private final ResourceLoader resourceLoader;
    
    public WekaModelService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    
    @PostConstruct
    public void initializeModel() throws Exception {
        // For demo purposes, create a simple mock classifier
        // In production, you would load a real trained WEKA model
        classifier = new MockClassifier();
        
        // Create the data structure that matches the training data
        createDataStructure();
        
        System.out.println("Mock WEKA model initialized successfully for demo purposes");
    }
    
    private void createDataStructure() {
        attributes = new ArrayList<>();
        
        // Add attributes in the same order as training data
        attributes.add(new Attribute("age"));
        attributes.add(new Attribute("sex", Arrays.asList("male", "female")));
        attributes.add(new Attribute("chestPain", Arrays.asList("typical", "atypical", "non-anginal", "asymptomatic")));
        attributes.add(new Attribute("bloodPressure"));
        attributes.add(new Attribute("cholesterol"));
        attributes.add(new Attribute("fastingBS", Arrays.asList("0", "1")));
        attributes.add(new Attribute("restECG", Arrays.asList("normal", "st-t-abnormality", "left-ventricular-hypertrophy")));
        attributes.add(new Attribute("maxHeartRate"));
        attributes.add(new Attribute("exerciseAngina", Arrays.asList("no", "yes")));
        attributes.add(new Attribute("oldpeak"));
        attributes.add(new Attribute("thallium", Arrays.asList("normal", "fixed-defect", "reversible-defect")));
        attributes.add(new Attribute("bmi"));
        
        // Add class attribute
        List<String> classValues = Arrays.asList("Healthy", "Moderate Risk", "Severe Risk");
        attributes.add(new Attribute("class", classValues));
        
        // Create instances structure
        dataStructure = new Instances("HeartDisease", attributes, 0);
        dataStructure.setClassIndex(dataStructure.numAttributes() - 1);
    }
    
    public DiagnosisResult classify(SymptomInput input) throws Exception {
        // Create a new instance
        Instance instance = new DenseInstance(dataStructure.numAttributes());
        instance.setDataset(dataStructure);
        
        // Set attribute values
        instance.setValue(attributes.get(0), input.getAge());
        instance.setValue(attributes.get(1), input.getSex());
        instance.setValue(attributes.get(2), input.getChestPain());
        instance.setValue(attributes.get(3), input.getBloodPressure());
        instance.setValue(attributes.get(4), input.getCholesterol());
        instance.setValue(attributes.get(5), input.getFastingBS().toString());
        instance.setValue(attributes.get(6), input.getRestECG());
        instance.setValue(attributes.get(7), input.getMaxHeartRate());
        instance.setValue(attributes.get(8), input.getExerciseAngina());
        instance.setValue(attributes.get(9), input.getOldpeak());
        instance.setValue(attributes.get(10), input.getThallium());
        instance.setValue(attributes.get(11), input.getBmi());
        
        // Classify the instance
        double[] distribution = classifier.distributionForInstance(instance);
        int predictedClass = (int) classifier.classifyInstance(instance);
        
        // Get the predicted label
        String predictedLabel = dataStructure.classAttribute().value(predictedClass);
        
        // Create scores map
        Map<String, Double> scores = new HashMap<>();
        for (int i = 0; i < distribution.length; i++) {
            String className = dataStructure.classAttribute().value(i);
            scores.put(className, distribution[i]);
        }
        
        // Generate explanation
        String explanation = generateExplanation(predictedLabel, scores, input);
        
        return new DiagnosisResult(predictedLabel, scores, explanation);
    }
    
    private String generateExplanation(String predictedLabel, Map<String, Double> scores, SymptomInput input) {
        StringBuilder explanation = new StringBuilder();
        
        // Get the top contributing factors
        List<Map.Entry<String, Double>> sortedScores = new ArrayList<>(scores.entrySet());
        sortedScores.sort(Map.Entry.<String, Double>comparingByValue().reversed());
        
        explanation.append("Based on your symptoms: ");
        
        // Add risk factors
        List<String> riskFactors = new ArrayList<>();
        
        if (input.getAge() > 65) riskFactors.add("age over 65");
        if (input.getBloodPressure() > 140) riskFactors.add("high blood pressure");
        if (input.getCholesterol() > 240) riskFactors.add("high cholesterol");
        if (input.getFastingBS() == 1) riskFactors.add("elevated blood sugar");
        if (input.getExerciseAngina().equals("yes")) riskFactors.add("exercise-induced angina");
        if (input.getOldpeak() > 2.0) riskFactors.add("significant ST depression");
        if (input.getThallium().equals("fixed-defect") || input.getThallium().equals("reversible-defect")) {
            riskFactors.add("thallium scan abnormalities");
        }
        
        if (!riskFactors.isEmpty()) {
            explanation.append("Key risk factors identified: ").append(String.join(", ", riskFactors));
        } else {
            explanation.append("No major risk factors identified");
        }
        
        explanation.append(". Confidence: ").append(String.format("%.1f%%", scores.get(predictedLabel) * 100));
        
        return explanation.toString();
    }
    
    public boolean isModelLoaded() {
        return classifier != null && dataStructure != null;
    }
    
    public String getModelInfo() {
        if (classifier != null) {
            return classifier.getClass().getSimpleName() + " (v1.0)";
        }
        return "Model not loaded";
    }
}
