# Reviews

The product details page supports customer reviews.

## API

- `GET /api/products/{id}` returns product details plus the latest reviews.
- `GET /api/products/{id}/reviews` returns reviews only.
- `POST /api/products/{id}/reviews` creates a new review.

Request body:

```json
{
  "rating": 5,
  "comment": "Great product!"
}
```

The React UI asks users to sign in before posting a review. The demo API stores reviews in memory.
