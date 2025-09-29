<template>
  <div class="bmi-widget" v-if="height > 0 && weight > 0">
    <div class="bmi-display">
      <h4>BMI Calculator</h4>
      <div class="bmi-value">
        <span class="bmi-number">{{ bmi.toFixed(1) }}</span>
        <span class="bmi-unit">kg/m²</span>
      </div>
      <div class="bmi-category" :class="bmiCategoryClass">
        {{ bmiCategory.category }}
      </div>
      <div class="bmi-range">{{ bmiCategory.range }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, watch } from 'vue'
import type { BMICategory } from '../types'

const props = defineProps<{
  height: number
  weight: number
}>()

const emit = defineEmits<{
  'bmi-calculated': [bmi: number]
}>()

const bmi = computed(() => {
  if (props.height > 0 && props.weight > 0) {
    return props.weight / (props.height * props.height)
  }
  return 0
})

const bmiCategory = computed((): BMICategory => {
  const bmiValue = bmi.value
  
  if (bmiValue < 18.5) {
    return {
      category: 'Underweight',
      range: '< 18.5',
      color: 'blue'
    }
  } else if (bmiValue < 25) {
    return {
      category: 'Normal',
      range: '18.5 - 24.9',
      color: 'green'
    }
  } else if (bmiValue < 30) {
    return {
      category: 'Overweight',
      range: '25.0 - 29.9',
      color: 'yellow'
    }
  } else {
    return {
      category: 'Obese',
      range: '≥ 30.0',
      color: 'red'
    }
  }
})

const bmiCategoryClass = computed(() => {
  return `bmi-${bmiCategory.value.color}`
})

// Emit BMI value when it changes
watch(bmi, (newBmi) => {
  if (newBmi > 0) {
    emit('bmi-calculated', newBmi)
  }
})
</script>

<style scoped>
.bmi-widget {
  background: #f0f9ff;
  border: 1px solid #0ea5e9;
  border-radius: 8px;
  padding: 1rem;
  margin-top: 1rem;
}

.bmi-display h4 {
  margin: 0 0 0.5rem 0;
  color: #0c4a6e;
  font-size: 1rem;
}

.bmi-value {
  display: flex;
  align-items: baseline;
  gap: 0.25rem;
  margin-bottom: 0.5rem;
}

.bmi-number {
  font-size: 1.5rem;
  font-weight: bold;
  color: #0c4a6e;
}

.bmi-unit {
  font-size: 0.875rem;
  color: #64748b;
}

.bmi-category {
  font-weight: 600;
  font-size: 1rem;
  margin-bottom: 0.25rem;
}

.bmi-blue {
  color: #2563eb;
}

.bmi-green {
  color: #16a34a;
}

.bmi-yellow {
  color: #ca8a04;
}

.bmi-red {
  color: #dc2626;
}

.bmi-range {
  font-size: 0.875rem;
  color: #64748b;
}
</style>

