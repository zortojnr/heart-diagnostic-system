<template>
  <div class="result-card">
    <!-- Emergency Modal for Severe Cases -->
    <div v-if="showEmergencyModal" class="emergency-modal-overlay" @click="closeEmergencyModal">
      <div class="emergency-modal" @click.stop>
        <div class="emergency-header">
          <div class="emergency-icon">🚨</div>
          <h2>URGENT MEDICAL ATTENTION REQUIRED</h2>
          <button @click="closeEmergencyModal" class="close-btn">&times;</button>
        </div>
        <div class="emergency-content">
          <p class="emergency-message">
            Your assessment indicates a high risk for heart disease. This requires immediate medical evaluation.
          </p>
          <div class="emergency-actions">
            <h3>Take Action Now:</h3>
            <ul>
              <li>📞 Call your doctor immediately</li>
              <li>🏥 Visit the nearest emergency room if experiencing symptoms</li>
              <li>⚠️ Do not ignore these results</li>
              <li>📋 Bring this assessment to your healthcare provider</li>
            </ul>
          </div>
          <div class="emergency-buttons">
            <button @click="callEmergency" class="btn btn-danger">
              📞 Call Emergency Services
            </button>
            <button @click="findHospital" class="btn btn-warning">
              🏥 Find Nearest Hospital
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="result-header">
      <div class="header-content">
        <h3>🔬 Diagnosis Result</h3>
        <div class="result-timestamp">
          <span class="timestamp-icon">🕒</span>
          {{ formatTimestamp(result.timestamp) }}
        </div>
      </div>
      <div class="result-status-badge" :class="riskLevelClass">
        {{ result.label }}
      </div>
    </div>

    <div class="result-main">
      <!-- Risk Level Display -->
      <div class="risk-level-section">
        <div class="risk-level" :class="riskLevelClass">
          <div class="risk-icon">
            <span v-if="result.label === 'Healthy'">✅</span>
            <span v-else-if="result.label === 'Moderate Risk'">⚠️</span>
            <span v-else-if="result.label === 'Severe Risk'">🚨</span>
          </div>
          <div class="risk-content">
            <h2 class="risk-title">{{ result.label }}</h2>
            <p v-if="result.explanation" class="risk-explanation">{{ result.explanation }}</p>
            <div class="risk-summary">
              <span class="summary-label">Risk Assessment:</span>
              <span class="summary-value" :class="riskLevelClass">{{ getRiskDescription() }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Visual Chart Section -->
      <div class="chart-section">
        <div class="section-header">
          <h4>📊 Risk Analysis Visualization</h4>
          <p class="section-description">Visual representation of your heart health assessment</p>
        </div>
        <div class="chart-container">
          <div class="chart-wrapper">
            <canvas ref="chartCanvas" width="400" height="200"></canvas>
          </div>
          <div class="chart-legend">
            <div class="legend-item" v-for="(score, label) in result.scores" :key="label">
              <div class="legend-color" :class="getScoreClass(label)"></div>
              <span class="legend-label">{{ label }}: {{ (score * 100).toFixed(1) }}%</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Enhanced Confidence Scores -->
      <div class="confidence-section">
        <div class="section-header">
          <h4>📊 Confidence Scores</h4>
          <p class="section-description">Model confidence for each risk category</p>
        </div>
        <div class="scores-container">
          <div 
            v-for="(score, label) in result.scores" 
            :key="label"
            class="score-card"
            :class="{ 'score-active': label === result.label }"
          >
            <div class="score-header">
              <div class="score-label">{{ label }}</div>
              <div class="score-percentage">{{ (score * 100).toFixed(1) }}%</div>
            </div>
            <div class="score-bar">
              <div 
                class="score-fill" 
                :style="{ width: `${score * 100}%` }"
                :class="getScoreClass(label)"
              ></div>
            </div>
            <div v-if="label === result.label" class="score-indicator">
              <span class="indicator-text">Primary Diagnosis</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Recommendations -->
      <div class="recommendations-section">
        <div class="section-header">
          <h4>💡 Medical Recommendations</h4>
          <p class="section-description">Suggested next steps based on your assessment</p>
        </div>
        
        <div class="recommendation-card" :class="riskLevelClass">
          <div class="recommendation-header">
            <div class="recommendation-icon">
              <span v-if="result.label === 'Healthy'">🎉</span>
              <span v-else-if="result.label === 'Moderate Risk'">⚠️</span>
              <span v-else-if="result.label === 'Severe Risk'">🚨</span>
            </div>
            <div class="recommendation-title">
              <h5 v-if="result.label === 'Healthy'">Excellent Heart Health!</h5>
              <h5 v-else-if="result.label === 'Moderate Risk'">Attention Required</h5>
              <h5 v-else-if="result.label === 'Severe Risk'">Immediate Action Needed</h5>
            </div>
          </div>
          
          <div class="recommendation-content">
            <p v-if="result.label === 'Healthy'" class="recommendation-summary">
              Your heart health indicators are within normal ranges. Keep up the great work!
            </p>
            <p v-else-if="result.label === 'Moderate Risk'" class="recommendation-summary">
              Some risk factors have been identified. Consider consulting a healthcare provider for further evaluation.
            </p>
            <p v-else-if="result.label === 'Severe Risk'" class="recommendation-summary">
              High risk detected! Immediate medical attention is strongly recommended.
            </p>
            
            <div class="recommendation-actions">
              <h6>Recommended Actions:</h6>
              <ul class="action-list">
                <li v-if="result.label === 'Healthy'" class="action-item healthy">
                  <span class="action-icon">✓</span>
                  Continue maintaining a healthy lifestyle with regular exercise
                </li>
                <li v-if="result.label === 'Healthy'" class="action-item healthy">
                  <span class="action-icon">✓</span>
                  Schedule annual check-ups with your healthcare provider
                </li>
                <li v-if="result.label === 'Healthy'" class="action-item healthy">
                  <span class="action-icon">✓</span>
                  Maintain a balanced diet and monitor your health regularly
                </li>
                
                <li v-if="result.label === 'Moderate Risk'" class="action-item moderate">
                  <span class="action-icon">!</span>
                  Schedule an appointment with a cardiologist within 2-4 weeks
                </li>
                <li v-if="result.label === 'Moderate Risk'" class="action-item moderate">
                  <span class="action-icon">!</span>
                  Monitor blood pressure and heart rate regularly
                </li>
                <li v-if="result.label === 'Moderate Risk'" class="action-item moderate">
                  <span class="action-icon">!</span>
                  Consider lifestyle modifications (diet, exercise, stress management)
                </li>
                <li v-if="result.label === 'Moderate Risk'" class="action-item moderate">
                  <span class="action-icon">!</span>
                  Follow up with healthcare provider in 3-6 months
                </li>
                
                <li v-if="result.label === 'Severe Risk'" class="action-item severe">
                  <span class="action-icon">⚠</span>
                  <strong>Seek immediate medical care - do not delay</strong>
                </li>
                <li v-if="result.label === 'Severe Risk'" class="action-item severe">
                  <span class="action-icon">⚠</span>
                  Call emergency services if experiencing chest pain or shortness of breath
                </li>
                <li v-if="result.label === 'Severe Risk'" class="action-item severe">
                  <span class="action-icon">⚠</span>
                  Schedule urgent cardiology consultation within 24-48 hours
                </li>
                <li v-if="result.label === 'Severe Risk'" class="action-item severe">
                  <span class="action-icon">⚠</span>
                  Avoid strenuous activities until cleared by a physician
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>

      <!-- Additional Information -->
      <div class="additional-info">
        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">Model Version:</span>
            <span class="info-value">{{ result.modelVersion }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">Analysis Date:</span>
            <span class="info-value">{{ formatDate(result.timestamp) }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">Assessment ID:</span>
            <span class="info-value">{{ generateAssessmentId() }}</span>
          </div>
        </div>
        
        <div class="disclaimer">
          <div class="disclaimer-header">
            <span class="disclaimer-icon">ℹ️</span>
            <strong>Important Disclaimer</strong>
          </div>
          <p class="disclaimer-text">
            This assessment is for informational purposes only and should not replace professional medical advice. 
            Always consult with qualified healthcare professionals for proper diagnosis and treatment.
          </p>
        </div>
      </div>
    </div>

    <!-- Action Buttons -->
    <div class="result-actions">
      <button @click="printResult" class="btn btn-secondary" :disabled="isLoading('print-result')">
        <span class="btn-icon">🖨️</span>
        {{ isLoading('print-result') ? 'Preparing...' : 'Print Result' }}
      </button>
      <button @click="saveResult" class="btn btn-primary" :disabled="isLoading('save-result')">
        <span class="btn-icon">💾</span>
        {{ isLoading('save-result') ? 'Saving...' : 'Save to History' }}
      </button>
      <button @click="shareResult" class="btn btn-success" :disabled="isLoading('share-result')">
        <span class="btn-icon">📤</span>
        {{ isLoading('share-result') ? 'Sharing...' : 'Share Result' }}
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, onMounted, nextTick } from 'vue'
import { Chart, type ChartConfiguration, registerables } from 'chart.js'
import type { DiagnosisResult } from '../types'
import { useErrorHandler } from '../composables/useErrorHandler'

// Register Chart.js components
Chart.register(...registerables)

const props = defineProps<{
  result: DiagnosisResult
}>()

const emit = defineEmits<{
  print: []
  save: []
  share: []
}>()

const { withErrorHandling, isLoading } = useErrorHandler()

// Emergency modal state
const showEmergencyModal = ref(false)
const chartCanvas = ref<HTMLCanvasElement | null>(null)
let chartInstance: Chart | null = null

const riskLevelClass = computed(() => {
  switch (props.result.label) {
    case 'Healthy':
      return 'risk-healthy'
    case 'Moderate Risk':
      return 'risk-moderate'
    case 'Severe Risk':
      return 'risk-severe'
    default:
      return 'risk-unknown'
  }
})

const getScoreClass = (label: string) => {
  switch (label) {
    case 'Healthy':
      return 'score-healthy'
    case 'Moderate Risk':
      return 'score-moderate'
    case 'Severe Risk':
      return 'score-severe'
    default:
      return 'score-unknown'
  }
}

const getRiskDescription = () => {
  switch (props.result.label) {
    case 'Healthy':
      return 'Low risk of heart disease'
    case 'Moderate Risk':
      return 'Moderate risk factors present'
    case 'Severe Risk':
      return 'High risk - immediate attention needed'
    default:
      return 'Assessment completed'
  }
}

const formatTimestamp = (timestamp: Date) => {
  return new Intl.DateTimeFormat('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  }).format(timestamp)
}

const formatDate = (timestamp: Date) => {
  return new Intl.DateTimeFormat('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  }).format(timestamp)
}

const generateAssessmentId = () => {
  return `HD-${props.result.timestamp.getTime().toString().slice(-8)}`
}

const printResult = async () => {
  await withErrorHandling(
    async () => {
      emit('print')
    },
    'print-result',
    'Preparing to print...',
    'Print result'
  )
}

const saveResult = async () => {
  await withErrorHandling(
    async () => {
      emit('save')
    },
    'save-result',
    'Saving result...',
    'Save result'
  )
}

const shareResult = async () => {
  await withErrorHandling(
    async () => {
      emit('share')
    },
    'share-result',
    'Preparing to share...',
    'Share result'
  )
}

// Emergency modal methods
const closeEmergencyModal = () => {
  showEmergencyModal.value = false
}

const callEmergency = () => {
  // Open emergency services number
  window.open('tel:911', '_self')
}

const findHospital = () => {
  // Open Google Maps to find nearest hospital
  window.open('https://www.google.com/maps/search/hospital+near+me', '_blank')
}

// Chart initialization
const initializeChart = async () => {
  await nextTick()
  
  if (!chartCanvas.value) return
  
  // Destroy existing chart if it exists
  if (chartInstance) {
    chartInstance.destroy()
  }
  
  const ctx = chartCanvas.value.getContext('2d')
  if (!ctx) return
  
  const labels = Object.keys(props.result.scores)
  const data = Object.values(props.result.scores).map(score => score * 100)
  const colors = labels.map(label => {
    switch (label) {
      case 'Healthy': return '#10b981'
      case 'Moderate Risk': return '#f59e0b'
      case 'Severe Risk': return '#ef4444'
      default: return '#6b7280'
    }
  })
  
  const config: ChartConfiguration = {
    type: 'doughnut',
    data: {
      labels,
      datasets: [{
        data,
        backgroundColor: colors,
        borderWidth: 2,
        borderColor: '#ffffff'
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          display: false
        },
        tooltip: {
          callbacks: {
            label: (context) => {
              return `${context.label}: ${context.parsed.toFixed(1)}%`
            }
          }
        }
      }
    }
  }
  
  chartInstance = new Chart(ctx, config)
}

// Show emergency modal for severe cases
onMounted(() => {
  if (props.result.label === 'Severe Risk') {
    showEmergencyModal.value = true
  }
  initializeChart()
})
</script>

<style scoped>
/* Emergency Modal Styles */
.emergency-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: var(--space-4);
}

.emergency-modal {
  background: white;
  border-radius: var(--radius-xl);
  max-width: 500px;
  width: 100%;
  box-shadow: var(--shadow-2xl);
  animation: modalSlideIn 0.3s ease-out;
}

@keyframes modalSlideIn {
  from {
    opacity: 0;
    transform: translateY(-20px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

.emergency-header {
  background: linear-gradient(135deg, #fee2e2 0%, #fecaca 100%);
  padding: var(--space-6);
  border-radius: var(--radius-xl) var(--radius-xl) 0 0;
  border-bottom: 2px solid #fca5a5;
  position: relative;
  text-align: center;
}

.emergency-icon {
  font-size: 3rem;
  margin-bottom: var(--space-2);
  animation: pulse 1.5s infinite;
}

.emergency-header h2 {
  color: #dc2626;
  font-size: var(--text-xl);
  font-weight: var(--font-bold);
  margin: 0;
}

.close-btn {
  position: absolute;
  top: var(--space-4);
  right: var(--space-4);
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #dc2626;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: background-color 0.2s;
}

.close-btn:hover {
  background: rgba(220, 38, 38, 0.1);
}

.emergency-content {
  padding: var(--space-6);
}

.emergency-message {
  font-size: var(--text-lg);
  color: #374151;
  margin-bottom: var(--space-4);
  text-align: center;
}

.emergency-actions h3 {
  color: #dc2626;
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  margin-bottom: var(--space-3);
}

.emergency-actions ul {
  list-style: none;
  padding: 0;
  margin-bottom: var(--space-6);
}

.emergency-actions li {
  padding: var(--space-2) 0;
  font-size: var(--text-base);
  color: #374151;
}

.emergency-buttons {
  display: flex;
  gap: var(--space-3);
  flex-wrap: wrap;
}

.emergency-buttons .btn {
  flex: 1;
  min-width: 150px;
}

/* Chart Section Styles */
.chart-section {
  margin-bottom: var(--space-8);
  padding: var(--space-6);
  background: var(--color-gray-50);
  border-radius: var(--radius-lg);
  border: 1px solid var(--color-gray-200);
}

.chart-container {
  display: flex;
  gap: var(--space-6);
  align-items: center;
  flex-wrap: wrap;
}

.chart-wrapper {
  flex: 1;
  min-width: 200px;
  height: 200px;
  position: relative;
}

.chart-legend {
  flex: 1;
  min-width: 200px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  margin-bottom: var(--space-2);
}

.legend-color {
  width: 16px;
  height: 16px;
  border-radius: 50%;
}

.legend-color.score-healthy {
  background: #10b981;
}

.legend-color.score-moderate {
  background: #f59e0b;
}

.legend-color.score-severe {
  background: #ef4444;
}

.legend-label {
  font-size: var(--text-sm);
  color: var(--color-gray-700);
  font-weight: var(--font-medium);
}

.result-card {
  background: var(--color-white);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-lg);
  overflow: hidden;
  max-width: 800px;
  margin: 0 auto;
  border: 1px solid var(--border-primary);
}

.result-header {
  background: linear-gradient(135deg, var(--color-primary-50) 0%, var(--color-primary-100) 100%);
  padding: var(--space-6);
  border-bottom: 1px solid var(--color-primary-200);
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: var(--space-4);
}

.header-content h3 {
  margin: 0 0 var(--space-2) 0;
  color: var(--color-primary-700);
  font-size: var(--text-xl);
  font-weight: var(--font-semibold);
}

.result-timestamp {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  color: var(--color-primary-600);
  font-size: var(--text-sm);
}

.timestamp-icon {
  font-size: var(--text-base);
}

.result-status-badge {
  padding: var(--space-2) var(--space-4);
  border-radius: var(--radius-full);
  font-weight: var(--font-semibold);
  font-size: var(--text-sm);
  white-space: nowrap;
}

.result-status-badge.risk-healthy {
  background: var(--color-success-100);
  color: var(--color-success-700);
  border: 1px solid var(--color-success-300);
}

.result-status-badge.risk-moderate {
  background: var(--color-warning-100);
  color: var(--color-warning-700);
  border: 1px solid var(--color-warning-300);
}

.result-status-badge.risk-severe {
  background: var(--color-danger-100);
  color: var(--color-danger-700);
  border: 1px solid var(--color-danger-300);
  animation: pulse 2s infinite;
}

.result-main {
  padding: var(--space-6);
}

.risk-level-section {
  margin-bottom: var(--space-8);
}

.risk-level {
  display: flex;
  align-items: flex-start;
  gap: var(--space-4);
  padding: var(--space-6);
  border-radius: var(--radius-lg);
  border: 2px solid;
}

.risk-level.risk-healthy {
  background: var(--color-success-50);
  border-color: var(--color-success-300);
}

.risk-level.risk-moderate {
  background: var(--color-warning-50);
  border-color: var(--color-warning-300);
}

.risk-level.risk-severe {
  background: var(--color-danger-50);
  border-color: var(--color-danger-300);
}

.risk-icon {
  font-size: 3rem;
  line-height: 1;
}

.risk-content {
  flex: 1;
}

.risk-title {
  margin: 0 0 var(--space-2) 0;
  font-size: var(--text-2xl);
  font-weight: var(--font-bold);
  color: var(--text-primary);
}

.risk-explanation {
  margin: 0 0 var(--space-4) 0;
  color: var(--text-secondary);
  font-size: var(--text-base);
  line-height: var(--leading-relaxed);
}

.risk-summary {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: var(--text-sm);
}

.summary-label {
  color: var(--text-secondary);
  font-weight: var(--font-medium);
}

.summary-value {
  font-weight: var(--font-semibold);
  padding: var(--space-1) var(--space-2);
  border-radius: var(--radius-md);
}

.summary-value.risk-healthy {
  background: var(--color-success-100);
  color: var(--color-success-700);
}

.summary-value.risk-moderate {
  background: var(--color-warning-100);
  color: var(--color-warning-700);
}

.summary-value.risk-severe {
  background: var(--color-danger-100);
  color: var(--color-danger-700);
}

.confidence-section,
.recommendations-section {
  margin-bottom: var(--space-8);
}

.section-header {
  margin-bottom: var(--space-6);
}

.section-header h4 {
  margin: 0 0 var(--space-2) 0;
  color: var(--text-primary);
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
}

.section-description {
  margin: 0;
  color: var(--text-secondary);
  font-size: var(--text-sm);
}

.scores-container {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.score-card {
  padding: var(--space-4);
  border: 1px solid var(--border-primary);
  border-radius: var(--radius-lg);
  transition: all 0.2s ease;
}

.score-card:hover {
  border-color: var(--color-primary-300);
  box-shadow: var(--shadow-sm);
}

.score-active {
  background: var(--color-primary-50);
  border-color: var(--color-primary-300);
  box-shadow: var(--shadow-sm);
}

.score-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-3);
}

.score-label {
  font-weight: var(--font-medium);
  color: var(--text-primary);
}

.score-percentage {
  font-weight: var(--font-bold);
  color: var(--text-primary);
  font-size: var(--text-lg);
}

.score-bar {
  height: 8px;
  background: var(--color-gray-200);
  border-radius: var(--radius-full);
  overflow: hidden;
  margin-bottom: var(--space-2);
}

.score-fill {
  height: 100%;
  transition: width 0.5s ease;
  border-radius: var(--radius-full);
}

.score-healthy {
  background: var(--color-success-500);
}

.score-moderate {
  background: var(--color-warning-500);
}

.score-severe {
  background: var(--color-danger-500);
}

.score-indicator {
  text-align: center;
}

.indicator-text {
  font-size: var(--text-xs);
  color: var(--color-primary-600);
  font-weight: var(--font-medium);
  background: var(--color-primary-100);
  padding: var(--space-1) var(--space-2);
  border-radius: var(--radius-full);
}

.recommendation-card {
  border: 2px solid;
  border-radius: var(--radius-lg);
  padding: var(--space-6);
}

.recommendation-card.risk-healthy {
  background: var(--color-success-50);
  border-color: var(--color-success-300);
}

.recommendation-card.risk-moderate {
  background: var(--color-warning-50);
  border-color: var(--color-warning-300);
}

.recommendation-card.risk-severe {
  background: var(--color-danger-50);
  border-color: var(--color-danger-300);
}

.recommendation-header {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  margin-bottom: var(--space-4);
}

.recommendation-icon {
  font-size: 2rem;
}

.recommendation-title h5 {
  margin: 0;
  font-size: var(--text-xl);
  font-weight: var(--font-bold);
  color: var(--text-primary);
}

.recommendation-summary {
  margin: 0 0 var(--space-4) 0;
  color: var(--text-secondary);
  font-size: var(--text-base);
  line-height: var(--leading-relaxed);
}

.recommendation-actions h6 {
  margin: 0 0 var(--space-3) 0;
  color: var(--text-primary);
  font-size: var(--text-base);
  font-weight: var(--font-semibold);
}

.action-list {
  margin: 0;
  padding: 0;
  list-style: none;
}

.action-item {
  display: flex;
  align-items: flex-start;
  gap: var(--space-3);
  margin-bottom: var(--space-3);
  padding: var(--space-3);
  border-radius: var(--radius-md);
  font-size: var(--text-sm);
  line-height: var(--leading-relaxed);
}

.action-item.healthy {
  background: var(--color-success-100);
  color: var(--color-success-800);
}

.action-item.moderate {
  background: var(--color-warning-100);
  color: var(--color-warning-800);
}

.action-item.severe {
  background: var(--color-danger-100);
  color: var(--color-danger-800);
}

.action-icon {
  font-weight: var(--font-bold);
  font-size: var(--text-base);
  margin-top: 2px;
}

.additional-info {
  margin-bottom: var(--space-6);
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: var(--space-4);
  margin-bottom: var(--space-6);
  padding: var(--space-4);
  background: var(--color-gray-50);
  border-radius: var(--radius-lg);
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: var(--space-1);
}

.info-label {
  font-size: var(--text-xs);
  color: var(--text-secondary);
  font-weight: var(--font-medium);
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.info-value {
  font-size: var(--text-sm);
  color: var(--text-primary);
  font-weight: var(--font-medium);
}

.disclaimer {
  padding: var(--space-4);
  background: var(--color-blue-50);
  border: 1px solid var(--color-blue-200);
  border-radius: var(--radius-lg);
}

.disclaimer-header {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  margin-bottom: var(--space-2);
  color: var(--color-blue-700);
  font-weight: var(--font-semibold);
}

.disclaimer-icon {
  font-size: var(--text-lg);
}

.disclaimer-text {
  margin: 0;
  color: var(--color-blue-600);
  font-size: var(--text-sm);
  line-height: var(--leading-relaxed);
}

.result-actions {
  background: var(--color-gray-50);
  padding: var(--space-6);
  display: flex;
  gap: var(--space-4);
  justify-content: center;
  border-top: 1px solid var(--border-primary);
}

.btn-icon {
  margin-right: var(--space-2);
}

@media (max-width: 768px) {
  .result-header {
    flex-direction: column;
    align-items: stretch;
    gap: var(--space-3);
  }
  
  .result-status-badge {
    align-self: flex-start;
  }
  
  .result-main {
    padding: var(--space-4);
  }
  
  .risk-level {
    flex-direction: column;
    text-align: center;
    gap: var(--space-3);
  }
  
  .risk-icon {
    font-size: 2.5rem;
  }
  
  .info-grid {
    grid-template-columns: 1fr;
  }
  
  .result-actions {
    flex-direction: column;
    padding: var(--space-4);
  }
  
  .recommendation-header {
    flex-direction: column;
    text-align: center;
    gap: var(--space-2);
  }
}

@media print {
  .result-actions {
    display: none;
  }
  
  .result-card {
    box-shadow: none;
    border: 1px solid #000;
  }
}
</style>

