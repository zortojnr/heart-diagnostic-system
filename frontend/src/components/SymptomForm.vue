<template>
  <div class="symptom-form">
    <!-- Progress Bar -->
    <div class="progress-container">
      <div class="progress-bar">
        <div 
          class="progress-fill" 
          :style="{ width: `${(currentStep / totalSteps) * 100}%` }"
        ></div>
      </div>
      <div class="step-indicators">
        <div 
          v-for="(step, index) in steps" 
          :key="index"
          class="step-indicator"
          :class="{
            'active': index + 1 === currentStep,
            'completed': index + 1 < currentStep,
            'upcoming': index + 1 > currentStep
          }"
        >
          <div class="step-circle">
            <svg v-if="index + 1 < currentStep" class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
            </svg>
            <span v-else>{{ index + 1 }}</span>
          </div>
          <span class="step-label">{{ step.title }}</span>
        </div>
      </div>
    </div>

    <!-- Form Header -->
    <div class="form-header">
      <h2>🫀 Heart Disease Risk Assessment</h2>
      <p class="form-description">
        Step {{ currentStep }} of {{ totalSteps }}: {{ steps[currentStep - 1].description }}
      </p>
    </div>

    <form @submit.prevent="handleSubmit" class="diagnostic-form">
      <!-- Step 1: Personal Information -->
      <div v-show="currentStep === 1" class="form-step">
        <div class="step-content">
          <div class="step-header">
            <h3>👤 Personal Information</h3>
            <p>Basic demographic and physical information</p>
          </div>
          
          <div class="form-grid">
            <div class="form-group">
              <label for="age" class="block text-sm font-medium text-gray-700 mb-2">Age *</label>
              <input
                id="age"
                v-model.number="formData.age"
                type="number"
                class="input"
                :class="{ 'border-red-500': errors.age }"
                min="1"
                max="120"
                placeholder="e.g., 35"
                @blur="validateField('age')"
              />
              <div v-if="errors.age" class="text-red-500 text-sm mt-1">{{ errors.age }}</div>
              <div class="text-gray-500 text-sm mt-1">Your current age in years</div>
            </div>

            <div class="form-group">
              <label for="sex" class="block text-sm font-medium text-gray-700 mb-2">Gender *</label>
              <select 
                id="sex" 
                v-model="formData.sex" 
                class="input"
                :class="{ 'border-red-500': errors.sex }"
                @change="validateField('sex')"
              >
                <option value="">Select Gender</option>
                <option value="M">Male</option>
                <option value="F">Female</option>
              </select>
              <div v-if="errors.sex" class="text-red-500 text-sm mt-1">{{ errors.sex }}</div>
              <div class="text-gray-500 text-sm mt-1">Biological sex assigned at birth</div>
            </div>
          </div>

          <div class="form-grid">
            <div class="form-group">
              <label for="height" class="block text-sm font-medium text-gray-700 mb-2">Height (meters) *</label>
              <input
                id="height"
                v-model.number="formData.height_m"
                type="number"
                step="0.01"
                class="input"
                :class="{ 'border-red-500': errors.height_m }"
                min="0.5"
                max="3.0"
                placeholder="e.g., 1.75"
                @blur="validateField('height_m')"
              />
              <div v-if="errors.height_m" class="text-red-500 text-sm mt-1">{{ errors.height_m }}</div>
              <div class="text-gray-500 text-sm mt-1">Your height in meters</div>
            </div>

            <div class="form-group">
              <label for="weight" class="block text-sm font-medium text-gray-700 mb-2">Weight (kg) *</label>
              <input
                id="weight"
                v-model.number="formData.weight_kg"
                type="number"
                step="0.1"
                class="input"
                :class="{ 'border-red-500': errors.weight_kg }"
                min="10"
                max="500"
                placeholder="e.g., 70.5"
                @blur="validateField('weight_kg')"
              />
              <div v-if="errors.weight_kg" class="text-red-500 text-sm mt-1">{{ errors.weight_kg }}</div>
              <div class="text-gray-500 text-sm mt-1">Your current weight in kilograms</div>
            </div>
          </div>

          <!-- BMI Widget -->
          <BMIWidget 
            :height="formData.height_m" 
            :weight="formData.weight_kg"
            @update="updateBMI"
          />
        </div>
      </div>

      <!-- Step 2: Symptoms & Medical History -->
      <div v-show="currentStep === 2" class="form-step">
        <div class="step-content">
          <div class="step-header">
            <h3>💓 Symptoms & Medical History</h3>
            <p>Information about symptoms and current medical conditions</p>
          </div>
          
          <div class="form-group">
            <label for="chestPain" class="block text-sm font-medium text-gray-700 mb-2">Chest Pain Type *</label>
            <select 
              id="chestPain" 
              v-model="formData.chestPain" 
              class="input"
              :class="{ 'border-red-500': errors.chestPain }"
              @change="validateField('chestPain')"
            >
              <option value="">Select Type</option>
              <option value="typical">Typical Angina</option>
              <option value="atypical">Atypical Angina</option>
              <option value="non-anginal">Non-Anginal Pain</option>
              <option value="asymptomatic">Asymptomatic</option>
            </select>
            <div v-if="errors.chestPain" class="text-red-500 text-sm mt-1">{{ errors.chestPain }}</div>
            <div class="text-gray-500 text-sm mt-1">Type of chest pain experienced</div>
          </div>
          
          <div class="form-grid">
            <div class="form-group">
              <label for="bloodPressure" class="block text-sm font-medium text-gray-700 mb-2">Resting Blood Pressure *</label>
              <input
                id="bloodPressure"
                v-model.number="formData.bloodPressure"
                type="number"
                class="input"
                :class="{ 'border-red-500': errors.bloodPressure }"
                min="50"
                max="300"
                placeholder="e.g., 120"
                @blur="validateField('bloodPressure')"
              />
              <div v-if="errors.bloodPressure" class="text-red-500 text-sm mt-1">{{ errors.bloodPressure }}</div>
              <div class="text-gray-500 text-sm mt-1">Systolic blood pressure (mmHg)</div>
            </div>

            <div class="form-group">
              <label for="cholesterol" class="block text-sm font-medium text-gray-700 mb-2">Serum Cholesterol *</label>
              <input
                id="cholesterol"
                v-model.number="formData.cholesterol"
                type="number"
                class="input"
                :class="{ 'border-red-500': errors.cholesterol }"
                min="100"
                max="600"
                placeholder="e.g., 200"
                @blur="validateField('cholesterol')"
              />
              <div v-if="errors.cholesterol" class="text-red-500 text-sm mt-1">{{ errors.cholesterol }}</div>
              <div class="text-gray-500 text-sm mt-1">Total cholesterol level (mg/dl)</div>
            </div>
          </div>

          <div class="form-grid">
            <div class="form-group">
              <label for="fastingBS" class="block text-sm font-medium text-gray-700 mb-2">Fasting Blood Sugar *</label>
              <select 
                id="fastingBS" 
                v-model="formData.fastingBS" 
                class="input"
                :class="{ 'border-red-500': errors.fastingBS }"
                @change="validateField('fastingBS')"
              >
                <option value="">Select Level</option>
                <option value="normal">Normal (≤ 120 mg/dl)</option>
                <option value="high">High (> 120 mg/dl)</option>
              </select>
              <div v-if="errors.fastingBS" class="text-red-500 text-sm mt-1">{{ errors.fastingBS }}</div>
              <div class="text-gray-500 text-sm mt-1">Fasting blood sugar level</div>
            </div>

            <div class="form-group">
              <label for="restECG" class="block text-sm font-medium text-gray-700 mb-2">Resting ECG Results *</label>
              <select 
                id="restECG" 
                v-model="formData.restECG" 
                class="input"
                :class="{ 'border-red-500': errors.restECG }"
                @change="validateField('restECG')"
              >
                <option value="">Select Result</option>
                <option value="normal">Normal</option>
                <option value="stt">ST-T Wave Abnormality</option>
                <option value="lvh">Left Ventricular Hypertrophy</option>
              </select>
              <div v-if="errors.restECG" class="text-red-500 text-sm mt-1">{{ errors.restECG }}</div>
              <div class="text-gray-500 text-sm mt-1">Resting electrocardiographic results</div>
            </div>
          </div>
        </div>
      </div>

      <!-- Step 3: Exercise & Test Results -->
      <div v-show="currentStep === 3" class="form-step">
        <div class="step-content">
          <div class="step-header">
            <h3>🏃‍♂️ Exercise & Test Results</h3>
            <p>Exercise tolerance and diagnostic test information</p>
          </div>
          
          <div class="form-grid">
            <div class="form-group">
              <label for="maxHeartRate" class="block text-sm font-medium text-gray-700 mb-2">Maximum Heart Rate *</label>
              <input
                id="maxHeartRate"
                v-model.number="formData.maxHeartRate"
                type="number"
                class="input"
                :class="{ 'border-red-500': errors.maxHeartRate }"
                min="60"
                max="220"
                placeholder="e.g., 150"
                @blur="validateField('maxHeartRate')"
              />
              <div v-if="errors.maxHeartRate" class="text-red-500 text-sm mt-1">{{ errors.maxHeartRate }}</div>
              <div class="text-gray-500 text-sm mt-1">Maximum heart rate achieved during exercise</div>
            </div>

            <div class="form-group">
              <label for="exerciseAngina" class="block text-sm font-medium text-gray-700 mb-2">Exercise Induced Angina *</label>
              <select 
                id="exerciseAngina" 
                v-model="formData.exerciseAngina" 
                class="input"
                :class="{ 'border-red-500': errors.exerciseAngina }"
                @change="validateField('exerciseAngina')"
              >
                <option value="">Select Option</option>
                <option value="yes">Yes</option>
                <option value="no">No</option>
              </select>
              <div v-if="errors.exerciseAngina" class="text-red-500 text-sm mt-1">{{ errors.exerciseAngina }}</div>
              <div class="text-gray-500 text-sm mt-1">Chest pain during exercise</div>
            </div>
          </div>
          
          <div class="form-grid">
            <div class="form-group">
              <label for="oldpeak" class="block text-sm font-medium text-gray-700 mb-2">ST Depression (Oldpeak) *</label>
              <input
                id="oldpeak"
                v-model.number="formData.oldpeak"
                type="number"
                step="0.1"
                class="input"
                :class="{ 'border-red-500': errors.oldpeak }"
                min="0"
                max="10"
                placeholder="e.g., 1.0"
                @blur="validateField('oldpeak')"
              />
              <div v-if="errors.oldpeak" class="text-red-500 text-sm mt-1">{{ errors.oldpeak }}</div>
              <div class="text-gray-500 text-sm mt-1">ST depression induced by exercise</div>
            </div>

            <div class="form-group">
              <label for="thallium" class="block text-sm font-medium text-gray-700 mb-2">Thallium Stress Test *</label>
              <select 
                id="thallium" 
                v-model="formData.thallium" 
                class="input"
                :class="{ 'border-red-500': errors.thallium }"
                @change="validateField('thallium')"
              >
                <option value="">Select Result</option>
                <option value="normal">Normal</option>
                <option value="fixed">Fixed Defect</option>
                <option value="reversible">Reversible Defect</option>
              </select>
              <div v-if="errors.thallium" class="text-red-500 text-sm mt-1">{{ errors.thallium }}</div>
              <div class="text-gray-500 text-sm mt-1">Thallium stress test results</div>
            </div>
          </div>
        </div>
      </div>

      <!-- Step 4: Review & Submit -->
      <div v-show="currentStep === 4" class="form-step">
        <div class="step-content">
          <div class="step-header">
            <h3>📋 Review & Submit</h3>
            <p>Please review your information before submitting</p>
          </div>
          
          <div class="review-sections">
            <div class="review-section">
              <h4 class="text-lg font-semibold text-gray-800 mb-3">Personal Information</h4>
              <div class="review-grid">
                <div class="review-item">
                  <span class="review-label">Age:</span>
                  <span class="review-value">{{ formData.age }} years</span>
                </div>
                <div class="review-item">
                  <span class="review-label">Gender:</span>
                  <span class="review-value">{{ formData.sex === 'M' ? 'Male' : 'Female' }}</span>
                </div>
                <div class="review-item">
                  <span class="review-label">Height:</span>
                  <span class="review-value">{{ formData.height_m }} m</span>
                </div>
                <div class="review-item">
                  <span class="review-label">Weight:</span>
                  <span class="review-value">{{ formData.weight_kg }} kg</span>
                </div>
              </div>
            </div>

            <div class="review-section">
              <h4 class="text-lg font-semibold text-gray-800 mb-3">Medical Information</h4>
              <div class="review-grid">
                <div class="review-item">
                  <span class="review-label">Chest Pain:</span>
                  <span class="review-value">{{ formatChestPain(formData.chestPain) }}</span>
                </div>
                <div class="review-item">
                  <span class="review-label">Blood Pressure:</span>
                  <span class="review-value">{{ formData.bloodPressure }} mmHg</span>
                </div>
                <div class="review-item">
                  <span class="review-label">Cholesterol:</span>
                  <span class="review-value">{{ formData.cholesterol }} mg/dl</span>
                </div>
                <div class="review-item">
                  <span class="review-label">Fasting Blood Sugar:</span>
                  <span class="review-value">{{ formData.fastingBS === 'normal' ? 'Normal' : 'High' }}</span>
                </div>
                <div class="review-item">
                  <span class="review-label">Resting ECG:</span>
                  <span class="review-value">{{ formatECG(formData.restECG) }}</span>
                </div>
              </div>
            </div>

            <div class="review-section">
              <h4 class="text-lg font-semibold text-gray-800 mb-3">Exercise & Tests</h4>
              <div class="review-grid">
                <div class="review-item">
                  <span class="review-label">Max Heart Rate:</span>
                  <span class="review-value">{{ formData.maxHeartRate }} bpm</span>
                </div>
                <div class="review-item">
                  <span class="review-label">Exercise Angina:</span>
                  <span class="review-value">{{ formData.exerciseAngina === 'yes' ? 'Yes' : 'No' }}</span>
                </div>
                <div class="review-item">
                  <span class="review-label">ST Depression:</span>
                  <span class="review-value">{{ formData.oldpeak }}</span>
                </div>
                <div class="review-item">
                  <span class="review-label">Thallium Test:</span>
                  <span class="review-value">{{ formatThallium(formData.thallium) }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Navigation Buttons -->
      <div class="form-navigation">
        <button 
          v-if="currentStep > 1"
          type="button" 
          @click="previousStep" 
          class="btn btn-secondary"
          :disabled="isLoading"
        >
          <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
          </svg>
          Previous
        </button>
        
        <div class="flex-1"></div>
        
        <button 
          v-if="currentStep < totalSteps"
          type="button" 
          @click="nextStep" 
          class="btn btn-primary"
          :disabled="!isCurrentStepValid"
        >
          Next
          <svg class="w-4 h-4 ml-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
          </svg>
        </button>
        
        <button 
          v-if="currentStep === totalSteps"
          type="submit" 
          class="btn btn-primary btn-lg"
          :disabled="isLoading || !isFormValid"
        >
          <span v-if="isLoading" class="loading-text">
            <span class="spinner-sm"></span>
            Analyzing...
          </span>
          <span v-else>
            🔍 Analyze Risk
          </span>
        </button>
      </div>
      
      <div v-if="!isFormValid && hasAttemptedSubmit" class="form-validation-summary">
        <div class="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
          <div class="flex">
            <svg class="w-5 h-5 text-yellow-400 mr-3 mt-0.5" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clip-rule="evenodd" />
            </svg>
            <div>
              <h3 class="text-sm font-medium text-yellow-800">Please complete all required fields</h3>
              <ul class="mt-2 text-sm text-yellow-700 list-disc list-inside">
                <li v-for="error in Object.values(errors)" :key="error">{{ error }}</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, reactive, watch } from 'vue'
