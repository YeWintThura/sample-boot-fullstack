# In-Class Demonstration Flow — Fullstack: AJAX frontend + REST backend

This repository has these branches:

- **`main`** — the in-class version. Some code is blanked out with `TODO` comments. We fill these in together during the demonstration.
- **[`complete`](https://github.com/cnacha-mfu/sample-boot-fullstack/tree/complete)** — the finished code for every step. Use it to catch up or check your work.

**This demo runs TWO applications at the same time** — that is the whole point:

| App | Port | Role |
|---|---|---|
| `backend/` | **8081** | REST API + H2 database (Customer/Product/SaleOrder) |
| `frontend/` | **8080** | serves one static page whose JavaScript calls the backend |

Start each in its own terminal:

```
mvn spring-boot:run --file backend\pom.xml
mvn spring-boot:run --file frontend\pom.xml
```

> **Postman:** import [`sample-boot-fullstack.postman_collection.json`](sample-boot-fullstack.postman_collection.json) to poke the backend directly.

---

## Step 1: Two apps, two ports — the fullstack shape

**Files:** `backend/src/main/resources/application.properties`, `frontend/src/main/resources/application.properties` (read together)

**Idea:** until now one Spring Boot app did everything. Real systems split: a **backend** that speaks only JSON, and a **frontend** that serves HTML/JS to the browser — the browser then talks to the backend directly with AJAX. Two apps → two ports (`8081` / `8080`).

**Try it:** start both. Backend: <http://localhost:8081/customers> shows raw JSON (20 customers from `data.sql`). Frontend: <http://localhost:8080/customer.html> shows the page — form works? Not yet: the display code is our TODO.

---

## Step 2: Verify the API before writing any JavaScript

**Tool:** Postman (collection above)

**Idea:** never debug two things at once. First prove the backend works with Postman (`GET /customers`, `POST /customers`), then any remaining bug must be in the JavaScript. Note the JSON field names — `fullname`, `tel` — they come from `@JsonProperty` on the `Customer` entity, and the frontend must use exactly these.

---

## Step 3: AJAX GET — fetch JSON and build the page

**File:** `frontend/src/main/resources/static/customer.html`

**Idea:** `$.ajax({url, type: 'GET', success})` — the browser fetches JSON from **another server** and jQuery builds HTML from it. The page never reloads.

**Fill in together:** in `fetchCustomers`' `success`, loop with `$.each(data, function(index, customer) {...})` and append a `<div>` per customer (template literal with `${customer.fullname}`, `${customer.address}`, `${customer.email}`, `${customer.tel}`, `${customer.birthday}`), then a fallback `console.error` in `error`.

**Try it:** reload <http://localhost:8080/customer.html> — 20 customers appear. Open DevTools (F12) → **Network** tab → see the `customers` request go to port 8081.

**Solution:** [customer.html](https://github.com/cnacha-mfu/sample-boot-fullstack/blob/complete/frontend/src/main/resources/static/customer.html)

---

## Step 4: AJAX POST — submit the form without leaving the page

**File:** `customer.html` (same file)

**Idea:** `event.preventDefault()` stops the browser's own form submit; instead we build a JSON object ourselves and POST it with `contentType: 'application/json'`. On success, re-fetch the list — the page updates in place.

**Fill in together:**
1. Build `formData` from the inputs — keys `fullname`, `address`, `email`, `tel`, `birthday` (must match Step 2's JSON!).
2. `$.ajax` POST to `http://localhost:8081/customers`, `data: JSON.stringify(formData)`.
3. On success: `fetchCustomers()` and `this.reset()` the form.

**Try it:** add a customer — it appears at the bottom of the list with no page reload. Watch the POST in the Network tab; check the row in the H2 console (<http://localhost:8081/h2-console>).

**Solution:** [customer.html](https://github.com/cnacha-mfu/sample-boot-fullstack/blob/complete/frontend/src/main/resources/static/customer.html)

---

## Step 5: CORS — why the browser lets this happen at all

**File:** `backend/src/main/java/th/mfu/config/CorsConfig.java` (read together)

**Idea:** a page from `localhost:8080` calling `localhost:8081` is a **cross-origin** request — browsers block those unless the *target* server says it's OK. `CorsConfig` is that permission slip (`allowedOriginPatterns("*")`).

**Try it (the best error of the day):** comment out `@Configuration` in `CorsConfig`, restart the backend, reload the page — the console shows the famous *"blocked by CORS policy"* error. Put it back. Every student will hit this error in their career; now they know what it means.

---

## After class

You are now ready for the graded lab: [lab-web-fullstack](https://github.com/maefahluang-uni/lab-web-fullstack) — a **concert booking frontend** (`app.js`) against a provided backend, built with exactly these pieces:

| Lab task | Demo step |
|---|---|
| Task 1 — `loadConcerts()` AJAX GET | Step 3 |
| Task 2 — `displayConcerts()` DOM building from JSON | Step 3 |
| Task 3 — `loadSeats()` / `displaySeats()` | Step 3 (same pattern, nested URL) |
| Task 4 — booking form AJAX POST + refresh | Step 4 |
| Task 5 — event handlers | Steps 3–4 (buttons, submit) |
| "CORS errors" in the lab's help section | Step 5 |

> **Tip:** both apps must run at the same time — use two terminals. If a port is taken, find the old process or reboot the app. In Codespaces the URLs differ; check the **Ports** tab.
