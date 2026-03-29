<script setup>
import { getSurfaceThemeClass } from '@/shared/uiClasses'

const props = defineProps({
  isDarkTheme: {
    type: Boolean,
    required: true,
  },
  email: {
    type: String,
    required: true,
  },
  fullName: {
    type: String,
    required: true,
  },
})

const emit = defineEmits(['update:email', 'update:full-name'])
</script>

<template>
  <section
    class="mx-auto w-full max-w-2xl rounded-xl border p-6"
    :class="getSurfaceThemeClass(props.isDarkTheme)"
  >
    <h1 class="text-xl font-semibold">Settings</h1>
    <p :class="props.isDarkTheme ? 'mt-1 text-sm text-slate-400' : 'mt-1 text-sm text-slate-500'">
      Manage your account data.
    </p>

    <form class="mt-6 space-y-5">
      <label class="block">
        <span class="mb-1 block text-sm font-medium">Email</span>
        <input
          :value="props.email"
          type="email"
          class="w-full rounded-lg border px-3 py-2 text-sm outline-none transition focus:ring-2"
          :class="
            props.isDarkTheme
              ? 'border-slate-700 bg-slate-800 text-slate-100 focus:ring-blue-500/40'
              : 'border-slate-300 bg-white text-slate-900 focus:ring-blue-400/40'
          "
          @input="emit('update:email', $event.target.value)"
        >
      </label>

      <label class="block">
        <span class="mb-1 block text-sm font-medium">Full name</span>
        <input
          :value="props.fullName"
          type="text"
          class="w-full rounded-lg border px-3 py-2 text-sm outline-none transition focus:ring-2"
          :class="
            props.isDarkTheme
              ? 'border-slate-700 bg-slate-800 text-slate-100 focus:ring-blue-500/40'
              : 'border-slate-300 bg-white text-slate-900 focus:ring-blue-400/40'
          "
          @input="emit('update:full-name', $event.target.value)"
        >
      </label>

      <button
        type="button"
        class="rounded-lg bg-blue-600 px-4 py-2 text-sm font-medium text-white transition hover:bg-blue-500"
      >
        Change password
      </button>
    </form>
  </section>
</template>
