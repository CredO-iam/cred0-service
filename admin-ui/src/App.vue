<script setup>
import { computed, ref } from 'vue'
import TopBar from './components/TopBar.vue'
import SideMenu from './components/SideMenu.vue'
import SettingsPage from './components/SettingsPage.vue'
import ClientsPage from './components/ClientsPage.vue'
import UsersPage from './components/UsersPage.vue'

const activeView = ref('settings')
const theme = ref('light')

const navItems = [
  { id: 'settings', label: 'Settings' },
  { id: 'clients', label: 'Clients' },
  { id: 'users', label: 'Users' },
]

const isDarkTheme = computed(() => theme.value === 'dark')

const settingsForm = ref({
  email: 'admin@cred0.local',
  fullName: 'Admin User',
})

const toggleTheme = () => {
  theme.value = theme.value === 'light' ? 'dark' : 'light'
}

const updateEmail = (email) => {
  settingsForm.value.email = email
}

const updateFullName = (fullName) => {
  settingsForm.value.fullName = fullName
}

const selectView = (viewId) => {
  activeView.value = viewId
}
</script>

<template>
  <div
    class="min-h-screen"
    :class="isDarkTheme ? 'bg-slate-950 text-slate-100' : 'bg-slate-100 text-slate-900'"
  >
    <TopBar :is-dark-theme="isDarkTheme" @toggle-theme="toggleTheme" />

    <div class="flex min-h-[calc(100vh-4rem)]">
      <SideMenu
        :is-dark-theme="isDarkTheme"
        :nav-items="navItems"
        :active-view="activeView"
        @select-view="selectView"
      />

      <main class="flex-1 p-6 sm:p-8">
        <SettingsPage
          v-if="activeView === 'settings'"
          :is-dark-theme="isDarkTheme"
          :email="settingsForm.email"
          :full-name="settingsForm.fullName"
          @update:email="updateEmail"
          @update:full-name="updateFullName"
        />

        <ClientsPage v-if="activeView === 'clients'" :is-dark-theme="isDarkTheme" />

        <UsersPage v-if="activeView === 'users'" :is-dark-theme="isDarkTheme" />
      </main>
    </div>
  </div>
</template>
