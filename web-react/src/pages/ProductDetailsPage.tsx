import { useEffect, useState, type FormEvent } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { api } from '../lib/api'
import { useAuth } from '../lib/auth'
import type { ProductDetails, ReviewResponse } from '../types/api'

function typeIcon(type: string) {
  if (type === 'Book') return '📚'
  if (type === 'Laptop') return '💻'
  if (type === 'Mobile') return '📱'
  return '🛍️'
}

function Stars({ value }: { value: number }) {
  const rounded = Math.round(value)
  return (
    <span aria-label={`${value.toFixed(1)} stars`} title={`${value.toFixed(1)} / 5`}>
      {Array.from({ length: 5 }, (_, i) => (
        <span key={i} style={{ color: i < rounded ? '#fbbf24' : '#64748b', letterSpacing: 1 }}>★</span>
      ))}
    </span>
  )
}

export default function ProductDetailsPage() {
  const { id } = useParams()
  const pid = Number(id)
  const [data, setData] = useState<ProductDetails | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [qty, setQty] = useState(1)
  const [adding, setAdding] = useState(false)
  const [reviewRating, setReviewRating] = useState(5)
  const [reviewComment, setReviewComment] = useState('')
  const [reviewSubmitting, setReviewSubmitting] = useState(false)
  const auth = useAuth()
  const navigate = useNavigate()

  const loadProduct = () => {
    if (!Number.isFinite(pid)) return
    setLoading(true)
    setError(null)
    api
      .get<ProductDetails>(`/api/products/${pid}`)
      .then((res) => setData(res.data))
      .catch((err) => setError(err?.response?.data?.message || 'Not found'))
      .finally(() => setLoading(false))
  }

  useEffect(() => {
    loadProduct()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [pid])

  const addToCart = async () => {
    if (!auth.isAuthed) {
      navigate(`/login?next=${encodeURIComponent(`/products/${pid}`)}`)
      return
    }
    setAdding(true)
    try {
      await api.post('/api/cart/items', { productId: pid, qty })
      navigate('/cart')
    } finally {
      setAdding(false)
    }
  }

  const submitReview = async (e: FormEvent) => {
    e.preventDefault()
    if (!auth.isAuthed) {
      navigate(`/login?next=${encodeURIComponent(`/products/${pid}`)}`)
      return
    }
    setReviewSubmitting(true)
    try {
      const res = await api.post<ReviewResponse>(`/api/products/${pid}/reviews`, {
        rating: reviewRating,
        comment: reviewComment.trim(),
      })
      setData((prev) => prev ? {
        ...prev,
        reviews: [res.data, ...prev.reviews],
        reviewCount: prev.reviewCount + 1,
        avgRating: ((prev.avgRating * prev.reviewCount) + res.data.rating) / (prev.reviewCount + 1),
      } : prev)
      setReviewRating(5)
      setReviewComment('')
    } catch (err: any) {
      setError(err?.response?.data?.message || 'Could not submit review')
    } finally {
      setReviewSubmitting(false)
    }
  }

  return (
    <div style={{ display: 'grid', gap: 18 }}>
      <Link to="/products" className="chip-button" style={{ width: 'fit-content' }}>← Back to products</Link>

      {loading && <div className="card"><div className="skeleton" /></div>}
      {error && <div className="alert">{error}</div>}

      {data && (
        <>
          <section className="detail-layout">
            <div className="hero-card">
              <div className="product-icon" style={{ marginBottom: 18 }}>{typeIcon(data.type)}</div>
              <span className="pill">{data.type}</span>
              <h1 className="page-title" style={{ marginTop: 14 }}>{data.name}</h1>
              <p className="page-lead">{data.description}</p>
              <div className="stats-row">
                <div className="stat-card">
                  <div className="stat-value">{data.avgRating.toFixed(1)}</div>
                  <div className="stat-label"><Stars value={data.avgRating} /> average</div>
                </div>
                <div className="stat-card"><div className="stat-value">{data.reviewCount}</div><div className="stat-label">reviews</div></div>
                <div className="stat-card"><div className="stat-value">{new Date(data.createdAt).toLocaleDateString()}</div><div className="stat-label">listed date</div></div>
              </div>
            </div>

            <aside className="card" style={{ position: 'sticky', top: 96 }}>
              <div className="muted small">Current price</div>
              <div className="price" style={{ textAlign: 'left', fontSize: 30, margin: '4px 0 18px' }}>{data.price.toLocaleString()} IRR</div>
              <div className="field">
                <label>Quantity</label>
                <input type="number" min={1} value={qty} onChange={(e) => setQty(Math.max(1, Number(e.target.value)))} />
              </div>
              <button className="btn btn-primary" style={{ width: '100%', marginTop: 14 }} onClick={addToCart} disabled={adding}>
                {adding ? 'Adding...' : auth.isAuthed ? 'Add to cart' : 'Sign in to add'}
              </button>
              <p className="small muted" style={{ marginBottom: 0 }}>Cart actions require a signed-in customer account.</p>
            </aside>
          </section>

          <section className="card" style={{ display: 'grid', gap: 18 }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', gap: 12, alignItems: 'center', flexWrap: 'wrap' }}>
              <div>
                <h2 style={{ margin: 0 }}>Customer reviews</h2>
                <p className="muted small" style={{ margin: '6px 0 0' }}>See what other customers think, or add your own review.</p>
              </div>
              <div className="pill"><Stars value={data.avgRating} /> {data.avgRating.toFixed(1)} / 5</div>
            </div>

            <form onSubmit={submitReview} className="review-form" style={{ display: 'grid', gap: 12 }}>
              <div className="field">
                <label>Your rating</label>
                <select value={reviewRating} onChange={(e) => setReviewRating(Number(e.target.value))}>
                  {[5, 4, 3, 2, 1].map((n) => <option key={n} value={n}>{n} stars</option>)}
                </select>
              </div>
              <div className="field">
                <label>Your review</label>
                <textarea
                  value={reviewComment}
                  onChange={(e) => setReviewComment(e.target.value)}
                  placeholder="Share a short, useful review..."
                  rows={4}
                  required
                  style={{ resize: 'vertical' }}
                />
              </div>
              <button className="btn btn-primary" type="submit" disabled={reviewSubmitting || !reviewComment.trim()}>
                {reviewSubmitting ? 'Submitting...' : auth.isAuthed ? 'Submit review' : 'Sign in to review'}
              </button>
            </form>

            <div style={{ display: 'grid', gap: 12 }}>
              {data.reviews.length === 0 ? (
                <div className="empty-state">No reviews yet. Be the first one to review this product.</div>
              ) : data.reviews.map((r) => (
                <article key={r.id} className="review-card">
                  <div style={{ display: 'flex', justifyContent: 'space-between', gap: 12, flexWrap: 'wrap' }}>
                    <div>
                      <strong>{r.userKey}</strong>
                      <div className="small muted">{new Date(r.createdAt).toLocaleString()}</div>
                    </div>
                    <div><Stars value={r.rating} /> <span className="small muted">{r.rating}/5</span></div>
                  </div>
                  <p style={{ margin: '10px 0 0', lineHeight: 1.65 }}>{r.comment}</p>
                </article>
              ))}
            </div>
          </section>
        </>
      )}
    </div>
  )
}
