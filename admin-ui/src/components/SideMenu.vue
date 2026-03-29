<script setup>
import { getSurfaceThemeClass } from '@/shared/uiClasses'

const props = defineProps({
  isDarkTheme: {
    type: Boolean,
    required: true,
  },
  activeView: {
    type: String,
    required: true,
  },
  navItems: {
    type: Array,
    required: true,
  },
})

const emit = defineEmits(['selectView'])
</script>

<template>
  <aside
    class="w-64 border-r p-4"
    :class="getSurfaceThemeClass(props.isDarkTheme)"
  >
    <nav class="space-y-1">
      <button
        v-for="item in props.navItems"
        :key="item.id"
        type="button"
        class="w-full rounded-lg px-3 py-2 text-left text-sm font-medium transition"
        :class="
          props.activeView === item.id
            ? props.isDarkTheme
              ? 'bg-slate-700 text-slate-50'
              : 'bg-slate-900 text-white'
            : props.isDarkTheme
              ? 'text-slate-300 hover:bg-slate-800'
              : 'text-slate-700 hover:bg-slate-100'
        "
        @click="emit('selectView', item.id)"
      >
        {{ item.label }}
      </button>
    </nav>
  </aside>
</template>

