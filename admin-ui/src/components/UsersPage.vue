<script setup>
import { onMounted, ref } from 'vue'
import { getSurfaceThemeClass } from '@/shared/uiClasses'
import UserRow from '@/components/users/UserRow.vue'
import UserForm from '@/components/users/UserForm.vue'

const props = defineProps({
  isDarkTheme: {
    type: Boolean,
    required: true,
  },
})

const API_BASE_URL = (import.meta.env.VITE_API_BASE_URL || '').replace(/\/$/, '')
const USERS_API_URL = `${API_BASE_URL}/api/users`
const ALLOWED_CREDENTIAL_TYPES = ['Bcrypt', 'PBKDF2', 'Argon2id', 'SCrypt']
const DEFAULT_CREDENTIAL_TYPE = 'Bcrypt'

const users = ref([])
const isLoading = ref(false)
const isSaving = ref(false)
const deletingUserId = ref('')
const isEditing = ref(false)
const isModalOpen = ref(false)
const errorMessage = ref('')
const successMessage = ref('')

const createEmptyForm = () => ({
  id: '',
  username: '',
  firstName: '',
  lastName: '',
  email: '',
  enabled: true,
  emailVerified: false,
  attributes: [],
  credentials: {
    type: '',
    value: '',
  },
})

const form = ref(createEmptyForm())

const clearMessages = () => {
  errorMessage.value = ''
  successMessage.value = ''
}

const resetForm = () => {
  form.value = createEmptyForm()
  isEditing.value = false
}

const updateForm = (nextForm) => {
  form.value = nextForm
}

const openCreateModal = () => {
  clearMessages()
  resetForm()
  form.value.credentials.type = DEFAULT_CREDENTIAL_TYPE
  isModalOpen.value = true
}

const closeModal = () => {
  isModalOpen.value = false
  resetForm()
}

const normalizeUsersResponse = (payload) => {
  if (Array.isArray(payload)) {
    return payload
  }
  if (payload && Array.isArray(payload.users)) {
    return payload.users
  }
  return []
}

const toErrorMessage = async (response) => {
  const fallback = `Request failed with status ${response.status}`
  const contentType = response.headers.get('content-type') || ''
  if (contentType.includes('application/json')) {
    const data = await response.json().catch(() => null)
    return data?.message || data?.error || fallback
  }
  const text = await response.text().catch(() => '')
  return text || fallback
}

const loadUsers = async () => {
  clearMessages()
  isLoading.value = true
  try {
    const response = await fetch(USERS_API_URL)
    if (!response.ok) {
      errorMessage.value = await toErrorMessage(response)
      return
    }
    const payload = await response.json()
    users.value = normalizeUsersResponse(payload)
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Failed to load users'
  } finally {
    isLoading.value = false
  }
}

const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

const buildPayload = () => ({
  username: form.value.username.trim(),
  firstName: form.value.firstName.trim(),
  lastName: form.value.lastName.trim(),
  email: form.value.email.trim(),
  enabled: Boolean(form.value.enabled),
  emailVerified: Boolean(form.value.emailVerified),
  attributes: form.value.attributes.map((attribute) => ({
    name: (attribute.name || '').trim(),
    value: (attribute.value || '').trim(),
  })),
  credentials:
    form.value.credentials.type.trim() || form.value.credentials.value.trim()
      ? {
          type: form.value.credentials.type.trim(),
          value: form.value.credentials.value.trim(),
        }
      : null,
})

const validateForm = () => {
  if (!form.value.username.trim()) {
    errorMessage.value = 'username is required'
    return false
  }
  if (!form.value.firstName.trim()) {
    errorMessage.value = 'firstName is required'
    return false
  }
  if (!form.value.lastName.trim()) {
    errorMessage.value = 'lastName is required'
    return false
  }
  if (!form.value.email.trim()) {
    errorMessage.value = 'email is required'
    return false
  }
  if (!emailRegex.test(form.value.email.trim())) {
    errorMessage.value = 'email must be a valid email address'
    return false
  }
  const hasInvalidAttribute = form.value.attributes.some(
    (attribute) => !(attribute.name || '').trim() || !(attribute.value || '').trim(),
  )
  if (hasInvalidAttribute) {
    errorMessage.value = 'attributes must contain non-empty name and value'
    return false
  }
  const hasType = Boolean(form.value.credentials.type.trim())
  const hasValue = Boolean(form.value.credentials.value.trim())
  if (hasType && !ALLOWED_CREDENTIAL_TYPES.includes(form.value.credentials.type.trim())) {
    errorMessage.value = 'credentials type must be one of: Bcrypt, PBKDF2, Argon2id, SCrypt'
    return false
  }
  if (!isEditing.value && (!hasType || !hasValue)) {
    errorMessage.value = 'credentials type and value are required'
    return false
  }
  if (hasType !== hasValue) {
    errorMessage.value = 'credentials type and value must both be provided'
    return false
  }
  return true
}

