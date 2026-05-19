import React, { createContext, useContext, useEffect, useMemo, useState } from 'react'

export type ThemeMode = 'dark' | 'light' | 'scheduled'
export type ResolvedTheme = 'dark' | 'light'

const STORAGE_KEY = 'vendilo.theme.mode'

function scheduledTheme(now = new Date()): ResolvedTheme {
  const hour = now.getHours()
  // Daytime: 07:00-18:59 => light, otherwise dark.
  return hour >= 7 && hour < 19 ? 'light' : 'dark'
}

function loadMode(): ThemeMode {
  const raw = localStorage.getItem(STORAGE_KEY)
  return raw === 'light' || raw === 'dark' || raw === 'scheduled' ? raw : 'dark'
}

type ThemeContextValue = {
  mode: ThemeMode
  resolvedTheme: ResolvedTheme
  setMode: (mode: ThemeMode) => void
}

const ThemeContext = createContext<ThemeContextValue | null>(null)

export function ThemeProvider({ children }: { children: React.ReactNode }) {
  const [mode, setModeState] = useState<ThemeMode>(() => loadMode())
  const [resolvedTheme, setResolvedTheme] = useState<ResolvedTheme>(() =>
    mode === 'scheduled' ? scheduledTheme() : mode,
  )

  const setMode = (next: ThemeMode) => {
    localStorage.setItem(STORAGE_KEY, next)
    setModeState(next)
    setResolvedTheme(next === 'scheduled' ? scheduledTheme() : next)
  }

  useEffect(() => {
    const update = () => setResolvedTheme(mode === 'scheduled' ? scheduledTheme() : mode)
    update()
    const timer = window.setInterval(update, 60_000)
    return () => window.clearInterval(timer)
  }, [mode])

  useEffect(() => {
    document.documentElement.dataset.theme = resolvedTheme
  }, [resolvedTheme])

  const value = useMemo<ThemeContextValue>(
    () => ({ mode, resolvedTheme, setMode }),
    [mode, resolvedTheme],
  )

  return <ThemeContext.Provider value={value}>{children}</ThemeContext.Provider>
}

export function useTheme(): ThemeContextValue {
  const ctx = useContext(ThemeContext)
  if (!ctx) throw new Error('useTheme must be used within ThemeProvider')
  return ctx
}
