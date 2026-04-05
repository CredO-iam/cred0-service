<script setup>
const props = defineProps({
  client: {
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

const toCsv = (values) => (values || []).join(', ')
</script>

<template>
  <article
    class="rounded-lg border p-4"
    :class="props.isDarkTheme ? 'border-slate-700 bg-slate-800/50' : 'border-slate-200 bg-slate-50'"
  >
    <div class="flex flex-wrap items-start justify-between gap-3">
      <div>
        <p class="font-medium">{{ props.client.clientName }}</p>
        <p class="text-sm opacity-80">client_id: {{ props.client.clientId }}</p>
        <p class="text-sm opacity-80">scopes: {{ toCsv(props.client.scopes) || '-' }}</p>
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
          @click="emit('edit', props.client)"
        >
          Edit
        </button>
        <button
          type="button"
          class="rounded-md border border-red-500/50 bg-red-500/10 px-3 py-1.5 text-sm font-medium text-red-300 transition hover:bg-red-500/20"
          :disabled="props.isDeleting"
          @click="emit('delete', props.client)"
        >
          {{ props.isDeleting ? 'Deleting...' : 'Delete' }}
        </button>
      </div>
    </div>
  </article>
</template>

