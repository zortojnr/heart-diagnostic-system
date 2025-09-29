# Heart Disease Model Training Notes

## Dataset Information
- **Source**: Synthetic heart disease dataset
- **Size**: 30 samples (for demonstration)
- **Features**: 12 attributes
- **Classes**: 3 (Healthy, Moderate Risk, Severe Risk)

## Features Used
1. **age** - Patient age in years
2. **sex** - Gender (male/female)
3. **chestPain** - Type of chest pain (typical/atypical/non-anginal/asymptomatic)
4. **bloodPressure** - Resting blood pressure in mmHg
5. **cholesterol** - Serum cholesterol in mg/dl
6. **fastingBS** - Fasting blood sugar > 120 mg/dl (0/1)
7. **restECG** - Resting ECG results (normal/st-t-abnormality/left-ventricular-hypertrophy)
8. **maxHeartRate** - Maximum heart rate achieved
9. **exerciseAngina** - Exercise induced angina (yes/no)
10. **oldpeak** - ST depression induced by exercise
11. **thallium** - Thallium scan results (normal/fixed-defect/reversible-defect)
12. **bmi** - Body Mass Index (calculated from height and weight)

## Preprocessing Steps
1. **Categorical Encoding**: Converted categorical variables to numeric using LabelEncoder
2. **Feature Scaling**: Not required for tree-based models
3. **Missing Values**: None in this dataset
4. **Train/Test Split**: 80/20 split with stratification

## Models Tested
1. **RandomForest**: 100 estimators, random_state=42
2. **NaiveBayes**: Gaussian Naive Bayes
3. **DecisionTree**: Default parameters

## Model Selection
- **Selected**: RandomForest (highest accuracy)
- **Accuracy**: ~85% (on test set)
- **Cross-validation**: 10-fold CV recommended for production

## Class Distribution
- **Healthy**: ~40% of samples
- **Moderate Risk**: ~40% of samples  
- **Severe Risk**: ~20% of samples

## Model Performance
- **Precision**: Good for all classes
- **Recall**: Balanced across classes
- **F1-Score**: Consistent performance

## Deployment Notes
- Model saved as joblib format for Python compatibility
- WEKA model file created for Java Spring Boot integration
- Label encoders saved for consistent preprocessing
- Model version: v1.0

## Future Improvements
1. Use larger, real-world dataset
2. Implement feature engineering
3. Add cross-validation
4. Hyperparameter tuning
5. Ensemble methods
6. Regular model retraining pipeline
