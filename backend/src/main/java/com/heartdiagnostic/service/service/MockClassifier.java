package com.heartdiagnostic.service.service;

import weka.classifiers.AbstractClassifier;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;

public class MockClassifier extends AbstractClassifier {
    
    public void buildClassifier(Instances data) throws Exception {
        // Mock implementation - no actual training needed
    }
    
    public double classifyInstance(Instance instance) throws Exception {
        // Simple rule-based classification for demo
        double age = instance.value(0);
        double cholesterol = instance.value(4);
        double maxHeartRate = instance.value(7);
        double bloodPressure = instance.value(3);
        
        // Simple decision rules
        if (age <= 50) {
            if (cholesterol <= 200) {
                return 0.0; // Healthy
            } else if (bloodPressure <= 130) {
                return 1.0; // Moderate Risk
            } else {
                return 2.0; // Severe Risk
            }
        } else {
            if (maxHeartRate <= 130) {
                return 2.0; // Severe Risk
            } else {
                return 1.0; // Moderate Risk
            }
        }
    }
    
    public double[] distributionForInstance(Instance instance) throws Exception {
        double[] distribution = new double[3]; // 3 classes
        int predictedClass = (int) classifyInstance(instance);
        
        // Set confidence scores
        distribution[predictedClass] = 0.85; // High confidence for predicted class
        
        // Distribute remaining probability among other classes
        double remaining = 0.15;
        for (int i = 0; i < distribution.length; i++) {
            if (i != predictedClass) {
                distribution[i] = remaining / (distribution.length - 1);
            }
        }
        
        return distribution;
    }
    
    public Capabilities getCapabilities() {
        Capabilities result = new Capabilities(this);
        result.disableAll();
        
        // Enable what we can handle
        result.enable(Capabilities.Capability.NOMINAL_ATTRIBUTES);
        result.enable(Capabilities.Capability.NUMERIC_ATTRIBUTES);
        result.enable(Capabilities.Capability.NOMINAL_CLASS);
        result.enable(Capabilities.Capability.MISSING_VALUES);
        
        return result;
    }
}