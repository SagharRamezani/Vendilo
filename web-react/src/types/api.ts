export type Role = 'CUSTOMER' | 'SELLER' | 'MANAGER' | 'SUPPORT'

export interface UserProfile {
  key: string
  role: Role
  displayName: string
}

export interface AuthResponse {
  accessToken: string
  tokenType: string
  expiresInSeconds: number
  user: UserProfile
}

export interface PagedResponse<T> {
  items: T[]
  page: number
  size: number
  totalItems: number
  totalPages: number
}

export interface ProductSummary {
  id: number
  name: string
  type: string
  price: number
  avgRating: number
  reviewCount: number
  createdAt: string
}

export interface ReviewResponse {
  id: number
  productId: number
  userKey: string
  rating: number
  comment: string
  createdAt: string
}

export interface ProductDetails {
  id: number
  name: string
  type: string
  price: number
  description: string
  avgRating: number
  reviewCount: number
  createdAt: string
  reviews: ReviewResponse[]
}

export interface CartItem {
  itemId: number
  productId: number
  name: string
  price: number
  qty: number
  lineTotal: number
}

export interface CartResponse {
  userKey: string
  items: CartItem[]
  total: number
  currency: string
}

export interface OrderReceipt {
  orderId: number
  userKey: string
  items: CartItem[]
  total: number
  createdAt: string
}
