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
        <h1 class="text-3xl font-bold text-white mb-2">Welcome Back</h1>
        <p class="text-primary-100">Sign in to your heart diagnostic account</p>
      </div>

      <!-- Login Card -->
      <div class="card card-elevated bg-gradient-to-br from-blue-50 to-indigo-50 border border-blue-100 shadow-xl">
        <div class="p-8">
          <form @submit.prevent="handleLogin" class="space-y-6">
            <div>
              <label for="email" class="block text-sm font-medium text-gray-800 mb-2">Email Address</label>
              <input
                id="email"
                v-model="email"
                type="email"
                class="w-full px-4 py-3 border border-blue-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-gray-900 bg-white/80 backdrop-blur-sm placeholder-gray-500 shadow-sm"
                placeholder="adebayo@example.com"
                required
                :disabled="authStore.loading"
              />
            </div>

            <div>
              <label for="password" class="block text-sm font-medium text-gray-800 mb-2">Password</label>
              <input
                id="password"
                v-model="password"
                type="password"
                class="w-full px-4 py-3 border border-blue-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-gray-900 bg-white/80 backdrop-blur-sm placeholder-gray-500 shadow-sm"
                placeholder="Enter your password"
                required
                :disabled="authStore.loading"
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

            <button
              type="submit"
              class="btn btn-primary w-full py-3 text-lg font-semibold"
              :disabled="authStore.loading"
            >
              <span v-if="authStore.loading" class="flex items-center justify-center">
                <svg class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
                Signing in...
              </span>
              <span v-else>Sign In</span>
            </button>
          </form>

          <!-- Divider -->
          <div class="relative my-6">
            <div class="absolute inset-0 flex items-center">
              <div class="w-full border-t border-secondary-200"></div>
            </div>
            <div class="relative flex justify-center text-sm">
              <span class="px-2 bg-white text-secondary-500">New to our platform?</span>
            </div>
          </div>

          <!-- Register Link -->
          <div class="text-center">
            <router-link 
              to="/register" 
              class="btn btn-secondary w-full py-3 text-lg font-semibold"
            >
              Create Account
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
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"></path>
            </svg>
            <span class="text-sm">Fast Results</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const email = ref('')
const password = ref('')

const handleLogin = async () => {
  try {
    await authStore.login(email.value, password.value)
    router.push('/')
  } catch (error) {
    // Error is handled by the store
  }
}

onMounted(() => {
  authStore.clearError()
})
</script>