const saveUser = async () => {
  clearMessages()
  if (!validateForm()) {
    return
  }

  isSaving.value = true
  const payload = buildPayload()
  const isUpdate = Boolean(form.value.id)
  const targetUrl = isUpdate ? `${USERS_API_URL}/${form.value.id}` : USERS_API_URL
  const method = isUpdate ? 'PUT' : 'POST'

  try {
    const response = await fetch(targetUrl, {
      method,
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(payload),
    })

    if (!response.ok) {
      errorMessage.value = await toErrorMessage(response)
      return
    }

    const savedUser = await response.json()
    const existingIndex = users.value.findIndex((user) => user.id === savedUser.id)
    if (existingIndex >= 0) {
      users.value.splice(existingIndex, 1, savedUser)
      successMessage.value = 'User updated'
    } else {
      users.value.unshift(savedUser)
      successMessage.value = 'User created'
    }
    resetForm()
    isModalOpen.value = false
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Failed to save user'
  } finally {
    isSaving.value = false
  }
}

const editUser = (user) => {
  clearMessages()
  form.value = {
    id: user.id || '',
    username: user.username || '',
    firstName: user.firstName || '',
    lastName: user.lastName || '',
    email: user.email || '',
    enabled: Boolean(user.enabled),
    emailVerified: Boolean(user.emailVerified),
    attributes: Array.isArray(user.attributes) ? user.attributes.map((attribute) => ({ ...attribute })) : [],
    credentials: {
      type: user.credentials?.type || '',
      value: '',
    },
  }
  isEditing.value = true
  isModalOpen.value = true
}

const deleteUser = async (user) => {
  clearMessages()
  deletingUserId.value = user.id
  try {
    const response = await fetch(`${USERS_API_URL}/${user.id}`, {
      method: 'DELETE',
    })
    if (!response.ok) {
      errorMessage.value = await toErrorMessage(response)
      return
    }
    users.value = users.value.filter((item) => item.id !== user.id)
    if (form.value.id === user.id) {
      resetForm()
    }
    successMessage.value = 'User deleted'
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Failed to delete user'
  } finally {
    deletingUserId.value = ''
  }
}

onMounted(loadUsers)
</script>

<template>
  <section
    class="mx-auto w-full max-w-5xl rounded-xl border p-6"
    :class="getSurfaceThemeClass(props.isDarkTheme)"
  >
    <div class="flex flex-wrap items-center justify-between gap-3">
      <div>
        <h1 class="text-xl font-semibold">Users</h1>
        <p :class="props.isDarkTheme ? 'mt-1 text-sm text-slate-400' : 'mt-1 text-sm text-slate-500'">
          Manage users.
        </p>
      </div>

      <div class="flex flex-wrap gap-2">
        <button
          type="button"
          class="rounded-lg border px-3 py-2 text-sm font-medium transition"
          :class="
            props.isDarkTheme
              ? 'border-slate-700 bg-slate-800 hover:bg-slate-700'
              : 'border-slate-300 bg-white hover:bg-slate-50'
          "
          :disabled="isLoading"
          @click="loadUsers"
        >
          {{ isLoading ? 'Loading...' : 'Refresh' }}
        </button>
        <button
          type="button"
          class="rounded-lg bg-blue-600 px-3 py-2 text-sm font-medium text-white transition hover:bg-blue-500"
          @click="openCreateModal"
        >
          New user
        </button>
      </div>
    </div>

    <p
      v-if="errorMessage"
      class="mt-4 rounded-lg border border-red-500/50 bg-red-500/10 px-3 py-2 text-sm text-red-300"
    >
      {{ errorMessage }}
    </p>
    <p
      v-if="successMessage"
      class="mt-4 rounded-lg border border-emerald-500/40 bg-emerald-500/10 px-3 py-2 text-sm text-emerald-300"
    >
      {{ successMessage }}
    </p>

    <div class="mt-6">
      <h2 class="text-sm font-semibold uppercase tracking-wide">Users</h2>

      <p v-if="!isLoading && users.length === 0" class="mt-3 text-sm opacity-70">
        No users found.
      </p>

      <div v-else class="mt-3 space-y-3" role="list">
        <UserRow
          v-for="user in users"
          :key="user.id"
          :user="user"
          :is-dark-theme="props.isDarkTheme"
          :is-deleting="deletingUserId === user.id"
          @edit="editUser"
          @delete="deleteUser"
        />
      </div>
    </div>

    <div
      v-if="isModalOpen"
      class="fixed inset-0 z-40 flex items-center justify-center bg-black/60 px-4 py-8"
      @click.self="closeModal"
    >
      <div
        class="w-full max-w-xl rounded-xl border p-5"
        :class="props.isDarkTheme ? 'border-slate-700 bg-slate-900' : 'border-slate-200 bg-white'"
      >
        <UserForm
          :model-value="form"
          :is-dark-theme="props.isDarkTheme"
          :is-editing="isEditing"
          :is-saving="isSaving"
          @update:model-value="updateForm"
          @submit="saveUser"
          @close="closeModal"
        />
      </div>
    </div>
  </section>
</template>
