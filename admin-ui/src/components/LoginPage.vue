<script setup>
import { computed, ref } from 'vue'
import { loginProcessingUrl, submitLoginForm } from '@/shared/adminApi'

const props = defineProps({
  isDarkTheme: {
    type: Boolean,
    required: true,
  },
})

const loginAction = computed(() => loginProcessingUrl())
const authError = ref('')
const isSubmitting = ref(false)

const submitCredentials = async (event) => {
  event.preventDefault()

  const form = event.currentTarget
  const formData = new FormData(form)
  const username = String(formData.get('username') || '')
  const password = String(formData.get('password') || '')

  authError.value = ''
  isSubmitting.value = true

  try {
    const result = await submitLoginForm({ username, password })
    if (result.ok) {
      globalThis.location.assign('/')
      return
    }

    authError.value = 'Invalid username or password.'
  } catch {
    authError.value = 'Unable to sign in. Please try again.'
  } finally {
    isSubmitting.value = false
  }
}

const cardClasses = computed(() =>
  props.isDarkTheme
    ? 'border-slate-700 bg-slate-900 text-slate-100'
    : 'border-slate-200 bg-white text-slate-900',
)

const mutedTextClasses = computed(() =>
  props.isDarkTheme ? 'text-slate-400' : 'text-slate-500',
)

const inputClasses = computed(() =>
  props.isDarkTheme
    ? 'border-slate-700 bg-slate-800 text-slate-100 placeholder:text-slate-500 focus:border-blue-500'
    : 'border-slate-300 bg-white text-slate-900 placeholder:text-slate-400 focus:border-blue-500',
)
</script>

<template>
  <section class="mx-auto flex min-h-[calc(100vh-6rem)] w-full max-w-5xl items-center justify-center px-4">
    <div class="w-full max-w-md rounded-xl border p-6 shadow-sm" :class="cardClasses">
      <div>
        <h1 class="text-2xl font-semibold">Admin login</h1>
        <p class="mt-1 text-sm" :class="mutedTextClasses">
          Sign in to continue to the admin panel.
        </p>
      </div>

      <form class="mt-6 space-y-4" method="post" :action="loginAction" @submit="submitCredentials">
        <label class="block">
          <span class="mb-1 block text-sm font-medium">Username</span>
          <input
            name="username"
            type="text"
            autocomplete="username"
            required
            class="w-full rounded-lg border px-3 py-2 text-sm outline-none transition"
            :class="inputClasses"
          />
        </label>

        <label class="block">
          <span class="mb-1 block text-sm font-medium">Password</span>
          <input
            name="password"
            type="password"
            autocomplete="current-password"
            required
            class="w-full rounded-lg border px-3 py-2 text-sm outline-none transition"
            :class="inputClasses"
          />
        </label>

        <p v-if="authError" class="text-sm text-red-500">{{ authError }}</p>

        <button
          type="submit"
          :disabled="isSubmitting"
          class="w-full rounded-lg bg-blue-600 px-3 py-2 text-sm font-medium text-white transition hover:bg-blue-500"
        >
          {{ isSubmitting ? 'Signing in...' : 'Sign in' }}
        </button>
      </form>
    </div>
  </section>
</template>


