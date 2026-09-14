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

## Running in GitHub Codespaces

Same two commands (two terminals). The page derives the backend URL from its
own address, so **no URL is ever edited**. One extra step, once per codespace:

> **Ports tab → right-click port 8081 → Port Visibility → Public.**

Why: the browser calls the backend directly (that is the whole point of the
demo), and Codespaces blocks browser requests to private forwarded ports.
Port 8080 can stay private — you open it yourself. If the customer list stays
empty on Codespaces, port 8081 visibility is the first thing to check
(DevTools → Network → the `customers` request will show a 401).
