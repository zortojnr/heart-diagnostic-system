<template>
  <div class="diagnose-page">
    <div class="container">
      <div v-if="!showResult" class="diagnosis-form-container">
        <SymptomForm 
          ref="symptomFormRef"
          @submit="handleDiagnosis"
        />
      </div>

      <div v-if="showResult && diagnosisResult" class="result-container">
        <ResultCard 
          :result="diagnosisResult"
          @print="handlePrint"
          @save="handleSave"
          @share="handleShare"
        />
        
        <div class="result-actions">
          <button @click="handlePrint" class="btn btn-secondary">
            📄 Print Report
          </button>
          <button @click="handleSave" class="btn btn-secondary">
            💾 Save Result
          </button>
          <button @click="handleShare" class="btn btn-secondary">
            📤 Share Report
          </button>
          <button @click="startNewDiagnosis" class="btn btn-primary" :disabled="isLoading('new-diagnosis')">
            🔄 New Diagnosis
          </button>
        </div>
      </div>

      <!-- Loading states are now handled by ErrorBoundary -->
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useDiagnosisStore } from '../stores/diagnosis'
import { useAuthStore } from '../stores/auth'
import { useErrorHandler } from '../composables/useErrorHandler'
import type { SymptomInput, DiagnosisResult } from '../types'
import SymptomForm from '../components/SymptomForm.vue'
import ResultCard from '../components/ResultCard.vue'

const router = useRouter()
const diagnosisStore = useDiagnosisStore()
const authStore = useAuthStore()
const { withErrorHandling, isLoading, addError } = useErrorHandler()

const showResult = ref(false)
const diagnosisResult = ref<DiagnosisResult | null>(null)
const symptomFormRef = ref<InstanceType<typeof SymptomForm> | null>(null)

const handleDiagnosis = async (symptomData: SymptomInput) => {
  const result = await withErrorHandling(
    async () => {
      // Submit diagnosis to the service
      const diagnosisResponse = await diagnosisStore.submitDiagnosis(symptomData)
      
      diagnosisResult.value = diagnosisResponse
      showResult.value = true

      // Check for emergency alert
      if (diagnosisResponse.label === 'Severe Risk') {
        // Trigger emergency alert
        window.dispatchEvent(new CustomEvent('emergency-alert'))
      }

      // Scroll to result
      setTimeout(() => {
        const resultElement = document.querySelector('.result-container')
        if (resultElement) {
          resultElement.scrollIntoView({ behavior: 'smooth' })
        }
      }, 100)

      return diagnosisResponse
    },
    'diagnosis',
    'Analyzing your symptoms...',
    'Heart disease diagnosis'
  )

  return result
}

const startNewDiagnosis = async () => {
  await withErrorHandling(
    async () => {
      showResult.value = false
      diagnosisResult.value = null
      
      // Clear any previous errors
      diagnosisStore.clearError()
      
      // Scroll to top
      window.scrollTo({ top: 0, behavior: 'smooth' })
    },
    'new-diagnosis',
    'Preparing new diagnosis...',
    'Starting new diagnosis'
  )
}

const handlePrint = async () => {
  if (!diagnosisResult.value) return

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
          <title>Heart Disease Diagnosis Report</title>
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
            <p>Generated on ${new Date().toLocaleString()}</p>
          </div>
          
          <div class="result">
            <div class="risk-level">${diagnosisResult.value.label}</div>
            <p>Model Version: ${diagnosisResult.value.modelVersion}</p>
          </div>
          
          <div class="scores">
            <h3>Confidence Scores</h3>
            ${Object.entries(diagnosisResult.value.scores).map(([label, score]) => 
              `<div class="score-item">
                <span>${label}</span>
                <span>${((score as number) * 100).toFixed(1)}%</span>
              </div>`
            ).join('')}
          </div>
          
          <div class="recommendations">
            <h3>Recommendations</h3>
            ${diagnosisResult.value.label === 'Healthy' ? 
              '<p>Continue maintaining a healthy lifestyle with regular exercise and balanced diet.</p>' :
              diagnosisResult.value.label === 'Moderate Risk' ?
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
    'print-report',
    'Preparing report for printing...',
    'Print diagnosis report'
  )
}

const handleSave = async () => {
  await withErrorHandling(
    async () => {
      // The result is already saved when the diagnosis is submitted
      // This could trigger additional actions like showing a success message
      addError({
        type: 'unknown',
        message: 'Report saved successfully',
        details: 'Your diagnosis result has been saved to your history.'
      })
    },
    'save-report',
    'Saving report...',
    'Save diagnosis report'
  )
}

const handleShare = async () => {
  if (!diagnosisResult.value) return

  await withErrorHandling(
    async () => {
      if (navigator.share) {
        await navigator.share({
          title: 'Heart Disease Diagnosis Report',
          text: `Diagnosis Result: ${diagnosisResult.value.label}`,
          url: window.location.href
        })
      } else {
        // Fallback for browsers that don't support Web Share API
        const shareText = `Heart Disease Diagnosis Report\nResult: ${diagnosisResult.value.label}\nGenerated on: ${new Date().toLocaleString()}`
        await navigator.clipboard.writeText(shareText)
        
        addError({
          type: 'unknown',
          message: 'Report copied to clipboard',
          details: 'The diagnosis summary has been copied to your clipboard.'
        })
      }
    },
    'share-report',
    'Preparing to share...',
    'Share diagnosis report'
  )
}

onMounted(() => {
  // Redirect to login if not authenticated
  if (!authStore.isAuthenticated) {
    router.push('/login')
  }
})
</script>

<style scoped>
.diagnose-page {
  min-height: 100vh;
  background: #f8fafc;
  padding: 2rem 0;
}

.diagnosis-form-container {
  max-width: 800px;
  margin: 0 auto;
}

.result-container {
  max-width: 600px;
  margin: 0 auto;
}

.result-actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
  margin-top: 2rem;
}

.loading-overlay {
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

.loading-spinner {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  text-align: center;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
}

.spinner {
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

.error-message {
  background: #fef2f2;
  border: 1px solid #fecaca;
  color: #dc2626;
  padding: 2rem;
  border-radius: 8px;
  text-align: center;
  max-width: 500px;
  margin: 2rem auto;
}

.error-message h3 {
  margin: 0 0 1rem 0;
}

.error-message p {
  margin: 0 0 1rem 0;
}

@media (max-width: 768px) {
  .diagnose-page {
    padding: 1rem 0;
  }
  
  .result-actions {
    flex-direction: column;
    align-items: center;
  }
}
</style>

