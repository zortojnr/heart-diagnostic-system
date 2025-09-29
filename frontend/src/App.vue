<template>
  <div id="app" class="min-h-screen bg-gray-50">
    <ErrorBoundary>
      <!-- Modern Professional Header -->
      <header class="bg-white shadow-soft border-b border-gray-100">
        <nav class="container">
          <div class="flex items-center justify-between h-16">
            <!-- Brand Logo -->
            <div class="flex items-center space-x-3">
              <div class="flex-shrink-0">
                <img src="/src/assets/logo.svg" alt="HeartCare AI" class="h-10 w-10" />
              </div>
              <div>
                <h1 class="text-xl font-bold text-secondary-800">HeartCare AI</h1>
                <p class="text-xs text-secondary-500 -mt-1">AI-Powered Healthcare</p>
              </div>
            </div>

            <!-- Desktop Navigation -->
            <div class="hidden md:flex items-center space-x-1">
              <router-link 
                to="/" 
                class="nav-link"
                :class="{ 'nav-link-active': $route.path === '/' }"
              >
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
                </svg>
                Home
              </router-link>
              
              <template v-if="!user">
                <router-link 
                  to="/login" 
                  class="nav-link"
                  :class="{ 'nav-link-active': $route.path === '/login' }"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 16l-4-4m0 0l4-4m-4 4h14m-5 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h7a3 3 0 013 3v1" />
                  </svg>
                  Login
                </router-link>
                <router-link 
                  to="/register" 
                  class="btn-primary ml-2"
                >
                  Get Started
                </router-link>
              </template>
              
              <template v-if="user">
                <router-link 
                  to="/diagnose" 
                  class="nav-link"
                  :class="{ 'nav-link-active': $route.path === '/diagnose' }"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                  </svg>
                  Diagnose
                </router-link>
                <router-link 
                  to="/history" 
                  class="nav-link"
                  :class="{ 'nav-link-active': $route.path === '/history' }"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                  History
                </router-link>
                
                <!-- User Menu -->
                <div class="relative ml-4">
                  <button 
                    @click="handleLogout" 
                    class="btn-secondary"
                    :disabled="isLoggingOut"
                  >
                    <svg v-if="!isLoggingOut" class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                    </svg>
                    <svg v-else class="w-4 h-4 mr-2 animate-spin" fill="none" viewBox="0 0 24 24">
                      <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                      <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                    </svg>
                    {{ isLoggingOut ? 'Logging out...' : 'Logout' }}
                  </button>
                </div>
              </template>
            </div>

            <!-- Mobile menu button -->
            <div class="md:hidden">
              <button 
                @click="mobileMenuOpen = !mobileMenuOpen"
                class="p-2 rounded-lg text-secondary-600 hover:text-secondary-800 hover:bg-secondary-100 transition-colors"
              >
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path v-if="!mobileMenuOpen" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
                  <path v-else stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>
          </div>

          <!-- Mobile Navigation -->
          <div v-if="mobileMenuOpen" class="md:hidden border-t border-gray-100 py-4">
            <div class="space-y-2">
              <router-link 
                to="/" 
                class="mobile-nav-link"
                @click="mobileMenuOpen = false"
              >
                Home
              </router-link>
              
              <template v-if="!user">
                <router-link 
                  to="/login" 
                  class="mobile-nav-link"
                  @click="mobileMenuOpen = false"
                >
                  Login
                </router-link>
                <router-link 
                  to="/register" 
                  class="mobile-nav-link"
                  @click="mobileMenuOpen = false"
                >
                  Register
                </router-link>
              </template>
              
              <template v-if="user">
                <router-link 
                  to="/diagnose" 
                  class="mobile-nav-link"
                  @click="mobileMenuOpen = false"
                >
                  Diagnose
                </router-link>
                <router-link 
                  to="/history" 
                  class="mobile-nav-link"
                  @click="mobileMenuOpen = false"
                >
                  History
                </router-link>
                <button 
                  @click="handleLogout" 
                  class="mobile-nav-link text-left w-full"
                  :disabled="isLoggingOut"
                >
                  {{ isLoggingOut ? 'Logging out...' : 'Logout' }}
                </button>
              </template>
            </div>
          </div>
        </nav>
      </header>

      <!-- Main Content -->
      <main class="flex-1">
        <router-view />
      </main>

      <!-- Enhanced Emergency Alert Modal -->
      <div v-if="showEmergencyAlert" class="fixed inset-0 z-50 overflow-y-auto">
        <div class="flex items-center justify-center min-h-screen px-4 pt-4 pb-20 text-center sm:block sm:p-0">
          <div class="fixed inset-0 transition-opacity bg-gray-900 bg-opacity-75" @click="closeEmergencyAlert"></div>
          
          <div class="inline-block w-full max-w-md p-6 my-8 overflow-hidden text-left align-middle transition-all transform bg-white shadow-xl rounded-2xl">
            <div class="flex items-center justify-center w-12 h-12 mx-auto bg-danger-100 rounded-full mb-4">
              <svg class="w-6 h-6 text-danger-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z" />
              </svg>
            </div>
            
            <div class="text-center">
              <h3 class="text-lg font-semibold text-gray-900 mb-2">🚨 EMERGENCY ALERT</h3>
              <p class="text-sm text-gray-600 mb-4">Severe heart disease risk detected!</p>
              <p class="text-sm text-gray-600 mb-6">Please seek immediate medical attention or call emergency services.</p>
              
              <div class="flex flex-col sm:flex-row gap-3 justify-center">
                <button 
                  @click="callEmergency" 
                  class="btn-danger flex-1 sm:flex-none"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
                  </svg>
                  Call Emergency
                </button>
                <button 
                  @click="closeEmergencyAlert" 
                  class="btn-secondary flex-1 sm:flex-none"
                >
                  Understood
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </ErrorBoundary>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from './stores/auth'
import { useErrorHandler } from './composables/useErrorHandler'
import ErrorBoundary from './components/ErrorBoundary.vue'

