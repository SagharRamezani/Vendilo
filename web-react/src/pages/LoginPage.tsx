import { useState } from 'react'
import { Link, useNavigate, useSearchParams } from 'react-router-dom'
import { useAuth } from '../lib/auth'

export default function LoginPage() {
  const auth = useAuth()
  const navigate = useNavigate()
  const [params] = useSearchParams()
  const next = params.get('next') || '/products'

  const [usernameOrEmail, setUsernameOrEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  return (
    <div className="auth-wrap">
      <section className="hero-card auth-aside auth-panel">
        <span className="pill">Welcome back</span>
        <h1 className="page-title" style={{ marginTop: 14 }}>Sign in to continue shopping.</h1>
        <p className="page-lead">Access your cart, checkout flow and authenticated API endpoints using JWT auth.</p>
        <div className="stats-row">
          <div className="stat-card"><div className="stat-value">JWT</div><div className="stat-label">secure API calls</div></div>
          <div className="stat-card"><div className="stat-value">Cart</div><div className="stat-label">persisted for session</div></div>
          <div className="stat-card"><div className="stat-value">Demo</div><div className="stat-label">sample accounts ready</div></div>
        </div>
      </section>

      <section className="card auth-panel">
        <div>
          <h2 className="section-title">Sign in</h2>
          <p className="muted" style={{ marginTop: -4 }}>Use customer email/phone, seller store code, supporter or manager username.</p>

          <form
            onSubmit={async (e) => {
              e.preventDefault()
              setError(null)
              setLoading(true)
              try {
                await auth.login(usernameOrEmail.trim(), password)
                navigate(next, { replace: true })
              } catch (err: any) {
                const msg = err?.response?.data?.message || 'Invalid credentials'
                setError(msg)
              } finally {
                setLoading(false)
              }
            }}
          >
            <div style={{ display: 'grid', gap: 14 }}>
              <div className="field">
                <label>Account identifier</label>
                <input placeholder="ali@mail.com" value={usernameOrEmail} onChange={(e) => setUsernameOrEmail(e.target.value)} autoFocus />
              </div>
              <div className="field">
                <label>Password</label>
                <input placeholder="Abcd@1234+" type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
              </div>
              <button className="btn btn-primary" disabled={loading || !usernameOrEmail || !password}>
                {loading ? 'Signing in...' : 'Sign in'}
              </button>
              {error && <div className="alert">{error}</div>}
            </div>
          </form>

          <div className="small muted" style={{ marginTop: 16 }}>
            Sample: <b>ali@mail.com</b> / <b>Abcd@1234+</b>
          </div>
          <div className="small muted" style={{ marginTop: 8 }}>
            New here? <Link to="/register">Create an account</Link>
          </div>
        </div>
      </section>
    </div>
  )
}
