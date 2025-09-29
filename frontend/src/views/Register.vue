<template>
  <div class="min-h-screen bg-gradient-to-br from-primary-600 via-primary-700 to-primary-800 flex items-center justify-center px-4 py-12">
    <div class="w-full max-w-md">
      <!-- Logo/Brand Section -->
      <div class="text-center mb-8">
        <div class="w-16 h-16 bg-white rounded-full flex items-center justify-center mx-auto mb-4 shadow-lg">
          <svg class="w-8 h-8 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"></path>
          </svg>
        </div>
        <h1 class="text-3xl font-bold text-white mb-2">Join Us Today</h1>
        <p class="text-primary-100">Create your heart diagnostic account</p>
      </div>

      <!-- Register Card -->
      <div class="card card-elevated bg-gradient-to-br from-green-50 to-emerald-50 border border-green-100 shadow-xl">
        <div class="p-8">
          <form @submit.prevent="handleRegister" class="space-y-6">
            <div>
              <label class="block text-sm font-medium text-gray-800 mb-2">Full Name</label>
              <input
                v-model="fullName"
                type="text"
                placeholder="Chioma Adebayo"
                class="w-full px-4 py-3 border border-green-200 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent text-gray-900 bg-white/80 backdrop-blur-sm placeholder-gray-500 shadow-sm"
                required
              />
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-800 mb-2">Email</label>
              <input
                v-model="email"
                type="email"
                placeholder="chioma@example.com"
                class="w-full px-4 py-3 border border-green-200 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent text-gray-900 bg-white/80 backdrop-blur-sm placeholder-gray-500 shadow-sm"
                required
              />
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-800 mb-2">Password</label>
              <input
                v-model="password"
                type="password"
                placeholder="Enter your password"
                class="w-full px-4 py-3 border border-green-200 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent text-gray-900 bg-white/80 backdrop-blur-sm placeholder-gray-500 shadow-sm"
                required
              />
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-800 mb-2">Confirm Password</label>
              <input
                v-model="confirmPassword"
                type="password"
                placeholder="Confirm your password"
                class="w-full px-4 py-3 border border-green-200 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent text-gray-900 bg-white/80 backdrop-blur-sm placeholder-gray-500 shadow-sm"
                required
              />
            </div>

            <div v-if="authStore.error" class="bg-danger-50 border border-danger-200 text-danger-700 px-4 py-3 rounded-lg">
              <div class="flex items-center">
                <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                </svg>
                {{ authStore.error }}
              </div>
            </div>

            <!-- Terms and Privacy -->
            <div class="flex items-start">
              <div class="flex items-center h-5">
                <input
                  id="terms"
                  v-model="acceptTerms"
                  type="checkbox"
                  class="w-4 h-4 text-primary-600 bg-gray-100 border-gray-300 rounded focus:ring-primary-500 focus:ring-2"
                  required
                />
              </div>
              <div class="ml-3 text-sm">
                <label for="terms" class="text-secondary-700">
                  I agree to the 
                  <a href="#" class="text-primary-600 hover:text-primary-500 font-medium">Terms of Service</a>
                  and 
                  <a href="#" class="text-primary-600 hover:text-primary-500 font-medium">Privacy Policy</a>
                </label>
              </div>
            </div>

            <button
              type="submit"
              class="btn btn-primary w-full py-3 text-lg font-semibold"
              :disabled="authStore.loading || !acceptTerms"
            >
              <span v-if="authStore.loading" class="flex items-center justify-center">
                <svg class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
                Creating Account...
              </span>
              <span v-else>Create Account</span>
            </button>
          </form>

          <!-- Divider -->
          <div class="relative my-6">
            <div class="absolute inset-0 flex items-center">
              <div class="w-full border-t border-secondary-200"></div>
            </div>
            <div class="relative flex justify-center text-sm">
              <span class="px-2 bg-white text-secondary-500">Already have an account?</span>
            </div>
          </div>

          <!-- Login Link -->
          <div class="text-center">
            <router-link 
              to="/login" 
              class="btn btn-secondary w-full py-3 text-lg font-semibold"
            >
              Sign In Instead
            </router-link>
          </div>
        </div>
      </div>

      <!-- Trust Indicators -->
      <div class="mt-8 text-center">
        <div class="flex items-center justify-center space-x-6 text-primary-100">
          <div class="flex items-center">
            <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"></path>
            </svg>
            <span class="text-sm">Secure</span>
          </div>
          <div class="flex items-center">
            <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"></path>
            </svg>
            <span class="text-sm">HIPAA Compliant</span>
          </div>
          <div class="flex items-center">
            <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"></path>
            </svg>
            <span class="text-sm">Trusted by Doctors</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()

const fullName = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const acceptTerms = ref(false)

const passwordMismatch = computed(() => {
  return password.value !== confirmPassword.value && confirmPassword.value.length > 0
})

const handleRegister = async () => {
  if (passwordMismatch.value) {
    return
  }
  
  if (password.value.length < 6) {
    authStore.error = 'Password must be at least 6 characters long'
    return
  }

  await authStore.register(email.value, password.value, fullName.value)
}
</script>

