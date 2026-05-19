import { useEffect, useMemo, useState } from 'react'
import { Link, useSearchParams } from 'react-router-dom'
import { api } from '../lib/api'
import { useDebouncedValue } from '../lib/useDebouncedValue'
import type { PagedResponse, ProductSummary } from '../types/api'

const SORT_OPTIONS = [
  { value: 'PRICE_ASC', label: 'Price: low to high' },
  { value: 'PRICE_DESC', label: 'Price: high to low' },
  { value: 'NAME_ASC', label: 'Name: A to Z' },
  { value: 'NAME_DESC', label: 'Name: Z to A' },
  { value: 'RATING_DESC', label: 'Rating: best first' },
]

const PRODUCT_ICONS: Record<string, string> = {
  Book: '📚',
  Laptop: '💻',
  Mobile: '📱',
}

function getNum(param: string | null, fallback: number) {
  const n = Number(param)
  return Number.isFinite(n) ? n : fallback
}

function iconFor(type: string) {
  return PRODUCT_ICONS[type] ?? '🛍️'
}

export default function ProductsPage() {
  const [params, setParams] = useSearchParams()

  const q = params.get('q') || ''
  const type = params.get('type') || ''
  const minPrice = params.get('minPrice') || ''
  const maxPrice = params.get('maxPrice') || ''
  const minRating = params.get('minRating') || ''
  const fuzzy = params.get('fuzzy') === 'true'
  const sort = params.get('sort') || 'PRICE_ASC'
  const page = getNum(params.get('page'), 0)
  const size = Math.min(100, Math.max(1, getNum(params.get('size'), 20)))

  const debouncedQ = useDebouncedValue(q, 300)

  const query = useMemo(() => {
    const qp: Record<string, string> = {
      page: String(page),
      size: String(size),
      sort,
      fuzzy: String(fuzzy),
    }
    if (debouncedQ.trim()) qp.q = debouncedQ.trim()
    if (type.trim()) qp.type = type.trim()
    if (minPrice.trim()) qp.minPrice = minPrice.trim()
    if (maxPrice.trim()) qp.maxPrice = maxPrice.trim()
    if (minRating.trim()) qp.minRating = minRating.trim()
    return qp
  }, [debouncedQ, type, minPrice, maxPrice, minRating, fuzzy, sort, page, size])

  const [data, setData] = useState<PagedResponse<ProductSummary> | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    let alive = true
    setLoading(true)
    setError(null)
    api
      .get<PagedResponse<ProductSummary>>('/api/products', { params: query })
      .then((res) => {
        if (!alive) return
        setData(res.data)
      })
      .catch((err) => {
        if (!alive) return
        setError(err?.response?.data?.message || 'Failed to load products')
        setData(null)
      })
      .finally(() => alive && setLoading(false))
    return () => { alive = false }
  }, [query])

  const update = (patch: Record<string, string | null>) => {
    const next = new URLSearchParams(params)
    for (const [k, v] of Object.entries(patch)) {
      if (v === null || v === '') next.delete(k)
      else next.set(k, v)
    }
    if ('q' in patch || 'type' in patch || 'minPrice' in patch || 'maxPrice' in patch || 'minRating' in patch || 'fuzzy' in patch || 'sort' in patch) {
      next.set('page', '0')
    }
    setParams(next, { replace: true })
  }

  const totalItems = data?.totalItems ?? 0
  const hasFilters = Boolean(q || type || minPrice || maxPrice || minRating || fuzzy)

  return (
    <div style={{ display: 'grid', gap: 18 }}>
      <section className="hero-card">
        <span className="pill">Data structures powered search</span>
        <h1 className="page-title" style={{ marginTop: 14 }}>Discover Vendilo products faster.</h1>
        <p className="page-lead">
          Filter by category, price and rating. Toggle fuzzy search to showcase BK-Tree based matching.
        </p>
        <div className="stats-row">
          <div className="stat-card"><div className="stat-value">{totalItems}</div><div className="stat-label">matched products</div></div>
          <div className="stat-card"><div className="stat-value">BK-Tree</div><div className="stat-label">fuzzy search index</div></div>
          <div className="stat-card"><div className="stat-value">URL</div><div className="stat-label">shareable filters</div></div>
        </div>
      </section>

      <section className="card">
        <div className="form-grid">
          <div className="field">
            <label>Keyword</label>
            <input placeholder="Search books, laptops, mobiles" value={q} onChange={(e) => update({ q: e.target.value })} />
          </div>
          <div className="field">
            <label>Type</label>
            <input placeholder="Book, Laptop, Mobile" value={type} onChange={(e) => update({ type: e.target.value })} />
          </div>
          <div className="field">
            <label>Min price</label>
            <input inputMode="numeric" placeholder="150000" value={minPrice} onChange={(e) => update({ minPrice: e.target.value })} />
          </div>
          <div className="field">
            <label>Max price</label>
            <input inputMode="numeric" placeholder="52000000" value={maxPrice} onChange={(e) => update({ maxPrice: e.target.value })} />
          </div>
        </div>

        <div className="form-row" style={{ marginTop: 14 }}>
          <div className="hstack" style={{ display: 'flex', gap: 12, alignItems: 'center', flexWrap: 'wrap' }}>
            <label className="checkbox-row">
              <input type="checkbox" checked={fuzzy} onChange={(e) => update({ fuzzy: String(e.target.checked) })} />
              Fuzzy search
            </label>
            <div className="field" style={{ minWidth: 140 }}>
              <label>Min rating</label>
              <input inputMode="decimal" placeholder="0" value={minRating} onChange={(e) => update({ minRating: e.target.value })} />
            </div>
          </div>

          <div className="hstack" style={{ display: 'flex', gap: 10, alignItems: 'end', flexWrap: 'wrap' }}>
            <div className="field" style={{ minWidth: 210 }}>
              <label>Sort</label>
              <select value={sort} onChange={(e) => update({ sort: e.target.value })}>
                {SORT_OPTIONS.map((o) => <option key={o.value} value={o.value}>{o.label}</option>)}
              </select>
            </div>
            <div className="field" style={{ width: 120 }}>
              <label>Page size</label>
              <select value={String(size)} onChange={(e) => update({ size: e.target.value })}>
                {[10, 20, 50, 100].map((n) => <option key={n} value={String(n)}>{n}</option>)}
              </select>
            </div>
            <button className="btn" onClick={() => setParams(new URLSearchParams(), { replace: true })} disabled={!hasFilters}>Clear</button>
          </div>
        </div>
      </section>

      <section className="card">
        <div className="form-row" style={{ marginBottom: 14 }}>
          <div>
            <h2 className="section-title">Results</h2>
            <div className="muted">Showing <b>{data?.items.length ?? 0}</b> of <b>{totalItems}</b> items</div>
          </div>
          {data && <Pagination page={data.page} totalPages={data.totalPages} onPageChange={(p) => update({ page: String(p) })} />}
        </div>

        {loading && <div className="product-list"><div className="skeleton" /><div className="skeleton" /><div className="skeleton" /></div>}
        {error && <div className="alert">{error}</div>}
        {!loading && !error && data && data.items.length === 0 && (
          <div className="empty-state">
            <div style={{ fontSize: 34, marginBottom: 8 }}>🔎</div>
            <b>No products found</b>
            <div className="small" style={{ marginTop: 6 }}>Try changing your filters or disabling fuzzy search.</div>
          </div>
        )}
        {!loading && !error && data && data.items.length > 0 && (
          <>
            <div className="product-list">
              {data.items.map((p) => (
                <Link to={`/products/${p.id}`} key={p.id} className="product-card">
                  <div className="product-icon">{iconFor(p.type)}</div>
                  <div>
                    <div className="product-name">{p.name}</div>
                    <div className="product-meta">
                      <span className="pill">{p.type}</span>
                      <span className="pill">created {new Date(p.createdAt).toLocaleDateString()}</span>
                    </div>
                  </div>
                  <div className="price-block">
                    <div className="price">{p.price.toLocaleString()} IRR</div>
                    <div className="rating">rating {p.avgRating.toFixed(2)}</div>
                  </div>
                </Link>
              ))}
            </div>
            <div style={{ marginTop: 16 }}>
              <Pagination page={data.page} totalPages={data.totalPages} onPageChange={(p) => update({ page: String(p) })} />
            </div>
          </>
        )}
      </section>
    </div>
  )
}

function Pagination({ page, totalPages, onPageChange }: { page: number; totalPages: number; onPageChange: (p: number) => void }) {
  const safeTotal = Math.max(1, totalPages)
  return (
    <div className="pagination">
      <button className="btn btn-small" disabled={page <= 0} onClick={() => onPageChange(page - 1)}>← Prev</button>
      <span className="pill">Page <b>{page + 1}</b> / {safeTotal}</span>
      <button className="btn btn-small" disabled={page + 1 >= totalPages} onClick={() => onPageChange(page + 1)}>Next →</button>
    </div>
  )
}
