<script setup>
import { getSurfaceThemeClass } from '@/shared/uiClasses'

const props = defineProps({
  isDarkTheme: {
    type: Boolean,
    required: true,
  },
})
</script>

<template>
  <section
    class="mx-auto w-full max-w-2xl rounded-xl border p-6"
    :class="getSurfaceThemeClass(props.isDarkTheme)"
  >
    <h1 class="text-xl font-semibold">Clients</h1>
  </section>
</template>
