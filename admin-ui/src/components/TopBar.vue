<script setup>
import { getSurfaceThemeClass } from '@/shared/uiClasses'

const props = defineProps({
  isDarkTheme: {
    type: Boolean,
    required: true,
  },
})

const emit = defineEmits(['toggleTheme'])
</script>

<template>
  <header
    class="flex h-16 items-center justify-between border-b px-6"
    :class="getSurfaceThemeClass(props.isDarkTheme)"
  >
    <div class="flex items-center gap-3">
      <div
        class="flex h-9 w-9 items-center justify-center rounded-lg text-sm font-semibold"
        :class="props.isDarkTheme ? 'bg-slate-700 text-slate-100' : 'bg-slate-900 text-white'"
      >
        C0
      </div>
      <span class="text-sm font-semibold tracking-wide">Admin Panel</span>
    </div>

    <div class="flex items-center gap-4">
      <button
        type="button"
        class="inline-flex items-center rounded-full border px-3 py-1 text-xs font-medium transition"
        :class="
          props.isDarkTheme
            ? 'border-slate-700 bg-slate-800 text-slate-100 hover:bg-slate-700'
            : 'border-slate-300 bg-white text-slate-700 hover:bg-slate-50'
        "
        @click="emit('toggleTheme')"
      >
        {{ props.isDarkTheme ? 'Dark' : 'Light' }}
      </button>

      <div
        class="flex h-9 w-9 items-center justify-center rounded-full text-sm font-semibold"
        :class="props.isDarkTheme ? 'bg-blue-600 text-white' : 'bg-blue-500 text-white'"
      >
        A
      </div>
    </div>
  </header>
</template>

