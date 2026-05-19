import React, { createContext, useContext, useMemo, useState } from 'react'
import { api } from './api'
import { clearToken, clearUserKey, getToken, getUserKey, setToken, setUserKey } from './storage'
import type { AuthResponse, Role } from '../types/api'

type RegisterPayload = {
  role: Role
  firstName: string
  lastName: string
  username?: string
  email?: string
  phone?: string
  storeName?: string
  city?: string
  nationalId?: string
  storeCode?: string
  password: string
}

type AuthState = {
  token: string | null
  userKey: string | null
  isAuthed: boolean
  login: (usernameOrEmail: string, password: string) => Promise<void>
  register: (payload: RegisterPayload) => Promise<void>
  logout: () => void
}

const AuthContext = createContext<AuthState | null>(null)

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [token, setTokenState] = useState<string | null>(getToken())
  const [userKey, setUserKeyState] = useState<string | null>(getUserKey())

  const applyAuth = (res: AuthResponse) => {
    setToken(res.accessToken)
    setUserKey(res.user.key)
    setTokenState(res.accessToken)
    setUserKeyState(res.user.key)
  }

  const logout = () => {
    clearToken()
    clearUserKey()
    setTokenState(null)
    setUserKeyState(null)
  }

  const login = async (usernameOrEmail: string, password: string) => {
    const res = await api.post<AuthResponse>('/api/auth/login', { usernameOrEmail, password })
    applyAuth(res.data)
  }

  const register = async (payload: RegisterPayload) => {
    const res = await api.post<AuthResponse>('/api/auth/register', payload)
    applyAuth(res.data)
  }

  const value = useMemo<AuthState>(() => ({
    token,
    userKey,
    isAuthed: Boolean(token),
    login,
    register,
    logout,
  }), [token, userKey])

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth(): AuthState {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used within AuthProvider')
  return ctx
}
