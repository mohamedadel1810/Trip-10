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
