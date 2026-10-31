# End-to-end test

From this directory, start the full stack:

```bash
docker compose up --build
```

All GraphQL calls require `X-User-Id: e2e-user` and go to `http://localhost:8080/graphql`.
Use GraphiQL at `http://localhost:8080/graphiql` or send the following requests with Postman.

1. Search (E2E-01):

```graphql
query { funds(query: "Parag Parikh") { schemeCode schemeName } }
```

2. Copy one `schemeCode` from the response and synchronize it (E2E-02):

```graphql
mutation { synchronizeFund(schemeCode: 122639) { schemeName navRecordsReceived navRecordsUpserted } }
```

3. Verify persistent reads (E2E-03/04):

```graphql
query { latestNav(schemeCode: 122639) navHistory(schemeCode: 122639, limit: 20) { navs { navDate nav } } }
```

`navHistory.navs` is descending by date and comes from PostgreSQL, not MFAPI.

4. Run the same synchronization again (E2E-05). The second response reports
`navRecordsUpserted: 0` unless MFAPI changed a historical NAV. Confirm directly:

```bash
docker compose exec postgres psql -U investiq -d investiq -c 'select scheme_code, nav_date, count(*) from nav_history group by 1,2 having count(*) > 1;'
```

5. Run `docker compose restart market-data portfolio-mgmt` then repeat the persistent
read (E2E-06). The named PostgreSQL volume retains its data.

6. Query `latestNav(schemeCode: 999999999)` for E2E-07; GraphQL returns an error
response rather than crashing either service. Stop Python with `docker compose stop
market-data` and repeat a `funds` or `latestNav` query for E2E-08; Java returns a
GraphQL error with message `Market data is unavailable`.

Finish with `docker compose down`; use `docker compose down -v` only when you intend
to delete the E2E database volume.
