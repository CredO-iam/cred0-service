<script setup>
import { onMounted, ref } from 'vue'
import { getSurfaceThemeClass } from '@/shared/uiClasses'
import GroupRow from '@/components/groups/GroupRow.vue'
import GroupForm from '@/components/groups/GroupForm.vue'

const props = defineProps({
  isDarkTheme: {
    type: Boolean,
    required: true,
  },
})

const API_BASE_URL = (import.meta.env.VITE_API_BASE_URL || '').replace(/\/$/, '')
const GROUPS_API_URL = `${API_BASE_URL}/api/groups`

const groups = ref([])
const isLoading = ref(false)
const isSaving = ref(false)
const deletingGroupId = ref('')
const isEditing = ref(false)
const isModalOpen = ref(false)
const errorMessage = ref('')
const successMessage = ref('')

const createEmptyForm = () => ({
  id: '',
  name: '',
  description: '',
  userIds: '',
  roleIds: '',
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

const normalizeGroupsResponse = (payload) => {
  if (Array.isArray(payload)) {
    return payload
  }
  if (payload && Array.isArray(payload.groups)) {
    return payload.groups
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

const loadGroups = async () => {
  clearMessages()
  isLoading.value = true
  try {
    const response = await fetch(GROUPS_API_URL)
    if (!response.ok) {
      errorMessage.value = await toErrorMessage(response)
      return
    }
    const payload = await response.json()
    groups.value = normalizeGroupsResponse(payload)
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Failed to load groups'
  } finally {
    isLoading.value = false
  }
}

const buildPayload = () => ({
  name: form.value.name.trim(),
  description: form.value.description.trim() || null,
  userIds: isEditing.value ? parseCsv(form.value.userIds) : [],
  roleIds: isEditing.value ? parseCsv(form.value.roleIds) : [],
})

const saveGroup = async () => {
  clearMessages()
  isSaving.value = true

  const isUpdate = Boolean(form.value.id)
  const targetUrl = isUpdate ? `${GROUPS_API_URL}/${form.value.id}` : GROUPS_API_URL
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

    const savedGroup = await response.json()
    const existingIndex = groups.value.findIndex((group) => group.id === savedGroup.id)
    if (existingIndex >= 0) {
      groups.value.splice(existingIndex, 1, savedGroup)
      successMessage.value = 'Group updated'
    } else {
      groups.value.unshift(savedGroup)
      successMessage.value = 'Group created'
    }
    resetForm()
    isModalOpen.value = false
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Failed to save group'
  } finally {
    isSaving.value = false
  }
}

const editGroup = (group) => {
  clearMessages()
  form.value = {
    id: group.id || '',
    name: group.name || '',
    description: group.description || '',
    userIds: toCsv(group.userIds),
    roleIds: toCsv(group.roleIds),
  }
  isEditing.value = true
  isModalOpen.value = true
}

const deleteGroup = async (group) => {
  clearMessages()
  deletingGroupId.value = group.id
  try {
    const response = await fetch(`${GROUPS_API_URL}/${group.id}`, {
      method: 'DELETE',
    })
    if (!response.ok) {
      errorMessage.value = await toErrorMessage(response)
      return
    }
    groups.value = groups.value.filter((item) => item.id !== group.id)
    if (form.value.id === group.id) {
      resetForm()
    }
    successMessage.value = 'Group deleted'
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Failed to delete group'
  } finally {
    deletingGroupId.value = ''
  }
}

onMounted(loadGroups)
</script>

<template>
  <section
    class="mx-auto w-full max-w-5xl rounded-xl border p-6"
    :class="getSurfaceThemeClass(props.isDarkTheme)"
  >
    <div class="flex flex-wrap items-center justify-between gap-3">
      <div>
        <h1 class="text-xl font-semibold">Groups</h1>
        <p :class="props.isDarkTheme ? 'mt-1 text-sm text-slate-400' : 'mt-1 text-sm text-slate-500'">
          Manage groups and user/role assignments.
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
          @click="loadGroups"
        >
          {{ isLoading ? 'Loading...' : 'Refresh' }}
        </button>
        <button
          type="button"
          class="rounded-lg bg-blue-600 px-3 py-2 text-sm font-medium text-white transition hover:bg-blue-500"
          @click="openCreateModal"
        >
          New group
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
      <h2 class="text-sm font-semibold uppercase tracking-wide">Groups</h2>

      <p v-if="!isLoading && groups.length === 0" class="mt-3 text-sm opacity-70">No groups found.</p>

      <div v-else class="mt-3 space-y-3" role="list">
        <GroupRow
          v-for="group in groups"
          :key="group.id"
          :group="group"
          :is-dark-theme="props.isDarkTheme"
          :is-deleting="deletingGroupId === group.id"
          @edit="editGroup"
          @delete="deleteGroup"
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
        <GroupForm
          :model-value="form"
          :is-dark-theme="props.isDarkTheme"
          :is-editing="isEditing"
          :is-saving="isSaving"
          @update:model-value="updateForm"
          @submit="saveGroup"
          @close="closeModal"
        />
      </div>
    </div>
  </section>
</template>
