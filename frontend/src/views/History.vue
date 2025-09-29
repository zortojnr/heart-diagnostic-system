<template>
  <div class="history-page">
    <div class="container">
      <div class="page-header">
        <h1>Diagnosis History</h1>
        <p>View your previous heart disease risk assessments</p>
      </div>

      <div v-if="isLoading('load-history')" class="loading-state">
        <div class="spinner"></div>
        <p>Loading your diagnosis history...</p>
      </div>

      <div v-else-if="diagnosisStore.error" class="error-state">
        <h3>Error Loading History</h3>
        <p>{{ diagnosisStore.error }}</p>
        <button @click="loadHistory" class="btn-primary" :disabled="isLoading('load-history')">
          {{ isLoading('load-history') ? 'Loading...' : 'Try Again' }}
        </button>
      </div>

      <div v-else-if="diagnosisStore.diagnoses.length === 0" class="empty-state">
        <div class="empty-icon">📋</div>
        <h3>No Diagnoses Yet</h3>
        <p>You haven't performed any heart disease risk assessments yet.</p>
        <router-link to="/diagnose" class="btn-primary">
          Start Your First Diagnosis
        </router-link>
      </div>

      <div v-else class="history-content">
        <div class="history-stats">
          <div class="stat-card">
            <div class="stat-number">{{ diagnosisStore.diagnoses.length }}</div>
            <div class="stat-label">Total Diagnoses</div>
          </div>
          <div class="stat-card">
            <div class="stat-number">{{ healthyCount }}</div>
            <div class="stat-label">Healthy Results</div>
          </div>
          <div class="stat-card">
            <div class="stat-number">{{ moderateCount }}</div>
            <div class="stat-label">Moderate Risk</div>
          </div>
          <div class="stat-card">
            <div class="stat-number">{{ severeCount }}</div>
            <div class="stat-label">Severe Risk</div>
          </div>
        </div>

        <div class="history-filters">
          <div class="filter-group">
            <label for="riskFilter" class="filter-label">Filter by Risk Level:</label>
            <select id="riskFilter" v-model="riskFilter" class="filter-select">
              <option value="">All Results</option>
              <option value="Healthy">Healthy</option>
              <option value="Moderate Risk">Moderate Risk</option>
              <option value="Severe Risk">Severe Risk</option>
            </select>
          </div>
          <div class="filter-group">
            <label for="dateFilter" class="filter-label">Sort by Date:</label>
            <select id="dateFilter" v-model="dateSort" class="filter-select">
              <option value="newest">Newest First</option>
              <option value="oldest">Oldest First</option>
            </select>
          </div>
        </div>

        <div class="diagnosis-list">
          <div 
            v-for="diagnosis in filteredDiagnoses" 
            :key="diagnosis.id"
            class="diagnosis-item"
            @click="viewDiagnosis(diagnosis)"
          >
            <div class="diagnosis-header">
              <div class="diagnosis-date">
                {{ formatDate(diagnosis.createdAt) }}
              </div>
              <div class="diagnosis-risk" :class="getRiskClass(diagnosis.modelResult.label)">
                {{ diagnosis.modelResult.label }}
              </div>
            </div>
            
            <div class="diagnosis-details">
              <div class="detail-item">
                <span class="detail-label">Age:</span>
                <span class="detail-value">{{ diagnosis.inputPayload.age }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">Gender:</span>
                <span class="detail-value">{{ diagnosis.inputPayload.sex }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">Blood Pressure:</span>
                <span class="detail-value">{{ diagnosis.inputPayload.bloodPressure }} mmHg</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">Cholesterol:</span>
                <span class="detail-value">{{ diagnosis.inputPayload.cholesterol }} mg/dl</span>
              </div>
            </div>

            <div class="diagnosis-actions">
              <button @click.stop="viewDiagnosis(diagnosis)" class="btn-secondary btn-sm">
                View Details
              </button>
              <button 
                @click.stop="printDiagnosis(diagnosis)" 
                class="btn-secondary btn-sm"
                :disabled="isLoading('print-diagnosis')"
              >
                {{ isLoading('print-diagnosis') ? 'Printing...' : 'Print' }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Diagnosis Detail Modal -->
    <div v-if="selectedDiagnosis" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>Diagnosis Details</h3>
          <button @click="closeModal" class="modal-close">&times;</button>
        </div>
        <div class="modal-body">
          <ResultCard 
            :result="selectedDiagnosis.modelResult"
            @print="() => printDiagnosis(selectedDiagnosis)"
            @save="() => {}"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useDiagnosisStore } from '../stores/diagnosis'
import { useAuthStore } from '../stores/auth'
import { useErrorHandler } from '../composables/useErrorHandler'
import type { DiagnosisRecord } from '../types'
import ResultCard from '../components/ResultCard.vue'

const diagnosisStore = useDiagnosisStore()
const authStore = useAuthStore()
const { withErrorHandling, isLoading } = useErrorHandler()

const riskFilter = ref('')
const dateSort = ref('newest')
const selectedDiagnosis = ref<DiagnosisRecord | null>(null)

const filteredDiagnoses = computed(() => {
  let filtered = [...diagnosisStore.diagnoses]

  // Filter by risk level
  if (riskFilter.value) {
    filtered = filtered.filter(d => d.modelResult.label === riskFilter.value)
  }

  // Sort by date
  filtered.sort((a, b) => {
    const dateA = new Date(a.createdAt)
    const dateB = new Date(b.createdAt)
    return dateSort.value === 'newest' ? dateB.getTime() - dateA.getTime() : dateA.getTime() - dateB.getTime()
  })

  return filtered
})

const healthyCount = computed(() => 
  diagnosisStore.diagnoses.filter(d => d.modelResult.label === 'Healthy').length
)

const moderateCount = computed(() => 
  diagnosisStore.diagnoses.filter(d => d.modelResult.label === 'Moderate Risk').length
)

const severeCount = computed(() => 
  diagnosisStore.diagnoses.filter(d => d.modelResult.label === 'Severe Risk').length
)

const loadHistory = async () => {
  await withErrorHandling(
    async () => {
      await diagnosisStore.fetchDiagnosisHistory()
    },
    'load-history',
    'Loading diagnosis history...',
    'Load diagnosis history'
  )
}

const viewDiagnosis = (diagnosis: DiagnosisRecord) => {
  selectedDiagnosis.value = diagnosis
}

const closeModal = () => {
  selectedDiagnosis.value = null
}

const printDiagnosis = async (diagnosis: DiagnosisRecord) => {
  await withErrorHandling(
    async () => {
      const printWindow = window.open('', '_blank')
      if (!printWindow) {
        throw new Error('Unable to open print window. Please check your browser settings.')
      }

      const printContent = `
        <!DOCTYPE html>
        <html>
        <head>
          <title>Heart Disease Diagnosis Report - ${formatDate(diagnosis.createdAt)}</title>
          <style>
            body { font-family: Arial, sans-serif; margin: 20px; }
            .header { text-align: center; border-bottom: 2px solid #333; padding-bottom: 20px; margin-bottom: 30px; }
            .result { background: #f5f5f5; padding: 20px; border-radius: 8px; margin: 20px 0; }
            .risk-level { font-size: 24px; font-weight: bold; margin: 10px 0; }
            .scores { margin: 20px 0; }
            .score-item { display: flex; justify-content: space-between; margin: 5px 0; }
            .recommendations { margin: 20px 0; }
            .recommendations ul { margin: 10px 0; }
            .footer { margin-top: 30px; font-size: 12px; color: #666; }
          </style>
        </head>
        <body>
          <div class="header">
            <h1>Heart Disease Diagnosis Report</h1>
            <p>Generated on ${formatDate(diagnosis.createdAt)}</p>
          </div>
          
          <div class="result">
            <div class="risk-level">${diagnosis.modelResult.label}</div>
            <p>Model Version: ${diagnosis.modelResult.modelVersion}</p>
          </div>
          
          <div class="scores">
            <h3>Confidence Scores</h3>
            ${Object.entries(diagnosis.modelResult.scores).map(([label, score]) => 
              `<div class="score-item">
                <span>${label}</span>
                <span>${(score * 100).toFixed(1)}%</span>
              </div>`
            ).join('')}
          </div>
          
          <div class="recommendations">
            <h3>Recommendations</h3>
            ${diagnosis.modelResult.label === 'Healthy' ? 
              '<p>Continue maintaining a healthy lifestyle with regular exercise and balanced diet.</p>' :
              diagnosis.modelResult.label === 'Moderate Risk' ?
              '<p>Consider consulting a healthcare provider and schedule regular check-ups.</p>' :
              '<p><strong>Seek immediate medical attention. Do not delay seeking medical care.</strong></p>'
            }
          </div>
          
          <div class="footer">
            <p>This report is generated by an AI diagnostic system and should not replace professional medical advice.</p>
          </div>
        </body>
        </html>
      `

      printWindow.document.write(printContent)
      printWindow.document.close()
      printWindow.print()
    },
    'print-diagnosis',
    'Preparing report for printing...',
    'Print diagnosis report'
  )
}

const formatDate = (date: Date) => {
  return new Intl.DateTimeFormat('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  }).format(date)
}

const getRiskClass = (label: string) => {
  switch (label) {
    case 'Healthy':
      return 'risk-healthy'
    case 'Moderate Risk':
      return 'risk-moderate'
    case 'Severe Risk':
      return 'risk-severe'
    default:
      return ''
  }
}

onMounted(() => {
  if (!authStore.isAuthenticated) {
    // Redirect to login if not authenticated
    return
  }
  loadHistory()
})
</script>

<style scoped>
.history-page {
  min-height: 100vh;
  background: #f8fafc;
  padding: 2rem 0;
}

.page-header {
  text-align: center;
  margin-bottom: 3rem;
}

.page-header h1 {
  color: #1f2937;
  margin-bottom: 0.5rem;
}

.page-header p {
  color: #6b7280;
  font-size: 1.1rem;
}

.loading-state,
.error-state,
.empty-state {
  text-align: center;
  padding: 4rem 2rem;
}

.loading-state .spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #e5e7eb;
  border-top: 4px solid #3b82f6;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.empty-state {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  max-width: 400px;
  margin: 0 auto;
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
}

.history-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 1rem;
  margin-bottom: 2rem;
}

.stat-card {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.stat-number {
  font-size: 2rem;
  font-weight: bold;
  color: #3b82f6;
  margin-bottom: 0.5rem;
}

.stat-label {
  color: #6b7280;
  font-size: 0.875rem;
}

.history-filters {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 2rem;
  display: flex;
  gap: 2rem;
  flex-wrap: wrap;
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.filter-label {
  font-weight: 500;
  color: #374151;
}

.filter-select {
  padding: 0.5rem;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  background: white;
}

.diagnosis-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.diagnosis-item {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 1.5rem;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

.diagnosis-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.diagnosis-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.diagnosis-date {
  color: #6b7280;
  font-size: 0.875rem;
}

.diagnosis-risk {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-weight: 600;
  font-size: 0.875rem;
}

.risk-healthy {
  background: #dcfce7;
  color: #166534;
}

.risk-moderate {
  background: #fef3c7;
  color: #92400e;
}

.risk-severe {
  background: #fecaca;
  color: #991b1b;
}

.diagnosis-details {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 1rem;
  margin-bottom: 1rem;
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.detail-label {
  font-size: 0.875rem;
  color: #6b7280;
  font-weight: 500;
}

.detail-value {
  color: #374151;
  font-weight: 600;
}

.diagnosis-actions {
  display: flex;
  gap: 0.5rem;
  justify-content: flex-end;
}

.btn-sm {
  padding: 0.5rem 1rem;
  font-size: 0.875rem;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 12px;
  max-width: 600px;
  width: 90%;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 1.5rem;
  border-bottom: 1px solid #e5e7eb;
}

.modal-close {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #6b7280;
}

.modal-body {
  padding: 1.5rem;
}

@media (max-width: 768px) {
  .history-filters {
    flex-direction: column;
    gap: 1rem;
  }
  
  .diagnosis-details {
    grid-template-columns: 1fr;
  }
  
  .diagnosis-actions {
    flex-direction: column;
  }
}
</style>

