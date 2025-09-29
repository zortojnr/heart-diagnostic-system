package com.heartdiagnostic.service;

import com.heartdiagnostic.service.controller.DiagnosticController;
import com.heartdiagnostic.service.model.DiagnosisResult;
import com.heartdiagnostic.service.model.SymptomInput;
import com.heartdiagnostic.service.service.WekaModelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DiagnosticControllerTest {

    @Mock
    private WekaModelService wekaModelService;

    private DiagnosticController diagnosticController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        diagnosticController = new DiagnosticController();
        // Inject the mock service (in real app, this would be done by Spring)
        // For this test, we'll test the controller logic directly
    }

    @Test
    void testDiagnose_Success() throws Exception {
        // Given
        SymptomInput input = createValidSymptomInput();
        DiagnosisResult expectedResult = createExpectedResult();
        
        when(wekaModelService.isModelLoaded()).thenReturn(true);
        when(wekaModelService.classify(input)).thenReturn(expectedResult);

        // When
        ResponseEntity<DiagnosisResult> response = diagnosticController.diagnose(input);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Moderate Risk", response.getBody().getLabel());
    }

    @Test
    void testDiagnose_ModelNotLoaded() {
        // Given
        SymptomInput input = createValidSymptomInput();
        when(wekaModelService.isModelLoaded()).thenReturn(false);

        // When
        ResponseEntity<DiagnosisResult> response = diagnosticController.diagnose(input);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getLabel());
    }

    @Test
    void testHealth() {
        // Given
        when(wekaModelService.isModelLoaded()).thenReturn(true);
        when(wekaModelService.getModelInfo()).thenReturn("RandomForest (v1.0)");

        // When
        ResponseEntity<Map<String, Object>> response = diagnosticController.health();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().get("status"));
        assertTrue((Boolean) response.getBody().get("modelLoaded"));
    }

    private SymptomInput createValidSymptomInput() {
        SymptomInput input = new SymptomInput();
        input.setAge(45);
        input.setSex("male");
        input.setChestPain("typical");
        input.setBloodPressure(140);
        input.setCholesterol(230);
        input.setFastingBS(1);
        input.setRestECG("normal");
        input.setMaxHeartRate(150);
        input.setExerciseAngina("no");
        input.setOldpeak(2.3);
        input.setThallium("normal");
        input.setHeightM(1.75);
        input.setWeightKg(70.0);
        return input;
    }

    private DiagnosisResult createExpectedResult() {
        DiagnosisResult result = new DiagnosisResult();
        result.setLabel("Moderate Risk");
        
        Map<String, Double> scores = new HashMap<>();
        scores.put("Healthy", 0.12);
        scores.put("Moderate Risk", 0.78);
        scores.put("Severe Risk", 0.10);
        result.setScores(scores);
        
        result.setExplanation("Key risk factors identified: high blood pressure, high cholesterol. Confidence: 78.0%");
        return result;
    }
}
