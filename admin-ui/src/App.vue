<script setup>
import { computed, onMounted, ref } from 'vue'
import { fetchAdmin } from '@/shared/adminApi'
import TopBar from './components/TopBar.vue'
import SideMenu from './components/SideMenu.vue'
import SettingsPage from './components/SettingsPage.vue'
import ClientsPage from './components/ClientsPage.vue'
import UsersPage from './components/UsersPage.vue'
import GroupsPage from './components/GroupsPage.vue'
import RolesPage from './components/RolesPage.vue'
import LoginPage from './components/LoginPage.vue'

const activeView = ref('settings')
const theme = ref('light')

const navItems = [
  { id: 'settings', label: 'Settings' },
  { id: 'clients', label: 'Clients' },
  { id: 'users', label: 'Users' },
  { id: 'groups', label: 'Groups' },
  { id: 'roles', label: 'Roles' },
]

const isDarkTheme = computed(() => theme.value === 'dark')
const isLoginRoute = computed(() => window.location.pathname === '/login')

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

onMounted(async () => {
  if (isLoginRoute.value) {
    return
  }

  try {
    await fetchAdmin('/users')
  } catch {
    // fetchAdmin handles login redirect for unauthenticated sessions.
  }
})
</script>

<template>
  <div
    class="min-h-screen"
    :class="isDarkTheme ? 'bg-slate-950 text-slate-100' : 'bg-slate-100 text-slate-900'"
  >
    <TopBar v-if="!isLoginRoute" :is-dark-theme="isDarkTheme" @toggle-theme="toggleTheme" />

    <div class="flex min-h-[calc(100vh-4rem)]">
      <SideMenu
        v-if="!isLoginRoute"
        :is-dark-theme="isDarkTheme"
        :nav-items="navItems"
        :active-view="activeView"
        @select-view="selectView"
      />

      <main class="flex-1 p-6 sm:p-8">
        <LoginPage v-if="isLoginRoute" :is-dark-theme="isDarkTheme" />

        <SettingsPage
          v-else-if="activeView === 'settings'"
          :is-dark-theme="isDarkTheme"
          :email="settingsForm.email"
          :full-name="settingsForm.fullName"
          @update:email="updateEmail"
          @update:full-name="updateFullName"
        />

        <ClientsPage v-else-if="activeView === 'clients'" :is-dark-theme="isDarkTheme" />

        <UsersPage v-else-if="activeView === 'users'" :is-dark-theme="isDarkTheme" />
        <GroupsPage v-else-if="activeView === 'groups'" :is-dark-theme="isDarkTheme" />
        <RolesPage v-else-if="activeView === 'roles'" :is-dark-theme="isDarkTheme" />
      </main>
    </div>
  </div>
</template>