import type { SymptomInput } from '../types'
import BMIWidget from './BMIWidget.vue'

const emit = defineEmits<{
  submit: [data: SymptomInput]
}>()

const isLoading = ref(false)
const hasAttemptedSubmit = ref(false)
const currentStep = ref(1)
const totalSteps = 4

const steps = [
  { title: 'Personal Info', description: 'Basic demographic and physical information' },
  { title: 'Symptoms', description: 'Medical history and current symptoms' },
  { title: 'Exercise & Tests', description: 'Exercise tolerance and test results' },
  { title: 'Review', description: 'Review and submit your information' }
]

const formData = reactive<Partial<SymptomInput>>({
  age: undefined,
  sex: '',
  chestPain: '',
  bloodPressure: undefined,
  cholesterol: undefined,
  fastingBS: '',
  restECG: '',
  maxHeartRate: undefined,
  exerciseAngina: '',
  oldpeak: undefined,
  thallium: '',
  height_m: undefined,
  weight_kg: undefined
})

const errors = reactive<Record<string, string>>({})

const validationRules = {
  age: (value: number) => {
    if (!value) return 'Age is required'
    if (value < 1 || value > 120) return 'Age must be between 1 and 120'
    return ''
  },
  sex: (value: string) => {
    if (!value) return 'Gender is required'
    return ''
  },
  height_m: (value: number) => {
    if (!value) return 'Height is required'
    if (value < 0.5 || value > 3.0) return 'Height must be between 0.5 and 3.0 meters'
    return ''
  },
  weight_kg: (value: number) => {
    if (!value) return 'Weight is required'
    if (value < 10 || value > 500) return 'Weight must be between 10 and 500 kg'
    return ''
  },
  chestPain: (value: string) => {
    if (!value) return 'Chest pain type is required'
    return ''
  },
  bloodPressure: (value: number) => {
    if (!value) return 'Blood pressure is required'
    if (value < 50 || value > 300) return 'Blood pressure must be between 50 and 300 mmHg'
    return ''
  },
  cholesterol: (value: number) => {
    if (!value) return 'Cholesterol level is required'
    if (value < 100 || value > 600) return 'Cholesterol must be between 100 and 600 mg/dl'
    return ''
  },
  fastingBS: (value: string) => {
    if (!value) return 'Fasting blood sugar is required'
    return ''
  },
  restECG: (value: string) => {
    if (!value) return 'Resting ECG result is required'
    return ''
  },
  maxHeartRate: (value: number) => {
    if (!value) return 'Maximum heart rate is required'
    if (value < 60 || value > 220) return 'Maximum heart rate must be between 60 and 220'
    return ''
  },
  exerciseAngina: (value: string) => {
    if (!value) return 'Exercise induced angina is required'
    return ''
  },
  oldpeak: (value: number) => {
    if (value === null || value === undefined) return 'ST depression value is required'
    if (value < 0 || value > 10) return 'ST depression must be between 0 and 10'
    return ''
  },
  thallium: (value: string) => {
    if (!value) return 'Thallium test result is required'
    return ''
  }
}

