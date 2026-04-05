<script setup>
const props = defineProps({
  modelValue: {
    type: Object,
    required: true,
  },
  isDarkTheme: {
    type: Boolean,
    required: true,
  },
  isEditing: {
    type: Boolean,
    required: true,
  },
  isSaving: {
    type: Boolean,
    required: true,
  },
})

const emit = defineEmits(['update:modelValue', 'submit', 'close'])

const updateField = (field, value) => {
  emit('update:modelValue', {
    ...props.modelValue,
    [field]: value,
  })
}

const generateUuid = () => {
  if (typeof crypto !== 'undefined' && typeof crypto.randomUUID === 'function') {
    return crypto.randomUUID()
  }
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (char) => {
    const random = Math.floor(Math.random() * 16)
    const value = char === 'x' ? random : (random & 0x3) | 0x8
    return value.toString(16)
  })
}

const generateClientSecret = () => {
  const byteLength = 32
  if (typeof crypto !== 'undefined' && typeof crypto.getRandomValues === 'function') {
    const bytes = crypto.getRandomValues(new Uint8Array(byteLength))
    return Array.from(bytes, (byte) => byte.toString(16).padStart(2, '0')).join('')
  }
  return Array.from({ length: byteLength * 2 }, () => Math.floor(Math.random() * 16).toString(16)).join('')
}
</script>

<template>
  <div>
    <div class="flex items-center justify-between gap-3">
      <h2 class="text-sm font-semibold uppercase tracking-wide">
        {{ props.isEditing ? 'Edit client' : 'New client' }}
      </h2>
      <button
        type="button"
        class="rounded-md border px-2 py-1 text-xs font-medium transition"
        :class="
          props.isDarkTheme
            ? 'border-slate-700 bg-slate-800 hover:bg-slate-700'
            : 'border-slate-300 bg-white hover:bg-slate-50'
        "
        @click="emit('close')"
      >
        Close
      </button>
    </div>

    <form class="mt-3 space-y-4" @submit.prevent="emit('submit')">
      <label class="block">
        <div class="mb-1 flex items-center justify-between gap-2">
          <span class="block text-sm font-medium">Client ID</span>
          <button
            type="button"
            class="rounded-md border px-2 py-1 text-xs font-medium transition"
            :class="
              props.isDarkTheme
                ? 'border-slate-700 bg-slate-800 hover:bg-slate-700'
                : 'border-slate-300 bg-white hover:bg-slate-50'
            "
            @click="updateField('clientId', generateUuid())"
          >
            Generate
          </button>
        </div>
        <input
          :value="props.modelValue.clientId"
          type="text"
          required
          class="w-full rounded-lg border px-3 py-2 text-sm outline-none transition focus:ring-2"
          :class="
            props.isDarkTheme
              ? 'border-slate-700 bg-slate-800 text-slate-100 focus:ring-blue-500/40'
              : 'border-slate-300 bg-white text-slate-900 focus:ring-blue-400/40'
          "
          @input="updateField('clientId', $event.target.value)"
        >
      </label>

      <label class="block">
        <span class="mb-1 block text-sm font-medium">Client name</span>
        <input
          :value="props.modelValue.clientName"
          type="text"
          required
          class="w-full rounded-lg border px-3 py-2 text-sm outline-none transition focus:ring-2"
          :class="
            props.isDarkTheme
              ? 'border-slate-700 bg-slate-800 text-slate-100 focus:ring-blue-500/40'
              : 'border-slate-300 bg-white text-slate-900 focus:ring-blue-400/40'
          "
          @input="updateField('clientName', $event.target.value)"
        >
      </label>

      <label class="block">
        <div class="mb-1 flex items-center justify-between gap-2">
          <span class="block text-sm font-medium">Client secret</span>
          <button
            type="button"
            class="rounded-md border px-2 py-1 text-xs font-medium transition"
            :class="
              props.isDarkTheme
                ? 'border-slate-700 bg-slate-800 hover:bg-slate-700'
                : 'border-slate-300 bg-white hover:bg-slate-50'
            "
            @click="updateField('clientSecret', generateClientSecret())"
          >
            Generate secret
          </button>
        </div>
        <input
          :value="props.modelValue.clientSecret"
          type="text"
          :required="!props.isEditing"
          class="w-full rounded-lg border px-3 py-2 text-sm outline-none transition focus:ring-2"
          :class="
            props.isDarkTheme
              ? 'border-slate-700 bg-slate-800 text-slate-100 focus:ring-blue-500/40'
              : 'border-slate-300 bg-white text-slate-900 focus:ring-blue-400/40'
          "
          @input="updateField('clientSecret', $event.target.value)"
        >
      </label>

      <label class="block">
        <span class="mb-1 block text-sm font-medium">Scopes (comma separated)</span>
        <input
          :value="props.modelValue.scopes"
          type="text"
          placeholder="read, write"
          class="w-full rounded-lg border px-3 py-2 text-sm outline-none transition focus:ring-2"
          :class="
            props.isDarkTheme
              ? 'border-slate-700 bg-slate-800 text-slate-100 focus:ring-blue-500/40'
              : 'border-slate-300 bg-white text-slate-900 focus:ring-blue-400/40'
          "
          @input="updateField('scopes', $event.target.value)"
        >
      </label>

      <label class="block">
        <span class="mb-1 block text-sm font-medium">Redirect URIs (comma separated)</span>
        <input
          :value="props.modelValue.redirectUris"
          type="text"
          placeholder="https://app.example.com/callback"
          class="w-full rounded-lg border px-3 py-2 text-sm outline-none transition focus:ring-2"
          :class="
            props.isDarkTheme
              ? 'border-slate-700 bg-slate-800 text-slate-100 focus:ring-blue-500/40'
              : 'border-slate-300 bg-white text-slate-900 focus:ring-blue-400/40'
          "
          @input="updateField('redirectUris', $event.target.value)"
        >
      </label>

      <div class="flex gap-2">
        <button
          type="submit"
          class="rounded-lg bg-blue-600 px-4 py-2 text-sm font-medium text-white transition hover:bg-blue-500 disabled:cursor-not-allowed disabled:opacity-70"
          :disabled="props.isSaving"
        >
          {{ props.isSaving ? 'Saving...' : props.isEditing ? 'Update client' : 'Create client' }}
        </button>
        <button
          type="button"
          class="rounded-lg border px-4 py-2 text-sm font-medium transition"
          :class="
            props.isDarkTheme
              ? 'border-slate-700 bg-slate-800 hover:bg-slate-700'
              : 'border-slate-300 bg-white hover:bg-slate-50'
          "
          @click="emit('close')"
        >
          Cancel
        </button>
      </div>
    </form>
  </div>
</template>

