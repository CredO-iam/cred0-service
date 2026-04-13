const API_BASE_URL = (import.meta.env.VITE_API_BASE_URL || '').replace(/\/$/, '')
const ADMIN_API_PREFIX = `${API_BASE_URL}/admin`
const LOGIN_URL = '/login'
const DEV_LOGIN_PROXY_URL = '/auth/login'

export const loginProcessingUrl = () => (API_BASE_URL ? `${API_BASE_URL}/login` : DEV_LOGIN_PROXY_URL)

export const adminEndpoint = (path) => {
  const normalizedPath = path.startsWith('/') ? path : `/${path}`
  return `${ADMIN_API_PREFIX}${normalizedPath}`
}

const shouldRedirectToLogin = (response) => {
  if (response.status === 401 || response.status === 403) {
    return true
  }

  return response.redirected && response.url.includes('/login')
}

export const fetchAdmin = async (path, init = {}) => {
  const response = await fetch(adminEndpoint(path), {
    credentials: 'include',
    ...init,
  })

  if (shouldRedirectToLogin(response)) {
    globalThis.location.assign(LOGIN_URL)
    throw new Error('Authentication required. Redirecting to login...')
  }

  return response
}

export const submitLoginForm = async ({ username, password }) => {
  const body = new URLSearchParams({ username, password })

  const response = await fetch(loginProcessingUrl(), {
    method: 'POST',
    credentials: 'include',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
    },
    body: body.toString(),
  })

  const loginErrorUrl = '/login?error'
  const isInvalidCredentials = response.url.includes(loginErrorUrl)
  if (response.status === 401 || response.status === 403 || isInvalidCredentials) {
    return { ok: false }
  }

  return { ok: true }
}

