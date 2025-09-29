import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Diagnose from '../views/Diagnose.vue'
import History from '../views/History.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { requiresGuest: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
    meta: { requiresGuest: true }
  },
  {
    path: '/diagnose',
    name: 'Diagnose',
    component: Diagnose,
    meta: { requiresAuth: true }
  },
  {
    path: '/history',
    name: 'History',
    component: History,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Navigation guards
router.beforeEach((to, _from, next) => {
  const authStore = useAuthStore()
  
  // Wait for auth state to be initialized
  if (authStore.loading) {
    // Wait for auth state to be determined
    const unsubscribe = authStore.$subscribe(() => {
      if (!authStore.loading) {
        unsubscribe()
        // Re-run the navigation guard logic
        if (to.meta.requiresAuth && !authStore.isAuthenticated) {
          next('/login')
        } else if (to.meta.requiresGuest && authStore.isAuthenticated) {
          next('/')
        } else {
          next()
        }
      }
    })
    return
  }
  
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next('/login')
  } else if (to.meta.requiresGuest && authStore.isAuthenticated) {
    next('/')
  } else {
    next()
  }
})

export default router