# sample-boot-fullstack — AJAX frontend + REST backend demo

In-class live-coding demo: a two-app fullstack setup — a REST backend
(port 8081, H2) and a separate frontend app (port 8080) whose page calls the
backend with jQuery AJAX. Covers `$.ajax` GET/POST, JSON field mapping,
DevTools Network debugging, and CORS.

**➡️ Follow the demonstration flow in [TODO.md](TODO.md).**

- Branch `main` — in-class version with `TODO`s we fill together
- Branch [`complete`](https://github.com/cnacha-mfu/sample-boot-fullstack/tree/complete) — finished code for every step
- Prepares you for the graded lab: [lab-web-fullstack](https://github.com/maefahluang-uni/lab-web-fullstack)

```
mvn spring-boot:run --file backend\pom.xml    # REST API  → http://localhost:8081/customers
mvn spring-boot:run --file frontend\pom.xml   # web page  → http://localhost:8080/customer.html
```
