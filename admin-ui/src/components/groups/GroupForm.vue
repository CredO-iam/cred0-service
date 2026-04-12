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
</script>

<template>
  <div>
    <div class="flex items-center justify-between gap-3">
      <h2 class="text-sm font-semibold uppercase tracking-wide">
        {{ props.isEditing ? 'Edit group' : 'New group' }}
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
        <span class="mb-1 block text-sm font-medium">Name</span>
        <input
          :value="props.modelValue.name"
          type="text"
          required
          class="w-full rounded-lg border px-3 py-2 text-sm outline-none transition focus:ring-2"
          :class="
            props.isDarkTheme
              ? 'border-slate-700 bg-slate-800 text-slate-100 focus:ring-blue-500/40'
              : 'border-slate-300 bg-white text-slate-900 focus:ring-blue-400/40'
          "
          @input="updateField('name', $event.target.value)"
        >
      </label>

      <label class="block">
        <span class="mb-1 block text-sm font-medium">Description</span>
        <textarea
          :value="props.modelValue.description"
          rows="3"
          class="w-full rounded-lg border px-3 py-2 text-sm outline-none transition focus:ring-2"
          :class="
            props.isDarkTheme
              ? 'border-slate-700 bg-slate-800 text-slate-100 focus:ring-blue-500/40'
              : 'border-slate-300 bg-white text-slate-900 focus:ring-blue-400/40'
          "
          @input="updateField('description', $event.target.value)"
        />
      </label>

      <template v-if="props.isEditing">
        <label class="block">
          <span class="mb-1 block text-sm font-medium">User IDs (comma separated UUIDs)</span>
          <input
            :value="props.modelValue.userIds"
            type="text"
            placeholder="11111111-1111-1111-1111-111111111111"
            class="w-full rounded-lg border px-3 py-2 text-sm outline-none transition focus:ring-2"
            :class="
              props.isDarkTheme
                ? 'border-slate-700 bg-slate-800 text-slate-100 focus:ring-blue-500/40'
                : 'border-slate-300 bg-white text-slate-900 focus:ring-blue-400/40'
            "
            @input="updateField('userIds', $event.target.value)"
          >
        </label>

        <label class="block">
          <span class="mb-1 block text-sm font-medium">Role IDs (comma separated UUIDs)</span>
          <input
            :value="props.modelValue.roleIds"
            type="text"
            placeholder="22222222-2222-2222-2222-222222222222"
            class="w-full rounded-lg border px-3 py-2 text-sm outline-none transition focus:ring-2"
            :class="
              props.isDarkTheme
                ? 'border-slate-700 bg-slate-800 text-slate-100 focus:ring-blue-500/40'
                : 'border-slate-300 bg-white text-slate-900 focus:ring-blue-400/40'
            "
            @input="updateField('roleIds', $event.target.value)"
          >
        </label>
      </template>

      <div class="flex gap-2">
        <button
          type="submit"
          class="rounded-lg bg-blue-600 px-4 py-2 text-sm font-medium text-white transition hover:bg-blue-500 disabled:cursor-not-allowed disabled:opacity-70"
          :disabled="props.isSaving"
        >
          {{ props.isSaving ? 'Saving...' : props.isEditing ? 'Update group' : 'Create group' }}
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
