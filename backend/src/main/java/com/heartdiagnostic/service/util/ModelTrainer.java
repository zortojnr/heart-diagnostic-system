package com.heartdiagnostic.service.util;

import weka.classifiers.trees.J48;
import weka.core.*;
import weka.core.converters.CSVLoader;
import java.io.*;
import java.util.ArrayList;

public class ModelTrainer {
    
    public static void main(String[] args) {
        try {
            // Create the dataset structure
            Instances dataset = createDataset();
            
            // Add sample data
            addSampleData(dataset);
            
            // Train the model
            J48 classifier = new J48();
            classifier.buildClassifier(dataset);
            
            // Save the model
            String modelPath = "src/main/resources/models/heart-model.model";
            weka.core.SerializationHelper.write(modelPath, classifier);
            
            System.out.println("WEKA model trained and saved successfully to: " + modelPath);
            System.out.println("Model: " + classifier.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static Instances createDataset() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        
        // Add numeric attributes
        attributes.add(new Attribute("age"));
        attributes.add(new Attribute("sex"));
        attributes.add(new Attribute("chestPain"));
        attributes.add(new Attribute("bloodPressure"));
        attributes.add(new Attribute("cholesterol"));
        attributes.add(new Attribute("fastingBS"));
        attributes.add(new Attribute("restECG"));
        attributes.add(new Attribute("maxHeartRate"));
        attributes.add(new Attribute("exerciseAngina"));
        attributes.add(new Attribute("oldpeak"));
        attributes.add(new Attribute("thallium"));
        attributes.add(new Attribute("bmi"));
        
        // Add class attribute
        ArrayList<String> classValues = new ArrayList<>();
        classValues.add("Healthy");
        classValues.add("Moderate Risk");
        classValues.add("Severe Risk");
        attributes.add(new Attribute("class", classValues));
        
        // Create dataset
        Instances dataset = new Instances("HeartDisease", attributes, 0);
        dataset.setClassIndex(dataset.numAttributes() - 1);
        
        return dataset;
    }
    
    private static void addSampleData(Instances dataset) {
        // Add some sample training data
        double[][] sampleData = {
            {45, 1, 1, 120, 200, 0, 0, 150, 0, 1.0, 1, 25.0, 0}, // Healthy
            {55, 0, 2, 140, 250, 1, 1, 130, 1, 2.0, 2, 28.0, 1}, // Moderate Risk
            {65, 1, 3, 160, 300, 1, 1, 110, 1, 3.0, 3, 32.0, 2}, // Severe Risk
            {35, 0, 0, 110, 180, 0, 0, 170, 0, 0.5, 1, 22.0, 0}, // Healthy
            {50, 1, 2, 130, 220, 0, 1, 140, 0, 1.5, 2, 26.0, 1}, // Moderate Risk
            {60, 0, 3, 150, 280, 1, 1, 120, 1, 2.5, 3, 30.0, 2}, // Severe Risk
            {40, 1, 1, 115, 190, 0, 0, 160, 0, 0.8, 1, 24.0, 0}, // Healthy
            {58, 0, 2, 135, 240, 1, 1, 125, 1, 2.2, 2, 29.0, 1}, // Moderate Risk
            {70, 1, 3, 170, 320, 1, 1, 100, 1, 3.5, 3, 35.0, 2}, // Severe Risk
            {30, 0, 0, 100, 160, 0, 0, 180, 0, 0.2, 1, 20.0, 0}  // Healthy
        };
        
        for (double[] row : sampleData) {
            DenseInstance instance = new DenseInstance(1.0, row);
            dataset.add(instance);
        }
    }
}