const validateField = (fieldName: keyof typeof validationRules) => {
  const rule = validationRules[fieldName]
  if (rule) {
    const value = formData[fieldName as keyof typeof formData]
    // Type assertion to handle the union type properly
    const error = (rule as (value: any) => string)(value)
    if (error) {
      errors[fieldName] = error
    } else {
      delete errors[fieldName]
    }
  }
}

const validateAllFields = () => {
  Object.keys(validationRules).forEach(field => {
    validateField(field as keyof typeof validationRules)
  })
}

const isFormValid = computed(() => {
  return Object.keys(errors).length === 0 && 
         Object.values(formData).every(value => value !== '' && value !== null && value !== undefined)
})

const getStepFields = (step: number) => {
  switch (step) {
    case 1:
      return ['age', 'sex', 'height_m', 'weight_kg']
    case 2:
      return ['chestPain', 'bloodPressure', 'cholesterol', 'fastingBS', 'restECG']
    case 3:
      return ['maxHeartRate', 'exerciseAngina', 'oldpeak', 'thallium']
    default:
      return []
  }
}

const isCurrentStepValid = computed(() => {
  const stepFields = getStepFields(currentStep.value)
  return stepFields.every(field => {
    const value = formData[field as keyof SymptomInput]
    return value !== '' && value !== null && value !== undefined && !errors[field]
  })
})

