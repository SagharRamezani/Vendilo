import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../lib/auth'
import type { Role } from '../types/api'

export default function RegisterPage() {
  const auth = useAuth()
  const navigate = useNavigate()
  const [role, setRole] = useState<Role>('CUSTOMER')
  const [firstName, setFirstName] = useState('')
  const [lastName, setLastName] = useState('')
  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [phone, setPhone] = useState('')
  const [storeName, setStoreName] = useState('')
  const [city, setCity] = useState('')
  const [nationalId, setNationalId] = useState('')
  const [password, setPassword] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const isSeller = role === 'SELLER'
  const needsUsername = role === 'MANAGER' || role === 'SUPPORT'

  return (
    <div className="auth-wrap">
      <section className="card">
        <h2 className="section-title">Create account</h2>
        <p className="muted" style={{ marginTop: -4 }}>Choose a role and the form adapts to the fields required by the API.</p>

        <form
          onSubmit={async (e) => {
            e.preventDefault()
            setError(null)
            setLoading(true)
            try {
              await auth.register({
                role,
                firstName: firstName.trim(),
                lastName: lastName.trim(),
                username: username.trim() || undefined,
                email: email.trim() || undefined,
                phone: phone.trim() || undefined,
                storeName: storeName.trim() || undefined,
                city: city.trim() || undefined,
                nationalId: nationalId.trim() || undefined,
                password,
              })
              navigate('/products', { replace: true })
            } catch (err: any) {
              const msg = err?.response?.data?.message || err?.message || 'Registration failed'
              setError(msg)
            } finally {
              setLoading(false)
            }
          }}
        >
          <div style={{ display: 'grid', gap: 14 }}>
            <div className="field">
              <label>Role</label>
              <select value={role} onChange={(e) => setRole(e.target.value as Role)}>
                <option value="CUSTOMER">Customer</option>
                <option value="SELLER">Seller</option>
                <option value="SUPPORT">Supporter</option>
                <option value="MANAGER">Manager</option>
              </select>
            </div>

            <div className="form-grid" style={{ gridTemplateColumns: '1fr 1fr' }}>
              <div className="field"><label>First name</label><input value={firstName} onChange={(e) => setFirstName(e.target.value)} required /></div>
              <div className="field"><label>Last name</label><input value={lastName} onChange={(e) => setLastName(e.target.value)} required /></div>
            </div>

            {needsUsername && <div className="field"><label>Username</label><input value={username} onChange={(e) => setUsername(e.target.value)} required /></div>}

            {role === 'CUSTOMER' && (
              <>
                <div className="field"><label>Email</label><input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required /></div>
                <div className="field"><label>Phone</label><input value={phone} onChange={(e) => setPhone(e.target.value)} /></div>
              </>
            )}

            {isSeller && (
              <>
                <div className="field"><label>Phone</label><input value={phone} onChange={(e) => setPhone(e.target.value)} required /></div>
                <div className="field"><label>Store name</label><input value={storeName} onChange={(e) => setStoreName(e.target.value)} required /></div>
                <div className="form-grid" style={{ gridTemplateColumns: '1fr 1fr' }}>
                  <div className="field"><label>City / state</label><input value={city} onChange={(e) => setCity(e.target.value)} /></div>
                  <div className="field"><label>National ID</label><input value={nationalId} onChange={(e) => setNationalId(e.target.value)} /></div>
                </div>
              </>
            )}

            <div className="field"><label>Password</label><input placeholder="Test@1234+" type="password" value={password} onChange={(e) => setPassword(e.target.value)} required /></div>
            <button className="btn btn-primary" disabled={loading || !firstName || !lastName || !password}>{loading ? 'Creating...' : 'Create account'}</button>
            {error && <div className="alert">{error}</div>}
          </div>
        </form>

        <div className="small muted" style={{ marginTop: 16 }}>Already have an account? <Link to="/login">Sign in</Link></div>
      </section>

      <section className="hero-card auth-aside auth-panel">
        <span className="pill">Role-based demo</span>
        <h1 className="page-title" style={{ marginTop: 14 }}>Build a marketplace identity.</h1>
        <p className="page-lead">Customers shop, sellers list products, managers enforce hierarchy rules, and supporters assist users.</p>
      </section>
    </div>
  )
}
