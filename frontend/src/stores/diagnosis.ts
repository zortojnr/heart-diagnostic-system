import { defineStore } from 'pinia'
import { ref, readonly } from 'vue'
import {
  collection,
  addDoc,
  query,
  where,
  orderBy,
  getDocs,
  serverTimestamp,
} from 'firebase/firestore'
import type {
  DiagnosisResult,
  SymptomInput,
  DiagnosisRecord,
  Patient,
} from '../types'
import { db } from '../firebase'
import { useAuthStore } from './auth'

export const useDiagnosisStore = defineStore('diagnosis', () => {
  const diagnoses = ref<DiagnosisRecord[]>([])
  const patients = ref<Patient[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)

  const authStore = useAuthStore()
  // Using imported db from firebase.ts instead of creating a new instance

  const diagnosticServiceUrl =
    import.meta.env.VITE_DIAGNOSTIC_SERVICE_URL || 'http://localhost:8080'

  const submitDiagnosis = async (
    symptomInput: SymptomInput
  ): Promise<DiagnosisResult> => {
    try {
      loading.value = true
      error.value = null

      // Call diagnostic service
      const response = await fetch(`${diagnosticServiceUrl}/api/diagnose`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(symptomInput),
      })

      if (!response.ok) {
        throw new Error(`Diagnostic service error: ${response.status}`)
      }

      const result: DiagnosisResult = await response.json()
      result.timestamp = new Date()

      // Save diagnosis record to Firestore
      if (authStore.user) {
        const diagnosisRecord: Omit<DiagnosisRecord, 'id'> = {
          patientId: '', // Will be set when patient is created/selected
          inputPayload: symptomInput,
          modelResult: result,
          performedBy: authStore.user.uid,
          createdAt: new Date(),
        }

        const docRef = await addDoc(collection(db, 'diagnoses'), {
          ...diagnosisRecord,
          createdAt: serverTimestamp(),
        })

        const savedRecord: DiagnosisRecord = {
          id: docRef.id,
          ...diagnosisRecord,
        }

        diagnoses.value.unshift(savedRecord)

        // Check for emergency alert
        if (result.label === 'Severe Risk') {
          await createEmergencyAlert(docRef.id, authStore.user.uid)
          // Trigger emergency alert event
          window.dispatchEvent(new CustomEvent('emergency-alert'))
        }
      }

      return result
    } catch (err: any) {
      error.value = err.message || 'Diagnosis failed'
      throw err
    } finally {
      loading.value = false
    }
  }

  const createEmergencyAlert = async (
    diagnosisId: string,
    patientId: string
  ) => {
    try {
      await addDoc(collection(db, 'emergencies'), {
        diagnosisId,
        patientId,
        severity: 'severe',
        timestamp: serverTimestamp(),
        acknowledged: false,
      })
    } catch (err) {
      console.error('Failed to create emergency alert:', err)
    }
  }

  const fetchDiagnosisHistory = async () => {
    if (!authStore.user) return

    try {
      loading.value = true
      error.value = null

      const q = query(
        collection(db, 'diagnoses'),
        where('performedBy', '==', authStore.user.uid),
        orderBy('createdAt', 'desc')
      )

      const querySnapshot = await getDocs(q)
      diagnoses.value = querySnapshot.docs.map((doc) => ({
        id: doc.id,
        ...doc.data(),
        createdAt: doc.data().createdAt?.toDate() || new Date(),
      })) as DiagnosisRecord[]
    } catch (err: any) {
      error.value = err.message || 'Failed to fetch diagnosis history'
      throw err
    } finally {
      loading.value = false
    }
  }

  const createPatient = async (
    patientData: Omit<Patient, 'id' | 'ownerUid' | 'createdAt'>
  ) => {
    if (!authStore.user) throw new Error('User not authenticated')

    try {
      loading.value = true
      error.value = null

      const patient: Omit<Patient, 'id'> = {
        ...patientData,
        ownerUid: authStore.user.uid,
        createdAt: new Date(),
      }

      const docRef = await addDoc(collection(db, 'patients'), {
        ...patient,
        createdAt: serverTimestamp(),
      })

      const savedPatient: Patient = {
        id: docRef.id,
        ...patient,
      }

      patients.value.unshift(savedPatient)
      return savedPatient
    } catch (err: any) {
      error.value = err.message || 'Failed to create patient'
      throw err
    } finally {
      loading.value = false
    }
  }

  const fetchPatients = async () => {
    if (!authStore.user) return

    try {
      loading.value = true
      error.value = null

      const q = query(
        collection(db, 'patients'),
        where('ownerUid', '==', authStore.user.uid),
        orderBy('createdAt', 'desc')
      )

      const querySnapshot = await getDocs(q)
      patients.value = querySnapshot.docs.map((doc) => ({
        id: doc.id,
        ...doc.data(),
        createdAt: doc.data().createdAt?.toDate() || new Date(),
      })) as Patient[]
    } catch (err: any) {
      error.value = err.message || 'Failed to fetch patients'
      throw err
    } finally {
      loading.value = false
    }
  }

  const clearError = () => {
    error.value = null
  }

  return {
    diagnoses: readonly(diagnoses),
    patients: readonly(patients),
    loading: readonly(loading),
    error: readonly(error),
    submitDiagnosis,
    fetchDiagnosisHistory,
    createPatient,
    fetchPatients,
    clearError,
  }
})