const nextStep = () => {
  const stepFields = getStepFields(currentStep.value)
  stepFields.forEach(field => validateField(field as keyof typeof validationRules))
  
  if (isCurrentStepValid.value && currentStep.value < totalSteps) {
    currentStep.value++
  }
}

const previousStep = () => {
  if (currentStep.value > 1) {
    currentStep.value--
  }
}

const handleSubmit = () => {
  hasAttemptedSubmit.value = true
  validateAllFields()
  
  if (isFormValid.value) {
    emit('submit', formData as SymptomInput)
  }
}

const resetForm = () => {
  Object.keys(formData).forEach(key => {
    const typedKey = key as keyof typeof formData
    if (typeof formData[typedKey] === 'number') {
      (formData as any)[key] = undefined
    } else {
      (formData as any)[key] = ''
    }
  })
  Object.keys(errors).forEach(key => delete errors[key])
  hasAttemptedSubmit.value = false
  currentStep.value = 1
}

const updateBMI = (bmiData: any) => {
  console.log('BMI updated:', bmiData)
}

const setLoading = (loading: boolean) => {
  isLoading.value = loading
}

// Format functions for review step
const formatChestPain = (value: string) => {
  const map: Record<string, string> = {
    'typical': 'Typical Angina',
    'atypical': 'Atypical Angina',
    'non-anginal': 'Non-Anginal Pain',
    'asymptomatic': 'Asymptomatic'
  }
  return map[value] || value
}

