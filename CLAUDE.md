# Database Conventions (Strict)

- **Singular Naming Only:** All database table names must be strictly **singular** (e.g., `user`, `order`, `product`).
- **No Plurals:** **Never** append an "s" or "es" to the end of any table name.
- **Execution Scripts:** Whenever providing any DB additions (INSERT/CREATE), updates (UPDATE/ALTER), or deletions (DELETE/DROP), **always provide the raw SQL script** ready to copy-paste and run directly in MySQL Workbench.
- **Database Selection:** Always begin every SQL script with `USE trip10;` before any other statement including `SET FOREIGN_KEY_CHECKS`.
- **Primary Key Naming:** A table's own primary key column must always be named just `id` — never prefix it with the table name (e.g. `trip` table → `id`, not `trip_id`). This applies to the Java entity's `@Id` field too (e.g. `private int id;`, not `private int tripId;`). Foreign key columns on *other* tables that reference this key still use the descriptive `<referenced_table>_id` form (e.g. `trip.driver_id` referencing `driver.id`) — this rule is only about a table's own primary key.
- **Ignore Foreign Keys:** In the generated SQL scripts, wrap the modifications to safely ignore foreign key constraints if they exist by using:

```sql
USE trip10;
SET FOREIGN_KEY_CHECKS = 0;
-- SQL operations here
SET FOREIGN_KEY_CHECKS = 1;
```

# Code Preservation Rules (Strict)

- **Java:** Never modify, remove, refactor, or overwrite any existing Java code that was manually written. Only add new code or extend existing classes/methods when explicitly asked. If a change is needed in an existing method, highlight it and ask before touching it.
- **JavaScript:** Same rule applies — never alter any existing JavaScript/TypeScript logic that was manually written. Only generate new functions, components, or files unless explicitly told to modify existing ones.
- **Default Behavior:** When in doubt, add — never replace or delete existing logic.

# API Endpoint Naming Conventions (Strict)

- **Multi-word path segments:** Always use `snake_case` (underscore) for any endpoint path segment that consists of two or more words.
- **Never use kebab-case (`-`)** in endpoint paths.
- **Examples:**
  - ✅ `/api/content_management`
  - ❌ `/api/content-management`
  - ✅ `/api/user_profile`
  - ❌ `/api/user-profile`
  - ✅ `/api/payment_request`
  - ❌ `/api/payment-request`
- **Applies to:** Spring Boot `@RequestMapping`, `@GetMapping`, `@PostMapping`, etc., Angular service URLs, and any API contract/documentation.
- **Default Behavior:** When generating any new endpoint, always default to `snake_case` for multi-word segments without needing to be reminded.

# Java Naming Conventions (Strict)

- **Never use underscores (`_`) in Java identifiers** — field names, method names, and local variable names must always be `camelCase` (e.g., `phoneNumber`, not `phone_number`).
- This applies regardless of the underlying DB column name — map the camelCase field to a snake_case column explicitly with `@Column(name = "phone_number")` instead of naming the Java field itself with an underscore.
- **Default Behavior:** When generating or fixing any Java field/method, always default to camelCase without needing to be reminded.

# JPA Entity Relationship Rules (Strict)

- **Never use JPA/Hibernate relationship annotations at all** — this includes `@OneToOne`, `@OneToMany`, `@ManyToOne`, and `@ManyToMany`. No exceptions.
- **Always model relationships as plain foreign key columns instead** (e.g., a plain `private int permissionId;` field), matching the existing pattern already used across this codebase (e.g., `Car.docId`, `CarDoc.carId`, `Driver.docId`, `DriverDoc.driverId`).
- If related data is needed, fetch it explicitly through the relevant repository (e.g., `adminPermissionRepo.findById(admin.getPermissionId())`) rather than relying on Hibernate to load an association automatically.
- **Default Behavior:** When generating any new relationship between entities, always default to a plain FK column + explicit repository lookup, never an ORM relationship annotation, without needing to be reminded.

# Production Deployment (Strict — Read Before Any Deploy/Server Work)

Trip10 is deployed on the **same EC2 server as GoBee**, sharing the same nginx instance and the same MySQL instance (different schema). There is no dedicated port or domain for Trip10 — everything rides on GoBee's existing `gobeeeg.com` domain / `13.62.239.189` IP via **path-based nginx routing**, because the server admin will not open additional ports ("one port only for all apps").

## Topology

| Component | Detail |
|---|---|
| Server | Single EC2 instance (Amazon Linux), `ip-172-31-2-220`, public IP `13.62.239.189` |
| Domain | `gobeeeg.com` / `www.gobeeeg.com` → resolves directly to the EC2 IP (no load balancer/CDN in front) |
| nginx config | `/etc/nginx/conf.d/tawsila.conf` — one file, multiple `server {}` blocks, handles both GoBee and Trip10 |
| MySQL | One instance, shared `root`/`123456` credentials (intentionally not hardened yet — explicit user decision, revisit later). GoBee uses schema `gobee`, Trip10 uses schema `trip10`. |
| GoBee backend | `GoBee.jar`, port `8080` (internal only, proxied by nginx) |
| Trip10 backend | `Trip10.jar`, port `8081` (internal only, proxied by nginx) — chosen specifically to avoid clashing with GoBee's 8080 |
| GoBee admin frontend | Static Angular build at `/home/ec2-user/tawsila-app/go-bee-admin-frontend/`, served by nginx directly (no backing process) |
| GoBee main site | Static Angular build at `/home/ec2-user/tawsila-app/browser/` |
| Trip10 admin frontend | Static Angular build at `/home/ec2-user/trip10-app/browser/`, served by nginx directly |

