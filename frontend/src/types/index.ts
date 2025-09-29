export interface User {
  uid: string
  email: string
  displayName?: string
  role: 'patient' | 'doctor' | 'admin'
  createdAt: Date
}

export interface Patient {
  id: string
  ownerUid: string
  name: string
  age: number
  gender: 'male' | 'female'
  phone?: string
  email?: string
  height: number // in meters
  weight: number // in kg
  bmi: number
  createdAt: Date
}

export interface SymptomInput {
  age: number
  sex: 'male' | 'female'
  chestPain: 'typical' | 'atypical' | 'non-anginal' | 'asymptomatic'
  bloodPressure: number
  cholesterol: number
  fastingBS: number // 0 or 1
  restECG: 'normal' | 'st-t-abnormality' | 'left-ventricular-hypertrophy'
  maxHeartRate: number
  exerciseAngina: 'yes' | 'no'
  oldpeak: number
  thallium: 'normal' | 'fixed-defect' | 'reversible-defect'
  height_m: number
  weight_kg: number
}

export interface DiagnosisResult {
  label: 'Healthy' | 'Moderate Risk' | 'Severe Risk'
  scores: {
    Healthy: number
    'Moderate Risk': number
    'Severe Risk': number
  }
  explanation?: string
  modelVersion: string
  timestamp: Date
}

export interface DiagnosisRecord {
  id: string
  patientId: string
  inputPayload: SymptomInput
  modelResult: DiagnosisResult
  performedBy: string
  createdAt: Date
}

export interface EmergencyAlert {
  id: string
  patientId: string
  diagnosisId: string
  severity: 'severe'
  timestamp: Date
  acknowledged: boolean
}

export interface BMICategory {
  category: 'Underweight' | 'Normal' | 'Overweight' | 'Obese'
  range: string
  color: string
}
