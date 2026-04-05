<script setup>
import { onMounted, ref } from 'vue'
import { getSurfaceThemeClass } from '@/shared/uiClasses'
import ClientRow from '@/components/clients/ClientRow.vue'
import ClientForm from '@/components/clients/ClientForm.vue'

const props = defineProps({
  isDarkTheme: {
    type: Boolean,
    required: true,
  },
})

const API_BASE_URL = (import.meta.env.VITE_API_BASE_URL || '').replace(/\/$/, '')
const CLIENTS_API_URL = `${API_BASE_URL}/api/registered-clients`

const clients = ref([])
const isLoading = ref(false)
const isSaving = ref(false)
const deletingClientId = ref('')
const isEditing = ref(false)
const isModalOpen = ref(false)
const errorMessage = ref('')
const successMessage = ref('')

const createEmptyForm = () => ({
  id: '',
  clientId: '',
  clientName: '',
  clientSecret: '',
  scopes: '',
  redirectUris: '',
})

const form = ref(createEmptyForm())

const clearMessages = () => {
  errorMessage.value = ''
  successMessage.value = ''
}

const toCsv = (values) => (values || []).join(', ')

const parseCsv = (value) =>
  value
    .split(',')
    .map((item) => item.trim())
    .filter(Boolean)

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

const normalizeClientsResponse = (payload) => {
  if (Array.isArray(payload)) {
    return payload
  }
  if (payload && Array.isArray(payload.clients)) {
    return payload.clients
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

const loadClients = async () => {
  clearMessages()
  isLoading.value = true
  try {
    const response = await fetch(CLIENTS_API_URL)
    if (!response.ok) {
      errorMessage.value = await toErrorMessage(response)
      return
    }
    const payload = await response.json()
    clients.value = normalizeClientsResponse(payload)
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Failed to load clients'
  } finally {
    isLoading.value = false
  }
}

const buildPayload = () => ({
  id: form.value.id || null,
  clientId: form.value.clientId.trim(),
  clientName: form.value.clientName.trim(),
  clientSecret: form.value.clientSecret.trim() || null,
  clientAuthenticationMethods: ['client_secret_basic'],
  authorizationGrantTypes: ['client_credentials'],
  redirectUris: parseCsv(form.value.redirectUris),
  scopes: parseCsv(form.value.scopes),
  clientSettings: {},
  tokenSettings: {},
})

const saveClient = async () => {
  clearMessages()
  isSaving.value = true
  try {
    const response = await fetch(CLIENTS_API_URL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(buildPayload()),
    })
    if (!response.ok) {
      errorMessage.value = await toErrorMessage(response)
      return
    }

    const savedClient = await response.json()
    const existingIndex = clients.value.findIndex((client) => client.id === savedClient.id)
    if (existingIndex >= 0) {
      clients.value.splice(existingIndex, 1, savedClient)
      successMessage.value = 'Client updated'
    } else {
      clients.value.unshift(savedClient)
      successMessage.value = 'Client created'
    }
    resetForm()
    isModalOpen.value = false
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Failed to save client'
  } finally {
    isSaving.value = false
  }
}

const editClient = (client) => {
  clearMessages()
  form.value = {
    id: client.id || '',
    clientId: client.clientId || '',
    clientName: client.clientName || '',
    clientSecret: client.clientSecret || '',
    scopes: toCsv(client.scopes),
    redirectUris: toCsv(client.redirectUris),
  }
  isEditing.value = true
  isModalOpen.value = true
}

const deleteClient = async (client) => {
  clearMessages()
  deletingClientId.value = client.id
  try {
    const response = await fetch(`${CLIENTS_API_URL}/${client.id}`, {
      method: 'DELETE',
    })
    if (!response.ok) {
      errorMessage.value = await toErrorMessage(response)
      return
    }
    clients.value = clients.value.filter((item) => item.id !== client.id)
    if (form.value.id === client.id) {
      resetForm()
    }
    successMessage.value = 'Client deleted'
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Failed to delete client'
  } finally {
    deletingClientId.value = ''
  }
}

onMounted(loadClients)
</script>

<template>
  <section
    class="mx-auto w-full max-w-5xl rounded-xl border p-6"
    :class="getSurfaceThemeClass(props.isDarkTheme)"
  >
    <div class="flex flex-wrap items-center justify-between gap-3">
      <div>
        <h1 class="text-xl font-semibold">Clients</h1>
        <p :class="props.isDarkTheme ? 'mt-1 text-sm text-slate-400' : 'mt-1 text-sm text-slate-500'">
          Manage registered OAuth2 clients.
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
          @click="loadClients"
        >
          {{ isLoading ? 'Loading...' : 'Refresh' }}
        </button>
        <button
          type="button"
          class="rounded-lg bg-blue-600 px-3 py-2 text-sm font-medium text-white transition hover:bg-blue-500"
          @click="openCreateModal"
        >
          New client
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
      <h2 class="text-sm font-semibold uppercase tracking-wide">Registered clients</h2>

      <p v-if="!isLoading && clients.length === 0" class="mt-3 text-sm opacity-70">
        No clients found.
      </p>

      <div v-else class="mt-3 space-y-3" role="list">
        <ClientRow
          v-for="client in clients"
          :key="client.id"
          :client="client"
          :is-dark-theme="props.isDarkTheme"
          :is-deleting="deletingClientId === client.id"
          @edit="editClient"
          @delete="deleteClient"
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
        <ClientForm
          :model-value="form"
          :is-dark-theme="props.isDarkTheme"
          :is-editing="isEditing"
          :is-saving="isSaving"
          @update:model-value="updateForm"
          @submit="saveClient"
          @close="closeModal"
        />
      </div>
    </div>
  </section>
</template>