const router = useRouter()
const authStore = useAuthStore()
const { withErrorHandling, isLoading } = useErrorHandler()
const showEmergencyAlert = ref(false)
const mobileMenuOpen = ref(false)

const user = computed(() => authStore.user)
const isLoggingOut = computed(() => isLoading('logout'))

const handleLogout = async () => {
  await withErrorHandling(
    async () => {
      await authStore.logout()
      mobileMenuOpen.value = false
      router.push('/')
    },
    'logout',
    'Logging out...',
    'User logout'
  )
}

const closeEmergencyAlert = () => {
  showEmergencyAlert.value = false
}

const callEmergency = () => {
  // In a real application, this would integrate with emergency services
  if (navigator.userAgent.includes('Mobile')) {
    window.location.href = 'tel:911'
  } else {
    alert('Please call your local emergency number immediately!')
  }
  showEmergencyAlert.value = false
}

// Listen for emergency alerts
onMounted(() => {
  // This would be connected to the emergency detection system
  window.addEventListener('emergency-alert', () => {
    showEmergencyAlert.value = true
  })
  
  // Global error handler for unhandled promise rejections
  window.addEventListener('unhandledrejection', (event) => {
    console.error('Unhandled promise rejection:', event.reason)
    // Prevent the default browser behavior
    event.preventDefault()
  })
  
  // Global error handler for JavaScript errors
  window.addEventListener('error', (event) => {
    console.error('Global error:', event.error)
  })
})
</script>

<style scoped>
.nav-link {
  @apply flex items-center px-3 py-2 text-sm font-medium text-secondary-600 rounded-lg hover:text-secondary-800 hover:bg-secondary-50 transition-all duration-200;
}

.nav-link-active {
  @apply text-primary-600 bg-primary-50 hover:text-primary-700 hover:bg-primary-100;
}

.mobile-nav-link {
  @apply block px-3 py-2 text-base font-medium text-secondary-600 rounded-lg hover:text-secondary-800 hover:bg-secondary-50 transition-colors;
}

.mobile-nav-link.router-link-active {
  @apply text-primary-600 bg-primary-50;
}

/* Custom scrollbar for mobile menu */
.mobile-menu::-webkit-scrollbar {
  width: 4px;
}

.mobile-menu::-webkit-scrollbar-track {
  background: #f1f5f9;
}

.mobile-menu::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 2px;
}

.mobile-menu::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}
</style>
