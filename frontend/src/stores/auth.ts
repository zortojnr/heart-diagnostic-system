import { defineStore } from 'pinia'
import { ref, computed, readonly } from 'vue'
import { 
  signInWithEmailAndPassword, 
  createUserWithEmailAndPassword,
  signOut,
  onAuthStateChanged,
  type User as FirebaseUser
} from 'firebase/auth'
import { 
  doc, 
  getDoc, 
  setDoc,
  serverTimestamp 
} from 'firebase/firestore'
import type { User } from '../types'
import { auth, db } from '../firebase'

export const useAuthStore = defineStore('auth', () => {
  const user = ref<User | null>(null)
  const loading = ref(false)
  const error = ref<string | null>(null)

  const isAuthenticated = computed(() => !!user.value)
  const isAdmin = computed(() => user.value?.role === 'admin')
  const isDoctor = computed(() => user.value?.role === 'doctor')

  // Initialize auth state listener
  loading.value = true // Start with loading true
  onAuthStateChanged(auth, async (firebaseUser: FirebaseUser | null) => {
    if (firebaseUser) {
      try {
        const userDoc = await getDoc(doc(db, 'users', firebaseUser.uid))
        if (userDoc.exists()) {
          const userData = userDoc.data()
          user.value = {
            uid: firebaseUser.uid,
            email: firebaseUser.email!,
            displayName: firebaseUser.displayName || userData.displayName || '',
            role: userData.role || 'patient',
            createdAt: userData.createdAt?.toDate() || new Date()
          }
        } else {
          // Create user document if it doesn't exist
          await createUserDocument(firebaseUser)
        }
      } catch (err) {
        console.error('Error fetching user data:', err)
        console.error('Error details:', err)
        // Don't set error for permission issues, just create a basic user object
        if (err instanceof Error && err.message.includes('permission')) {
          user.value = {
            uid: firebaseUser.uid,
            email: firebaseUser.email!,
            displayName: firebaseUser.displayName || '',
            role: 'patient',
            createdAt: new Date()
          }
        } else {
          error.value = 'Failed to load user data'
        }
      }
    } else {
      user.value = null
    }
    loading.value = false
  })

  const createUserDocument = async (firebaseUser: FirebaseUser) => {
    try {
      const userData: User = {
        uid: firebaseUser.uid,
        email: firebaseUser.email!,
        displayName: firebaseUser.displayName || '',
        role: 'patient',
        createdAt: new Date()
      }

      // Only save required fields to Firestore to match security rules
      await setDoc(doc(db, 'users', firebaseUser.uid), {
        email: firebaseUser.email!,
        displayName: firebaseUser.displayName || '',
        role: 'patient',
        createdAt: serverTimestamp()
      })

      user.value = userData
    } catch (err) {
      console.error('Error creating user document:', err)
      throw new Error('Failed to create user profile')
    }
  }

  const login = async (email: string, password: string) => {
    try {
      loading.value = true
      error.value = null
      await signInWithEmailAndPassword(auth, email, password)
      
      // Wait for auth state to be properly set
      return new Promise<void>((resolve, reject) => {
        const unsubscribe = onAuthStateChanged(auth, async (firebaseUser) => {
          if (firebaseUser && user.value) {
            unsubscribe()
            resolve()
          } else if (!firebaseUser) {
            unsubscribe()
            reject(new Error('Authentication failed'))
          }
        })
        
        // Timeout after 10 seconds
        setTimeout(() => {
          unsubscribe()
          reject(new Error('Authentication timeout'))
        }, 10000)
      })
    } catch (err: any) {
      error.value = err.message || 'Login failed'
      throw err
    } finally {
      loading.value = false
    }
  }

  const register = async (email: string, password: string, displayName: string, role: 'patient' | 'doctor' = 'patient') => {
    try {
      loading.value = true
      error.value = null
      const userCredential = await createUserWithEmailAndPassword(auth, email, password)
      
      // Update display name using dynamic import to avoid TypeScript issues
      if (displayName) {
        const { updateProfile } = await import('firebase/auth')
        await updateProfile(userCredential.user, { displayName })
      }
      
      // Create user document with role
      await setDoc(doc(db, 'users', userCredential.user.uid), {
        uid: userCredential.user.uid,
        email: userCredential.user.email,
        displayName,
        role,
        createdAt: serverTimestamp()
      })
    } catch (err: any) {
      error.value = err.message || 'Registration failed'
      throw err
    } finally {
      loading.value = false
    }
  }

  const logout = async () => {
    try {
      loading.value = true
      await signOut(auth)
      user.value = null
    } catch (err: any) {
      error.value = err.message || 'Logout failed'
      throw err
    } finally {
      loading.value = false
    }
  }

  const updateProfile = async (displayName: string) => {
    try {
      loading.value = true
      error.value = null
      
      if (!user.value) {
        throw new Error('No user logged in')
      }

      // Update Firebase Auth profile
      const currentUser = auth.currentUser
      if (currentUser && displayName) {
        const { updateProfile } = await import('firebase/auth')
        await updateProfile(currentUser, { displayName })
        
        // Update Firestore document
        await setDoc(doc(db, 'users', currentUser.uid), {
          displayName
        }, { merge: true })

        // Update local user state
        user.value = {
          ...user.value,
          displayName
        }
      }
    } catch (err: any) {
      error.value = err.message || 'Profile update failed'
      throw err
    } finally {
      loading.value = false
    }
  }

  const clearError = () => {
    error.value = null
  }

  return {
    user: readonly(user),
    loading: readonly(loading),
    error: readonly(error),
    isAuthenticated,
    isAdmin,
    isDoctor,
    login,
    register,
    logout,
    updateProfile,
    clearError
  }
})