const formatECG = (value: string) => {
  const map: Record<string, string> = {
    'normal': 'Normal',
    'stt': 'ST-T Wave Abnormality',
    'lvh': 'Left Ventricular Hypertrophy'
  }
  return map[value] || value
}

const formatThallium = (value: string) => {
  const map: Record<string, string> = {
    'normal': 'Normal',
    'fixed': 'Fixed Defect',
    'reversible': 'Reversible Defect'
  }
  return map[value] || value
}

// Watch for form changes to clear errors
watch(formData, () => {
  if (hasAttemptedSubmit.value) {
    validateAllFields()
  }
}, { deep: true })

defineExpose({
  setLoading,
  resetForm
})
</script>

<style scoped>
.symptom-form {
  @apply max-w-4xl mx-auto;
}

.progress-container {
  @apply mb-8;
}

.progress-bar {
  @apply w-full bg-gray-200 rounded-full h-2 mb-6;
}

.progress-fill {
  @apply bg-primary-500 h-2 rounded-full transition-all duration-300 ease-in-out;
}

.step-indicators {
  @apply flex justify-between items-center;
}

.step-indicator {
  @apply flex flex-col items-center text-center;
}

.step-circle {
  @apply w-10 h-10 rounded-full flex items-center justify-center text-sm font-medium mb-2 transition-all duration-200;
}

