<script setup>
const props = defineProps({
  user: {
    type: Object,
    required: true,
  },
  isDarkTheme: {
    type: Boolean,
    required: true,
  },
  isDeleting: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['edit', 'delete'])

const toAttributes = (attributes) => {
  if (!Array.isArray(attributes) || attributes.length === 0) {
    return '-'
  }
  return attributes.map((attribute) => `${attribute.name}: ${attribute.value}`).join(', ')
}
</script>

<template>
  <article
    class="rounded-lg border p-4"
    :class="props.isDarkTheme ? 'border-slate-700 bg-slate-800/50' : 'border-slate-200 bg-slate-50'"
  >
    <div class="flex flex-wrap items-start justify-between gap-3">
      <div>
        <p class="font-medium">{{ props.user.username }}</p>
        <p class="text-sm opacity-80">{{ props.user.firstName }} {{ props.user.lastName }}</p>
        <p class="text-sm opacity-80">{{ props.user.email }}</p>
        <p class="text-sm opacity-80">attributes: {{ toAttributes(props.user.attributes) }}</p>
      </div>

      <div class="flex gap-2">
        <button
          type="button"
          class="rounded-md border px-3 py-1.5 text-sm font-medium transition"
          :class="
            props.isDarkTheme
              ? 'border-slate-600 bg-slate-700 hover:bg-slate-600'
              : 'border-slate-300 bg-white hover:bg-slate-100'
          "
          @click="emit('edit', props.user)"
        >
          Edit
        </button>
        <button
          type="button"
          class="rounded-md border border-red-500/50 bg-red-500/10 px-3 py-1.5 text-sm font-medium text-red-300 transition hover:bg-red-500/20"
          :disabled="props.isDeleting"
          @click="emit('delete', props.user)"
        >
          {{ props.isDeleting ? 'Deleting...' : 'Delete' }}
        </button>
      </div>
    </div>
  </article>
</template>
