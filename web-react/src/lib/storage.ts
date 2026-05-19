const TOKEN_KEY = 'mp.jwt'
const USER_KEY = 'mp.user'

export function getToken(): string | null {
  return localStorage.getItem(TOKEN_KEY)
}

export function setToken(token: string) {
  localStorage.setItem(TOKEN_KEY, token)
}

export function clearToken() {
  localStorage.removeItem(TOKEN_KEY)
}

export function getUserKey(): string | null {
  return localStorage.getItem(USER_KEY)
}

export function setUserKey(key: string) {
  localStorage.setItem(USER_KEY, key)
}

export function clearUserKey() {
  localStorage.removeItem(USER_KEY)
}