.step-indicator.upcoming .step-circle {
  @apply bg-gray-200 text-gray-500;
}

.step-indicator.active .step-circle {
  @apply bg-primary-500 text-white;
}

.step-indicator.completed .step-circle {
  @apply bg-green-500 text-white;
}

.step-label {
  @apply text-sm font-medium;
}

.step-indicator.upcoming .step-label {
  @apply text-gray-500;
}

.step-indicator.active .step-label {
  @apply text-primary-600;
}

.step-indicator.completed .step-label {
  @apply text-green-600;
}

.form-header {
  @apply text-center mb-8 p-6 bg-gradient-to-r from-primary-50 to-primary-100 rounded-xl border border-primary-200;
}

.form-header h2 {
  @apply text-2xl font-bold text-primary-700 mb-3;
}

.form-description {
  @apply text-gray-600 text-lg;
}

.form-step {
  @apply min-h-96;
}

.step-content {
  @apply space-y-6;
}

.step-header {
  @apply text-center mb-6;
}

.step-header h3 {
  @apply text-xl font-semibold text-gray-800 mb-2;
}

.step-header p {
  @apply text-gray-600;
}

.form-grid {
  @apply grid grid-cols-1 md:grid-cols-2 gap-6;
}

.form-group {
  @apply space-y-2;
}

.review-sections {
  @apply space-y-6;
}

.review-section {
  @apply bg-gray-50 rounded-lg p-4;
}

.review-grid {
  @apply grid grid-cols-1 md:grid-cols-2 gap-3;
}

.review-item {
  @apply flex justify-between items-center py-2 border-b border-gray-200 last:border-b-0;
}

.review-label {
  @apply font-medium text-gray-700;
}

.review-value {
  @apply text-gray-900;
}

.form-navigation {
  @apply flex items-center justify-between mt-8 pt-6 border-t border-gray-200;
}

.loading-text {
  @apply flex items-center gap-2;
}

.spinner-sm {
  @apply w-4 h-4 border-2 border-transparent border-t-current rounded-full animate-spin;
}

.form-validation-summary {
  @apply mt-6;
}

@media (max-width: 768px) {
  .step-indicators {
    @apply flex-wrap gap-4;
  }
  
  .step-indicator {
    @apply flex-1 min-w-0;
  }
  
  .step-label {
    @apply text-xs;
  }
  
  .form-navigation {
    @apply flex-col gap-4;
  }
}
</style>

