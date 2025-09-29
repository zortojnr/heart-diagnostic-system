<template>
  <div class="error-boundary">
    <!-- Error Toast Notifications -->
    <Transition name="slide-down">
      <div v-if="errors.length > 0" class="error-container">
        <div
          v-for="error in errors"
          :key="error.id"
          :class="['error-toast', `error-${error.type}`]"
        >
          <div class="error-icon">
            <svg v-if="error.type === 'network'" viewBox="0 0 24 24" fill="currentColor">
              <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zM13 17h-2v-6h2v6zm0-8h-2V7h2v2z"/>
            </svg>
            <svg v-else-if="error.type === 'validation'" viewBox="0 0 24 24" fill="currentColor">
              <path d="M1 21h22L12 2 1 21zm12-3h-2v-2h2v2zm0-4h-2v-4h2v4z"/>
            </svg>
            <svg v-else-if="error.type === 'auth'" viewBox="0 0 24 24" fill="currentColor">
              <path d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zm-6 9c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2zm3.1-9H8.9V6c0-1.71 1.39-3.1 3.1-3.1 1.71 0 3.1 1.39 3.1 3.1v2z"/>
            </svg>
            <svg v-else-if="error.type === 'server'" viewBox="0 0 24 24" fill="currentColor">
              <path d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zM9 17H7v-7h2v7zm4 0h-2V7h2v10zm4 0h-2v-4h2v4z"/>
            </svg>
            <svg v-else viewBox="0 0 24 24" fill="currentColor">
              <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
            </svg>
          </div>
          
          <div class="error-content">
            <div class="error-message">{{ error.message }}</div>
            <div v-if="error.details" class="error-details">{{ error.details }}</div>
            <div class="error-timestamp">{{ formatTimestamp(error.timestamp) }}</div>
          </div>
          
          <div class="error-actions">
            <button
              v-if="error.action"
              @click="error.action.handler"
              class="error-action-btn"
            >
              {{ error.action.label }}
            </button>
            <button
              @click="removeError(error.id)"
              class="error-close-btn"
              aria-label="Close error"
            >
              <svg viewBox="0 0 24 24" fill="currentColor">
                <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
              </svg>
            </button>
          </div>
        </div>
      </div>
    </Transition>

    <!-- Global Loading Overlay -->
    <Transition name="fade">
      <div v-if="hasGlobalLoading" class="loading-overlay">
        <div class="loading-content">
          <div class="loading-spinner">
            <svg viewBox="0 0 50 50">
              <circle
                cx="25"
                cy="25"
                r="20"
                fill="none"
                stroke="currentColor"
                stroke-width="4"
                stroke-linecap="round"
                stroke-dasharray="31.416"
                stroke-dashoffset="31.416"
              >
                <animate
                  attributeName="stroke-dasharray"
                  dur="2s"
                  values="0 31.416;15.708 15.708;0 31.416"
                  repeatCount="indefinite"
                />
                <animate
                  attributeName="stroke-dashoffset"
                  dur="2s"
                  values="0;-15.708;-31.416"
                  repeatCount="indefinite"
                />
              </circle>
            </svg>
          </div>
          <div v-if="globalLoadingMessage" class="loading-message">
            {{ globalLoadingMessage }}
          </div>
        </div>
      </div>
    </Transition>

    <!-- Slot for child components -->
    <slot />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useErrorHandler } from '../composables/useErrorHandler'

const { errors, loadingStates, removeError } = useErrorHandler()

const hasGlobalLoading = computed(() => {
  return Object.keys(loadingStates).some(key => 
    key.startsWith('global') && loadingStates[key]?.isLoading
  )
})

const globalLoadingMessage = computed(() => {
  const globalLoadingKey = Object.keys(loadingStates).find(key => 
    key.startsWith('global') && loadingStates[key]?.isLoading
  )
  return globalLoadingKey ? loadingStates[globalLoadingKey]?.message : ''
})

const formatTimestamp = (timestamp: Date) => {
  const now = new Date()
  const diff = now.getTime() - timestamp.getTime()
  
  if (diff < 60000) { // Less than 1 minute
    return 'Just now'
  } else if (diff < 3600000) { // Less than 1 hour
    const minutes = Math.floor(diff / 60000)
    return `${minutes} minute${minutes > 1 ? 's' : ''} ago`
  } else {
    return timestamp.toLocaleTimeString()
  }
}
</script>

<style scoped>
.error-boundary {
  position: relative;
}

.error-container {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 1000;
  max-width: 400px;
  width: 100%;
}

.error-toast {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 16px;
  margin-bottom: 12px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  border-left: 4px solid;
  animation: slideIn 0.3s ease-out;
}

.error-network {
  border-left-color: #f59e0b;
  background: #fef3c7;
}

.error-validation {
  border-left-color: #ef4444;
  background: #fee2e2;
}

.error-auth {
  border-left-color: #8b5cf6;
  background: #ede9fe;
}

.error-server {
  border-left-color: #ef4444;
  background: #fee2e2;
}

.error-unknown {
  border-left-color: #6b7280;
  background: #f3f4f6;
}

.error-icon {
  flex-shrink: 0;
  width: 24px;
  height: 24px;
  margin-top: 2px;
}

.error-icon svg {
  width: 100%;
  height: 100%;
}

.error-network .error-icon {
  color: #d97706;
}

.error-validation .error-icon,
.error-server .error-icon {
  color: #dc2626;
}

.error-auth .error-icon {
  color: #7c3aed;
}

.error-unknown .error-icon {
  color: #4b5563;
}

.error-content {
  flex: 1;
  min-width: 0;
}

.error-message {
  font-weight: 600;
  color: #111827;
  margin-bottom: 4px;
  line-height: 1.4;
}

.error-details {
  font-size: 14px;
  color: #6b7280;
  margin-bottom: 8px;
  line-height: 1.4;
}

.error-timestamp {
  font-size: 12px;
  color: #9ca3af;
}

.error-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
  align-items: flex-end;
}

.error-action-btn {
  padding: 6px 12px;
  background: #3b82f6;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.error-action-btn:hover {
  background: #2563eb;
}

.error-close-btn {
  width: 24px;
  height: 24px;
  padding: 0;
  background: none;
  border: none;
  color: #6b7280;
  cursor: pointer;
  border-radius: 4px;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.error-close-btn:hover {
  background: #f3f4f6;
  color: #374151;
}

.error-close-btn svg {
  width: 16px;
  height: 16px;
}

.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.loading-content {
  text-align: center;
  color: white;
}

.loading-spinner {
  width: 50px;
  height: 50px;
  margin: 0 auto 16px;
  color: #3b82f6;
}

.loading-message {
  font-size: 16px;
  font-weight: 500;
}

/* Transitions */
.slide-down-enter-active,
.slide-down-leave-active {
  transition: all 0.3s ease;
}

.slide-down-enter-from {
  transform: translateY(-100%);
  opacity: 0;
}

.slide-down-leave-to {
  transform: translateY(-100%);
  opacity: 0;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

/* Responsive */
@media (max-width: 640px) {
  .error-container {
    top: 10px;
    right: 10px;
    left: 10px;
    max-width: none;
  }
  
  .error-toast {
    padding: 12px;
  }
  
  .error-actions {
    flex-direction: row;
    align-items: center;
  }
}
</style>