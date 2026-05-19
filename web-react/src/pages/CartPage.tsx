import { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { api } from '../lib/api'
import { useAuth } from '../lib/auth'
import type { CartResponse, OrderReceipt } from '../types/api'

export default function CartPage() {
  const auth = useAuth()
  const navigate = useNavigate()
  const [cart, setCart] = useState<CartResponse | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [receipt, setReceipt] = useState<OrderReceipt | null>(null)

  const loadCart = () => {
    setLoading(true)
    setError(null)
    api
      .get<CartResponse>('/api/cart')
      .then((res) => setCart(res.data))
      .catch((err) => {
        if (err?.response?.status === 401) {
          auth.logout()
          navigate('/login?next=%2Fcart')
          return
        }
        setError(err?.response?.data?.message || 'Failed to load cart')
      })
      .finally(() => setLoading(false))
  }

  useEffect(() => {
    if (!auth.isAuthed) {
      navigate('/login?next=%2Fcart')
      return
    }
    loadCart()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [auth.isAuthed])

  const removeItem = async (productId: number) => {
    await api.delete(`/api/cart/items/${productId}`)
    loadCart()
  }

  const checkout = async () => {
    const res = await api.post<OrderReceipt>('/api/orders/checkout')
    setReceipt(res.data)
    loadCart()
  }

  const items = cart?.items ?? []

  return (
    <div style={{ display: 'grid', gap: 18 }}>
      <section className="hero-card">
        <span className="pill">Secure checkout</span>
        <h1 className="page-title" style={{ marginTop: 14 }}>Your cart</h1>
        <p className="page-lead">Review your selections, remove items, and finish the checkout flow with the REST API.</p>
      </section>

      <section className="card">
        {loading && <div className="product-list"><div className="skeleton" /><div className="skeleton" /></div>}
        {error && <div className="alert">{error}</div>}
        {!loading && cart && items.length === 0 && (
          <div className="empty-state">
            <div style={{ fontSize: 38, marginBottom: 8 }}>🛒</div>
            <b>Your cart is empty</b>
            <div className="small" style={{ marginTop: 6 }}><Link to="/products">Browse products</Link> to add something.</div>
          </div>
        )}
        {!loading && cart && items.length > 0 && (
          <>
            <div style={{ display: 'grid', gap: 12 }}>
              {items.map((it) => (
                <div key={it.productId} className="cart-item">
                  <div>
                    <div className="product-name"><Link to={`/products/${it.productId}`} style={{ textDecoration: 'none' }}>{it.name}</Link></div>
                    <div className="product-meta"><span className="pill">Qty {it.qty}</span><span className="pill">{it.price.toLocaleString()} IRR each</span></div>
                  </div>
                  <div style={{ textAlign: 'right' }}>
                    <div className="price">{it.lineTotal.toLocaleString()} IRR</div>
                    <button className="btn btn-small btn-danger" style={{ marginTop: 8 }} onClick={() => removeItem(it.productId)}>Remove</button>
                  </div>
                </div>
              ))}
            </div>

            <div className="card" style={{ marginTop: 16, background: 'var(--surface-2)' }}>
              <div className="form-row">
                <div>
                  <div className="muted small">Cart total</div>
                  <div className="price" style={{ textAlign: 'left', fontSize: 28 }}>{cart.total.toLocaleString()} IRR</div>
                </div>
                <button className="btn btn-primary" onClick={checkout}>Checkout</button>
              </div>
            </div>
          </>
        )}
      </section>

      {receipt && (
        <div className="card success">
          <h3 style={{ marginTop: 0 }}>Order receipt</h3>
          <div>Order #{receipt.orderId} · {new Date(receipt.createdAt).toLocaleString()}</div>
          <div style={{ marginTop: 10 }}>Total: <b>{receipt.total.toLocaleString()} IRR</b></div>
        </div>
      )}
    </div>
  )
}
