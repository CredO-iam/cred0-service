<script setup>
import { onMounted, ref } from 'vue'
import { getSurfaceThemeClass } from '@/shared/uiClasses'
import RoleRow from '@/components/roles/RoleRow.vue'
import RoleForm from '@/components/roles/RoleForm.vue'

const props = defineProps({
  isDarkTheme: {
    type: Boolean,
    required: true,
  },
})

const API_BASE_URL = (import.meta.env.VITE_API_BASE_URL || '').replace(/\/$/, '')
const ROLES_API_URL = `${API_BASE_URL}/api/roles`

const roles = ref([])
const isLoading = ref(false)
const isSaving = ref(false)
const deletingRoleId = ref('')
const isEditing = ref(false)
const isModalOpen = ref(false)
const errorMessage = ref('')
const successMessage = ref('')

const createEmptyForm = () => ({
  id: '',
  name: '',
  description: '',
  userIds: '',
  groupIds: '',
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
  isModalOpen.value = true
}

const closeModal = () => {
  isModalOpen.value = false
  resetForm()
}

const normalizeRolesResponse = (payload) => {
  if (Array.isArray(payload)) {
    return payload
  }
  if (payload && Array.isArray(payload.roles)) {
    return payload.roles
  }
  return []
}

const parseCsv = (value) =>
  value
    .split(',')
    .map((item) => item.trim())
    .filter(Boolean)

const toCsv = (values) => (values || []).join(', ')

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

const loadRoles = async () => {
  clearMessages()
  isLoading.value = true
  try {
    const response = await fetch(ROLES_API_URL)
    if (!response.ok) {
      errorMessage.value = await toErrorMessage(response)
      return
    }
    const payload = await response.json()
    roles.value = normalizeRolesResponse(payload)
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Failed to load roles'
  } finally {
    isLoading.value = false
  }
}

const buildPayload = () => ({
  name: form.value.name.trim(),
  description: form.value.description.trim() || null,
  userIds: parseCsv(form.value.userIds),
  groupIds: parseCsv(form.value.groupIds),
})

const saveRole = async () => {
  clearMessages()
  isSaving.value = true

  const isUpdate = Boolean(form.value.id)
  const targetUrl = isUpdate ? `${ROLES_API_URL}/${form.value.id}` : ROLES_API_URL
  const method = isUpdate ? 'PUT' : 'POST'

  try {
    const response = await fetch(targetUrl, {
      method,
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(buildPayload()),
    })

    if (!response.ok) {
      errorMessage.value = await toErrorMessage(response)
      return
    }

    const savedRole = await response.json()
    const existingIndex = roles.value.findIndex((role) => role.id === savedRole.id)
    if (existingIndex >= 0) {
      roles.value.splice(existingIndex, 1, savedRole)
      successMessage.value = 'Role updated'
    } else {
      roles.value.unshift(savedRole)
      successMessage.value = 'Role created'
    }
    resetForm()
    isModalOpen.value = false
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Failed to save role'
  } finally {
    isSaving.value = false
  }
}

const editRole = (role) => {
  clearMessages()
  form.value = {
    id: role.id || '',
    name: role.name || '',
    description: role.description || '',
    userIds: toCsv(role.userIds),
    groupIds: toCsv(role.groupIds),
  }
  isEditing.value = true
  isModalOpen.value = true
}

const deleteRole = async (role) => {
  clearMessages()
  deletingRoleId.value = role.id
  try {
    const response = await fetch(`${ROLES_API_URL}/${role.id}`, {
      method: 'DELETE',
    })
    if (!response.ok) {
      errorMessage.value = await toErrorMessage(response)
      return
    }
    roles.value = roles.value.filter((item) => item.id !== role.id)
    if (form.value.id === role.id) {
      resetForm()
    }
    successMessage.value = 'Role deleted'
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Failed to delete role'
  } finally {
    deletingRoleId.value = ''
  }
}

onMounted(loadRoles)
</script>

<template>
  <section
    class="mx-auto w-full max-w-5xl rounded-xl border p-6"
    :class="getSurfaceThemeClass(props.isDarkTheme)"
  >
    <div class="flex flex-wrap items-center justify-between gap-3">
      <div>
        <h1 class="text-xl font-semibold">Roles</h1>
        <p :class="props.isDarkTheme ? 'mt-1 text-sm text-slate-400' : 'mt-1 text-sm text-slate-500'">
          Manage roles and user/group assignments.
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
          @click="loadRoles"
        >
          {{ isLoading ? 'Loading...' : 'Refresh' }}
        </button>
        <button
          type="button"
          class="rounded-lg bg-blue-600 px-3 py-2 text-sm font-medium text-white transition hover:bg-blue-500"
          @click="openCreateModal"
        >
          New role
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
      <h2 class="text-sm font-semibold uppercase tracking-wide">Roles</h2>

      <p v-if="!isLoading && roles.length === 0" class="mt-3 text-sm opacity-70">No roles found.</p>

      <div v-else class="mt-3 space-y-3" role="list">
        <RoleRow
          v-for="role in roles"
          :key="role.id"
          :role="role"
          :is-dark-theme="props.isDarkTheme"
          :is-deleting="deletingRoleId === role.id"
          @edit="editRole"
          @delete="deleteRole"
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
        <RoleForm
          :model-value="form"
          :is-dark-theme="props.isDarkTheme"
          :is-editing="isEditing"
          :is-saving="isSaving"
          @update:model-value="updateForm"
          @submit="saveRole"
          @close="closeModal"
        />
      </div>
    </div>
  </section>
</template>
