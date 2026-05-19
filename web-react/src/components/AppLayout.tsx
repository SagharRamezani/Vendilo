import { NavLink, Outlet, useNavigate } from 'react-router-dom'
import { useAuth } from '../lib/auth'
import { useTheme, type ThemeMode } from '../lib/theme'

const themeOptions: Array<{ value: ThemeMode; label: string }> = [
  { value: 'dark', label: 'Dark' },
  { value: 'light', label: 'Light' },
  { value: 'scheduled', label: 'Scheduled' },
]

export default function AppLayout() {
  const auth = useAuth()
  const theme = useTheme()
  const navigate = useNavigate()

  return (
    <div className="shell">
      <header className="topbar">
        <NavLink to="/products" className="brand" aria-label="Go to products">
          <span className="logo">V</span>
          <span>
            <span className="brand-title">Vendilo</span>
            <span className="brand-subtitle">Algorithmic marketplace</span>
          </span>
        </NavLink>

        <nav className="nav-cluster" aria-label="Main navigation">
          <NavLink to="/products" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>Products</NavLink>
          <NavLink to="/cart" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>Cart</NavLink>

          <label className="theme-control" title="Choose UI theme">
            <span>Theme</span>
            <select value={theme.mode} onChange={(e) => theme.setMode(e.target.value as ThemeMode)}>
              {themeOptions.map((option) => (
                <option key={option.value} value={option.value}>{option.label}</option>
              ))}
            </select>
          </label>

          {auth.isAuthed ? (
            <>
              <span className="pill">Signed in: <b>{auth.userKey}</b></span>
              <button
                className="btn btn-small"
                onClick={() => {
                  auth.logout()
                  navigate('/products')
                }}
              >
                Logout
              </button>
            </>
          ) : (
            <>
              <NavLink to="/login" className="nav-link">Sign in</NavLink>
              <NavLink to="/register" className="btn btn-primary">Sign up</NavLink>
            </>
          )}
        </nav>
      </header>

      <main>
        <Outlet />
      </main>

      <footer className="footer">
        <span>API base: {import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'}</span>
        <span>Theme: {theme.mode} ({theme.resolvedTheme})</span>
      </footer>
    </div>
  )
}
