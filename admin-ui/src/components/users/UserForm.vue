<script setup>
import { USER_CREDENTIAL_TYPES } from '@/shared/userCredentialTypes'

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

const updateAttribute = (index, field, value) => {
  const next = [...props.modelValue.attributes]
  next[index] = {
    ...next[index],
    [field]: value,
  }
  updateField('attributes', next)
}

const addAttribute = () => {
  updateField('attributes', [...props.modelValue.attributes, { name: '', value: '' }])
}

const removeAttribute = (index) => {
  const next = props.modelValue.attributes.filter((_, currentIndex) => currentIndex !== index)
  updateField('attributes', next)
}
</script>

<template>
  <div>
    <div class="flex items-center justify-between gap-3">
      <h2 class="text-sm font-semibold uppercase tracking-wide">
        {{ props.isEditing ? 'Edit user' : 'New user' }}
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
        <span class="mb-1 block text-sm font-medium">Username</span>
        <input
          :value="props.modelValue.username"
          type="text"
          required
          class="w-full rounded-lg border px-3 py-2 text-sm outline-none transition focus:ring-2"
          :class="
            props.isDarkTheme
              ? 'border-slate-700 bg-slate-800 text-slate-100 focus:ring-blue-500/40'
              : 'border-slate-300 bg-white text-slate-900 focus:ring-blue-400/40'
          "
          @input="updateField('username', $event.target.value)"
        >
      </label>

      <div class="grid gap-4 sm:grid-cols-2">
        <label class="block">
          <span class="mb-1 block text-sm font-medium">First name</span>
          <input
            :value="props.modelValue.firstName"
            type="text"
            required
            class="w-full rounded-lg border px-3 py-2 text-sm outline-none transition focus:ring-2"
            :class="
              props.isDarkTheme
                ? 'border-slate-700 bg-slate-800 text-slate-100 focus:ring-blue-500/40'
                : 'border-slate-300 bg-white text-slate-900 focus:ring-blue-400/40'
            "
            @input="updateField('firstName', $event.target.value)"
          >
        </label>

        <label class="block">
          <span class="mb-1 block text-sm font-medium">Last name</span>
          <input
            :value="props.modelValue.lastName"
            type="text"
            required
            class="w-full rounded-lg border px-3 py-2 text-sm outline-none transition focus:ring-2"
            :class="
              props.isDarkTheme
                ? 'border-slate-700 bg-slate-800 text-slate-100 focus:ring-blue-500/40'
                : 'border-slate-300 bg-white text-slate-900 focus:ring-blue-400/40'
            "
            @input="updateField('lastName', $event.target.value)"
          >
        </label>
      </div>

      <label class="block">
        <span class="mb-1 block text-sm font-medium">Email</span>
        <input
          :value="props.modelValue.email"
          type="email"
          required
          class="w-full rounded-lg border px-3 py-2 text-sm outline-none transition focus:ring-2"
          :class="
            props.isDarkTheme
              ? 'border-slate-700 bg-slate-800 text-slate-100 focus:ring-blue-500/40'
              : 'border-slate-300 bg-white text-slate-900 focus:ring-blue-400/40'
          "
          @input="updateField('email', $event.target.value)"
        >
      </label>

      <div class="grid gap-3 sm:grid-cols-2">
        <label class="inline-flex items-center gap-2 text-sm">
          <input
            :checked="props.modelValue.enabled"
            type="checkbox"
            @change="updateField('enabled', $event.target.checked)"
          >
          Enabled
        </label>
        <label class="inline-flex items-center gap-2 text-sm">
          <input
            :checked="props.modelValue.emailVerified"
            type="checkbox"
            @change="updateField('emailVerified', $event.target.checked)"
          >
          Email verified
        </label>
      </div>

      <div>
        <div class="mb-2 flex items-center justify-between gap-2">
          <span class="text-sm font-medium">Attributes</span>
          <button
            type="button"
            class="rounded-md border px-2 py-1 text-xs font-medium transition"
            :class="
              props.isDarkTheme
                ? 'border-slate-700 bg-slate-800 hover:bg-slate-700'
                : 'border-slate-300 bg-white hover:bg-slate-50'
            "
            @click="addAttribute"
          >
            Add attribute
          </button>
        </div>
        <div v-if="props.modelValue.attributes.length === 0" class="text-xs opacity-70">No attributes.</div>
        <div v-else class="space-y-2">
          <div
            v-for="(attribute, index) in props.modelValue.attributes"
            :key="`attribute-${index}`"
            class="grid gap-2 sm:grid-cols-[1fr_1fr_auto]"
          >
            <input
              :value="attribute.name"
              type="text"
              required
              placeholder="name"
              class="rounded-lg border px-3 py-2 text-sm outline-none transition focus:ring-2"
              :class="
                props.isDarkTheme
                  ? 'border-slate-700 bg-slate-800 text-slate-100 focus:ring-blue-500/40'
                  : 'border-slate-300 bg-white text-slate-900 focus:ring-blue-400/40'
              "
              @input="updateAttribute(index, 'name', $event.target.value)"
            >
            <input
              :value="attribute.value"
              type="text"
              required
              placeholder="value"
              class="rounded-lg border px-3 py-2 text-sm outline-none transition focus:ring-2"
              :class="
                props.isDarkTheme
                  ? 'border-slate-700 bg-slate-800 text-slate-100 focus:ring-blue-500/40'
                  : 'border-slate-300 bg-white text-slate-900 focus:ring-blue-400/40'
              "
              @input="updateAttribute(index, 'value', $event.target.value)"
            >
            <button
              type="button"
              class="rounded-md border border-red-500/50 bg-red-500/10 px-2 py-1 text-xs font-medium text-red-300"
              @click="removeAttribute(index)"
            >
              Remove
            </button>
          </div>
        </div>
      </div>

      <label class="block">
        <span class="mb-1 block text-sm font-medium">Credential type</span>
        <select
          :value="props.modelValue.credentials.type"
          :required="!props.isEditing"
          class="w-full rounded-lg border px-3 py-2 text-sm outline-none transition focus:ring-2"
          :class="
            props.isDarkTheme
              ? 'border-slate-700 bg-slate-800 text-slate-100 focus:ring-blue-500/40'
              : 'border-slate-300 bg-white text-slate-900 focus:ring-blue-400/40'
          "
          @change="updateField('credentials', { ...props.modelValue.credentials, type: $event.target.value })"
        >
          <option v-if="props.isEditing" value="">No change</option>
          <option v-for="type in USER_CREDENTIAL_TYPES" :key="type" :value="type">{{ type }}</option>
        </select>
      </label>

      <label class="block">
        <span class="mb-1 block text-sm font-medium">Credential value</span>
        <input
          :value="props.modelValue.credentials.value"
          type="password"
          :required="!props.isEditing"
          class="w-full rounded-lg border px-3 py-2 text-sm outline-none transition focus:ring-2"
          :class="
            props.isDarkTheme
              ? 'border-slate-700 bg-slate-800 text-slate-100 focus:ring-blue-500/40'
              : 'border-slate-300 bg-white text-slate-900 focus:ring-blue-400/40'
          "
          @input="updateField('credentials', { ...props.modelValue.credentials, value: $event.target.value })"
        >
      </label>

      <div class="flex gap-2">
        <button
          type="submit"
          class="rounded-lg bg-blue-600 px-4 py-2 text-sm font-medium text-white transition hover:bg-blue-500 disabled:cursor-not-allowed disabled:opacity-70"
          :disabled="props.isSaving"
        >
          {{ props.isSaving ? 'Saving...' : props.isEditing ? 'Update user' : 'Create user' }}
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