## URL map

- `https://gobeeeg.com/` → GoBee main site
- `https://gobeeeg.com/api/` → GoBee backend (proxied to `localhost:8080`, full path preserved — no prefix stripping)
- `https://gobeeeg.com/admin/` → GoBee admin frontend (static)
- `http://13.62.239.189/trip10-admin/` → Trip10 admin frontend (static) — **no HTTPS/domain for Trip10 yet**
- `http://13.62.239.189/trip10-api/...` → Trip10 backend (nginx strips the `/trip10-api` prefix, then forwards the rest unchanged — e.g. `/trip10-api/api/admin/login` → `localhost:8081/api/admin/login`)

## Frontend build requirements (critical — easy to get wrong)

- **Always build with `--configuration production` explicitly.** `angular.json`'s `production` configuration must have a `fileReplacements` block swapping `environment.ts` → `environment.prod.ts` — without it, `--configuration production` only changes minification/budgets and silently keeps using the dev environment file (this was a real bug that shipped once: the built app kept calling `localhost:8080` from a production server, causing a CORS-looking failure).
- **Always pass `--base-href` matching the nginx path the app is served from** (`/trip10-admin/` for Trip10, `/admin/` for GoBee admin). Skipping this breaks asset loading once deployed under a subpath — assets 404 or resolve against the domain root instead of the subpath.
- Trip10's `environment.prod.ts` `apiUrl` is `/trip10-api` (relative, same-origin) — not an absolute URL, and not empty string.
- Frontend deploys are **static files only** — delete old files in the target folder before uploading new ones (filenames are content-hashed per build, so stale bundles silently accumulate otherwise, as happened with GoBee's admin folder: ~50 old `main.*.js` files before cleanup).

## Deploy commands (see full walkthrough in project chat history if needed)

**Trip10 backend:** `mvn clean package -DskipTests` in `Trip-10-main` → upload jar as `/home/ec2-user/Trip10.jar` → run `./restart-trip10.sh` on server.

**Trip10 frontend:** `ng build --configuration production --base-href /trip10-admin/` in `Trip-10-Admin-Frontend` → wipe and re-upload `/home/ec2-user/trip10-app/browser/` from `dist/trip-10-admin-frontend/browser/`.

**GoBee backend:** same idea, `GoBee.jar`, `./restart.sh` on server.

**GoBee admin frontend:** `ng build --configuration production --base-href /admin/` → wipe and re-upload `/home/ec2-user/tawsila-app/go-bee-admin-frontend/`.

**nginx does NOT need reloading for any of the above.** It only needs `sudo nginx -t && sudo systemctl reload nginx` when `tawsila.conf` itself changes (new location blocks, proxy targets, certs). Routine backend/frontend deploys never touch that file.

## Known gotchas / history (so future sessions don't re-discover these)

- GoBee's `/api/` nginx block originally stripped the `/api/` prefix (`proxy_pass http://localhost:8080/;` with trailing slash) while GoBee's actual Spring controllers are mapped **with** the `/api/` prefix in Java (`@RequestMapping("api/user")` etc.) — this was a genuine, long-standing mismatch, fixed by removing the trailing slash on `proxy_pass` so the full path is forwarded unchanged. If GoBee endpoints ever start 404ing/500ing with `NoResourceFoundException` in `app.log` again, check this first.
- Restart scripts (`restart.sh` for GoBee, `restart-trip10.sh` for Trip10) are scoped by `pgrep -f "<jar-name>.jar"`, so they only ever touch their own app's process — safe to run either one without affecting the other.
- `tawsila.conf` has `0777` permissions (world-writable) — not the cause of any known issue, but unusual and worth tightening eventually; if tightened, WinSCP's plain "Edit" flow on that file will need an elevated/sudo-enabled SFTP session instead.
- GoBee's HTTPS was fully configured (valid Let's Encrypt certs exist at `/etc/letsencrypt/live/gobeeeg.com/`) but nginx wasn't actually listening on 443 after a `reload` — needed a full `sudo systemctl restart nginx` to bind the new socket. If HTTPS ever silently stops working after an nginx config change, check `sudo ss -ltnp | grep :443` and do a full restart, not just reload, if it's empty.
- No cron jobs, systemd timers, git syncs, or deploy agents exist on this server that touch nginx config — confirmed by investigation. If `tawsila.conf` ever reverts unexpectedly again, suspect a stale WinSCP "Edit" temp-file save (e.g. two edit sessions open at once) before assuming automation.
- Production intentionally has **no seeded admin/user accounts** (`data.sql` is skipped via `spring.sql.init.data-locations=optional:classpath:data-none.sql` in `application-prod.properties`) — creating any admin account requires a manual `INSERT` with a properly generated BCrypt hash (see DB Conventions above for script format).
