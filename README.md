# portfolio-mgmt-svc

Java 25 Spring Boot GraphQL service for portfolios, transactions, and mutual-fund
valuations. It calls `market-data-ingestion-svc`, which fetches live MFAPI data and
stores NAV history in PostgreSQL.

## Run the E2E stack

From this directory, with Docker Desktop running:

```bash
docker compose up --build
```

The services are available at:

- GraphiQL: `http://localhost:8080/graphiql`
- GraphQL: `http://localhost:8080/graphql`
- Python market-data API: `http://localhost:8000`
- PostgreSQL: `localhost:5432`

Stop containers while keeping the E2E database:

```bash
docker compose down
```

Use `docker compose down -v` only when a fresh local database is intended.

## GraphQL workflow

Send GraphQL requests to `/graphql` with these headers:

```text
Content-Type: application/json
X-User-Id: e2e-user
```

Search live MFAPI data:

```graphql
query { funds(query: "Parag Parikh") { schemeCode schemeName } }
```

Synchronize a fund returned by search. This sends Java → Python → MFAPI and
upserts real NAV history into PostgreSQL:

```graphql
mutation { synchronizeFund(schemeCode: 122639) { schemeName navRecordsReceived navRecordsUpserted } }
```

Read persisted—not live—NAV data. NAV history is newest first and should be kept
small with `limit`:

```graphql
query { latestNav(schemeCode: 122639) navHistory(schemeCode: 122639, limit: 10) { navs { navDate nav } } }
```

Repeat `synchronizeFund` to verify idempotency: `navRecordsUpserted` is `0` unless
MFAPI changed a historical NAV.

Create a portfolio:

```graphql
mutation { createPortfolio(name: "Long-Term Investments") { id name } }
```

Transactions support `PURCHASE`, `SIP`, and `REDEMPTION`; redemptions cannot exceed
owned units. Holdings calculate units, invested cost, latest persisted NAV, current
value, unrealized gain, and allocation.

## Local development

Set `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`, and optionally
`MARKET_DATA_BASE_URL`, then run `./mvnw spring-boot:run`. Flyway baselines the
shared schema at version 0 and applies the portfolio migration at version 1.

## Security note

`X-User-Id` is deliberately simple MVP authentication for local development. Replace
it with validated user identity and real secrets before deploying publicly.
