import { ref, reactive } from 'vue'

export interface AppError {
  id: string
  type: 'network' | 'validation' | 'auth' | 'server' | 'unknown'
  message: string
  details?: string
  timestamp: Date
  action?: {
    label: string
    handler: () => void
  }
}

export interface LoadingState {
  isLoading: boolean
  message?: string
}

const errors = ref<AppError[]>([])
const loadingStates = reactive<Record<string, LoadingState>>({})

export function useErrorHandler() {
  const addError = (error: Omit<AppError, 'id' | 'timestamp'>) => {
    const appError: AppError = {
      ...error,
      id: `error_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`,
      timestamp: new Date()
    }
    
    errors.value.push(appError)
    
    // Auto-remove error after 10 seconds unless it's a critical error
    if (error.type !== 'server' && error.type !== 'auth') {
      setTimeout(() => {
        removeError(appError.id)
      }, 10000)
    }
    
    return appError.id
  }

  const removeError = (errorId: string) => {
    const index = errors.value.findIndex(error => error.id === errorId)
    if (index > -1) {
      errors.value.splice(index, 1)
    }
  }

  const clearAllErrors = () => {
    errors.value.length = 0
  }

  const setLoading = (key: string, loading: boolean, message?: string) => {
    if (loading) {
      loadingStates[key] = { isLoading: true, message }
    } else {
      delete loadingStates[key]
    }
  }

  const isLoading = (key?: string) => {
    if (key) {
      return loadingStates[key]?.isLoading || false
    }
    return Object.keys(loadingStates).length > 0
  }

  const getLoadingMessage = (key: string) => {
    return loadingStates[key]?.message
  }

  const handleApiError = (error: any, context?: string) => {
    console.error('API Error:', error, context)
    
    let errorType: AppError['type'] = 'unknown'
    let message = 'An unexpected error occurred'
    let details = ''
    let action: AppError['action'] | undefined

    if (error.response) {
      // Server responded with error status
      const status = error.response.status
      const data = error.response.data

      switch (status) {
        case 400:
          errorType = 'validation'
          message = data?.message || 'Invalid request data'
          details = data?.details || 'Please check your input and try again'
          break
        case 401:
          errorType = 'auth'
          message = 'Authentication required'
          details = 'Please log in to continue'
          action = {
            label: 'Login',
            handler: () => {
              // This would trigger navigation to login
              window.location.href = '/login'
            }
          }
          break
        case 403:
          errorType = 'auth'
          message = 'Access denied'
          details = 'You do not have permission to perform this action'
          break
        case 404:
          errorType = 'server'
          message = 'Resource not found'
          details = 'The requested resource could not be found'
          break
        case 422:
          errorType = 'validation'
          message = 'Validation failed'
          details = data?.message || 'Please check your input data'
          break
        case 429:
          errorType = 'server'
          message = 'Too many requests'
          details = 'Please wait a moment before trying again'
          action = {
            label: 'Retry',
            handler: () => {
              // This would retry the failed request
              console.log('Retrying request...')
            }
          }
          break
        case 500:
          errorType = 'server'
          message = 'Server error'
          details = 'An internal server error occurred. Please try again later.'
          action = {
            label: 'Retry',
            handler: () => {
              window.location.reload()
            }
          }
          break
        case 503:
          errorType = 'server'
          message = 'Service unavailable'
          details = 'The service is temporarily unavailable. Please try again later.'
          break
        default:
          errorType = 'server'
          message = `Server error (${status})`
          details = data?.message || 'An unexpected server error occurred'
      }
    } else if (error.request) {
      // Network error
      errorType = 'network'
      message = 'Network connection failed'
      details = 'Please check your internet connection and try again'
      action = {
        label: 'Retry',
        handler: () => {
          window.location.reload()
        }
      }
    } else {
      // Other error
      errorType = 'unknown'
      message = error.message || 'An unexpected error occurred'
      details = context ? `Context: ${context}` : ''
    }

    return addError({
      type: errorType,
      message,
      details,
      action
    })
  }

  const handleValidationError = (field: string, message: string) => {
    return addError({
      type: 'validation',
      message: `${field}: ${message}`,
      details: 'Please correct the highlighted field and try again'
    })
  }

  const handleNetworkError = (context?: string) => {
    return addError({
      type: 'network',
      message: 'Network connection failed',
      details: context || 'Please check your internet connection and try again',
      action: {
        label: 'Retry',
        handler: () => {
          window.location.reload()
        }
      }
    })
  }

  const withErrorHandling = async <T>(
    operation: () => Promise<T>,
    loadingKey?: string,
    loadingMessage?: string,
    context?: string
  ): Promise<T | null> => {
    try {
      if (loadingKey) {
        setLoading(loadingKey, true, loadingMessage)
      }
      
      const result = await operation()
      return result
    } catch (error) {
      handleApiError(error, context)
      return null
    } finally {
      if (loadingKey) {
        setLoading(loadingKey, false)
      }
    }
  }

  return {
    // State
    errors: errors.value,
    loadingStates,
    
    // Error management
    addError,
    removeError,
    clearAllErrors,
    
    // Loading management
    setLoading,
    isLoading,
    getLoadingMessage,
    
    // Error handlers
    handleApiError,
    handleValidationError,
    handleNetworkError,
    withErrorHandling
  }
}