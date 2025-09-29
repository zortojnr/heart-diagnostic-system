<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Hero Section -->
    <div class="bg-gradient-to-br from-primary-600 via-primary-700 to-primary-800 text-white">
      <div class="container mx-auto px-4 py-16 lg:py-24">
        <div class="text-center max-w-4xl mx-auto">
          <h1 class="text-4xl lg:text-6xl font-bold mb-6 leading-tight">
            Heart Disease Diagnostic System
          </h1>
          <p class="text-xl lg:text-2xl mb-8 text-primary-100 leading-relaxed">
            Advanced AI-powered heart disease risk assessment using machine learning
          </p>
          <div class="flex flex-col sm:flex-row gap-4 justify-center">
            <router-link 
              v-if="!authStore.isAuthenticated" 
              to="/register" 
              class="btn btn-primary bg-white text-primary-700 hover:bg-primary-50 px-8 py-4 text-lg font-semibold"
            >
              Get Started
            </router-link>
            <router-link 
              v-if="authStore.isAuthenticated" 
              to="/diagnose" 
              class="btn btn-primary bg-white text-primary-700 hover:bg-primary-50 px-8 py-4 text-lg font-semibold"
            >
              Start Diagnosis
            </router-link>
            <router-link 
              v-if="!authStore.isAuthenticated" 
              to="/login" 
              class="btn btn-secondary border-2 border-white text-white hover:bg-white hover:text-primary-700 px-8 py-4 text-lg font-semibold"
            >
              Login
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- Dashboard Section for Authenticated Users -->
    <div v-if="authStore.isAuthenticated" class="container mx-auto px-4 py-12">
      <div class="mb-8">
        <h2 class="text-3xl font-bold text-secondary-800 mb-2">Welcome back!</h2>
        <p class="text-secondary-600">Manage your heart health with our comprehensive diagnostic tools</p>
      </div>

      <!-- Quick Actions -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-12">
        <router-link to="/diagnose" class="card card-elevated group hover:shadow-xl transition-all duration-300">
          <div class="p-6">
            <div class="flex items-center mb-4">
              <div class="w-12 h-12 bg-primary-100 rounded-lg flex items-center justify-center mr-4">
                <svg class="w-6 h-6 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                </svg>
              </div>
              <div>
                <h3 class="text-lg font-semibold text-secondary-800 group-hover:text-primary-600 transition-colors">New Diagnosis</h3>
                <p class="text-secondary-600 text-sm">Start a new heart health assessment</p>
              </div>
            </div>
            <div class="text-right">
              <span class="text-primary-600 font-medium">Start Now →</span>
            </div>
          </div>
        </router-link>

        <router-link to="/history" class="card card-elevated group hover:shadow-xl transition-all duration-300">
          <div class="p-6">
            <div class="flex items-center mb-4">
              <div class="w-12 h-12 bg-secondary-100 rounded-lg flex items-center justify-center mr-4">
                <svg class="w-6 h-6 text-secondary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"></path>
                </svg>
              </div>
              <div>
                <h3 class="text-lg font-semibold text-secondary-800 group-hover:text-primary-600 transition-colors">View History</h3>
                <p class="text-secondary-600 text-sm">Review past diagnostic results</p>
              </div>
            </div>
            <div class="text-right">
              <span class="text-primary-600 font-medium">View All →</span>
            </div>
          </div>
        </router-link>

        <div class="card card-elevated">
          <div class="p-6">
            <div class="flex items-center mb-4">
              <div class="w-12 h-12 bg-success-100 rounded-lg flex items-center justify-center mr-4">
                <svg class="w-6 h-6 text-success-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path>
                </svg>
              </div>
              <div>
                <h3 class="text-lg font-semibold text-secondary-800">Health Overview</h3>
                <p class="text-secondary-600 text-sm">Track your health metrics</p>
              </div>
            </div>
            <div class="text-right">
              <span class="text-secondary-500 font-medium">Coming Soon</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Features Section -->
    <div class="bg-white py-16">
      <div class="container mx-auto px-4">
        <div class="text-center mb-12">
          <h2 class="text-3xl lg:text-4xl font-bold text-secondary-800 mb-4">Key Features</h2>
          <p class="text-xl text-secondary-600 max-w-2xl mx-auto">
            Advanced AI-powered heart disease prediction with comprehensive analysis
          </p>
        </div>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          <div class="text-center p-8 rounded-xl bg-gradient-to-br from-primary-50 to-primary-100 border border-primary-200">
            <div class="w-16 h-16 bg-primary-500 rounded-full flex items-center justify-center mx-auto mb-6">
              <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <h3 class="text-2xl font-bold text-gray-900 mb-4">AI-Powered Analysis</h3>
            <p class="text-gray-700 leading-relaxed">
              Advanced machine learning algorithms analyze your symptoms and medical data to provide accurate risk assessments
            </p>
          </div>

          <div class="text-center p-8 rounded-xl bg-gradient-to-br from-blue-50 to-blue-100 border border-blue-200">
            <div class="w-16 h-16 bg-blue-500 rounded-full flex items-center justify-center mx-auto mb-6">
              <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <h3 class="text-2xl font-bold text-gray-900 mb-4">Quick Results</h3>
            <p class="text-gray-700 leading-relaxed">
              Get comprehensive heart disease risk assessment in under 5 minutes with detailed explanations
            </p>
          </div>

          <div class="text-center p-8 rounded-xl bg-gradient-to-br from-purple-50 to-purple-100 border border-purple-200">
            <div class="w-16 h-16 bg-purple-500 rounded-full flex items-center justify-center mx-auto mb-6">
              <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
              </svg>
            </div>
            <h3 class="text-2xl font-bold text-gray-900 mb-4">Secure & Private</h3>
            <p class="text-gray-700 leading-relaxed">
              Your medical data is encrypted and protected with industry-standard security measures
            </p>
          </div>
        </div>
      </div>
    </div>

    <!-- How It Works Section -->
    <div class="bg-gray-50 py-16">
      <div class="container mx-auto px-4">
        <div class="text-center mb-12">
          <h2 class="text-3xl lg:text-4xl font-bold text-secondary-800 mb-4">How It Works</h2>
          <p class="text-xl text-secondary-600 max-w-2xl mx-auto">
            Simple steps to get your heart health assessment
          </p>
        </div>
        <div class="max-w-4xl mx-auto">
          <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div class="text-center">
              <div class="w-16 h-16 bg-primary-600 text-white rounded-full flex items-center justify-center mx-auto mb-6 text-2xl font-bold">
                1
              </div>
              <h3 class="text-xl font-semibold text-secondary-800 mb-4">Enter Symptoms</h3>
              <p class="text-secondary-600 leading-relaxed">
                Input your symptoms, vital signs, and medical history through our intuitive form
              </p>
            </div>
            <div class="text-center">
              <div class="w-16 h-16 bg-primary-600 text-white rounded-full flex items-center justify-center mx-auto mb-6 text-2xl font-bold">
                2
              </div>
              <h3 class="text-xl font-semibold text-secondary-800 mb-4">AI Analysis</h3>
              <p class="text-secondary-600 leading-relaxed">
                Our advanced machine learning model analyzes your data using proven algorithms
              </p>
            </div>
            <div class="text-center">
              <div class="w-16 h-16 bg-primary-600 text-white rounded-full flex items-center justify-center mx-auto mb-6 text-2xl font-bold">
                3
              </div>
              <h3 class="text-xl font-semibold text-secondary-800 mb-4">Get Results</h3>
              <p class="text-secondary-600 leading-relaxed">
                Receive instant risk assessment with detailed recommendations and next steps
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- CTA Section -->
    <div class="bg-gradient-to-r from-primary-600 to-primary-700 text-white py-16">
      <div class="container mx-auto px-4 text-center">
        <h2 class="text-3xl lg:text-4xl font-bold mb-4">Ready to Get Started?</h2>
        <p class="text-xl mb-8 text-primary-100 max-w-2xl mx-auto">
          Take control of your heart health with our advanced diagnostic system
        </p>
        <router-link 
          v-if="!authStore.isAuthenticated" 
          to="/register" 
          class="btn btn-primary bg-white text-primary-700 hover:bg-primary-50 px-8 py-4 text-lg font-semibold inline-block"
        >
          Create Account
        </router-link>
        <router-link 
          v-if="authStore.isAuthenticated" 
          to="/diagnose" 
          class="btn btn-primary bg-white text-primary-700 hover:bg-primary-50 px-8 py-4 text-lg font-semibold inline-block"
        >
          Start Diagnosis
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useAuthStore } from '../stores/auth'

const authStore = useAuthStore()
</script>

