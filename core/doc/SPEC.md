# Client Management Specification

This document describes only the implemented functionality related to registered OAuth2 client management.

## 1. Architecture

### 1.1 Relevant Modules

```
io.cred0.core.clients
|- api/
|  |- RegisteredClientController
|  |- RegisteredClientDto
|  \- RegisteredClientsResponse
|- service/
|  \- JpaRegisteredClientRepository
|- persistence/
|  |- RegisteredClientEntity
|  \- JpaRegisteredClientEntityRepository
|- mappers/
|  |- RegisteredClientDtoMapper
|  |- RegisteredClientEntityMapper
|  \- RegisteredClientMapper
\- config/
   \- RegisteredClientRepositoryConfig
```

### 1.2 Runtime Flow

```
HTTP -> RegisteredClientController
     -> JpaRegisteredClientRepository (implements RegisteredClientRepository)
     -> JpaRegisteredClientEntityRepository
     -> oauth2_registered_client table
```

### 1.3 Mapping Boundaries

| Conversion                                        | Mapper                         |
|--------------------------------------------------|--------------------------------|
| `RegisteredClientDto` -> `RegisteredClient`      | `RegisteredClientDtoMapper`    |
| `RegisteredClient` -> `RegisteredClientDto`      | `RegisteredClientDtoMapper`    |
| `RegisteredClient` -> `RegisteredClientEntity`   | `RegisteredClientEntityMapper` |
| `RegisteredClientEntity` -> `RegisteredClient`   | `RegisteredClientEntityMapper` |
| String values <-> OAuth2 security types          | `RegisteredClientMapper`       |

## 2. Domain and Persistence Model

### 2.1 Domain Object

Core domain type: `org.springframework.security.oauth2.server.authorization.client.RegisteredClient`.

### 2.2 API Model

`RegisteredClientDto` fields:

- `id`
- `clientId`
- `clientIdIssuedAt`
- `clientSecret`
- `clientSecretExpiresAt`
- `clientName`
- `clientAuthenticationMethods`
- `authorizationGrantTypes`
- `redirectUris`
- `postLogoutRedirectUris`
- `scopes`
- `clientSettings`
- `tokenSettings`

Mapper rules in `RegisteredClientDtoMapper`:

- Required: `clientId`, `clientName`
- Auto-generated when missing: `id`
- Defaults when missing:
  - `clientIdIssuedAt` -> `Instant.now()`
  - `clientAuthenticationMethods` -> `client_secret_basic`
  - `authorizationGrantTypes` -> `client_credentials`

### 2.3 Persistence Model

Entity: `RegisteredClientEntity`  
Table: `oauth2_registered_client`

| Column                        | Type          | Constraints              |
|------------------------------|---------------|--------------------------|
| `id`                         | VARCHAR(100)  | PK, NOT NULL             |
| `client_id`                  | VARCHAR(200)  | NOT NULL, UNIQUE         |
| `client_id_issued_at`        | TIMESTAMP     | nullable                 |
| `client_secret`              | VARCHAR(500)  | nullable                 |
| `client_secret_expires_at`   | TIMESTAMP     | nullable                 |
| `client_name`                | VARCHAR(200)  | NOT NULL                 |
| `client_authentication_methods` | VARCHAR(1000) | NOT NULL              |
| `authorization_grant_types`  | VARCHAR(1000) | NOT NULL                 |
| `redirect_uris`              | VARCHAR(1000) | nullable                 |
| `post_logout_redirect_uris`  | VARCHAR(1000) | nullable                 |
| `scopes`                     | VARCHAR(1000) | NOT NULL                 |
| `client_settings`            | LOB/TEXT      | NOT NULL                 |
| `token_settings`             | LOB/TEXT      | NOT NULL                 |

Unique constraint: `uk_oauth2_registered_client_client_id` on `client_id`.

### 2.4 Serialization Rules

Implemented in `RegisteredClientEntityMapper`:

- Collections are stored as comma-delimited strings
- `clientSettings` and `tokenSettings` are stored as JSON
- JSON mapper is built with Spring Security modules via `RegisteredClientRepositoryConfig`
- If `tokenSettings` has no `access_token_format`, mapper applies `SELF_CONTAINED`

### 2.5 Repository Adapter

`JpaRegisteredClientEntityRepository`:

```java
interface JpaRegisteredClientEntityRepository extends JpaRepository<RegisteredClientEntity, String> {
    Optional<RegisteredClientEntity> findByClientId(String clientId);
}
```

Used by `JpaRegisteredClientRepository` as the persistence adapter behind the Spring Authorization Server `RegisteredClientRepository` contract.

## 3. Client Management REST API

Base path: `/api/registered-clients`

### 3.1 List

`GET /api/registered-clients`

- `200 OK`
- Response body: `RegisteredClientsResponse`

### 3.2 Get by Internal ID

`GET /api/registered-clients/{id}`

- `200 OK` with `RegisteredClientDto`
- `404 Not Found` if no client exists for `id`

### 3.3 Get by OAuth2 Client ID

`GET /api/registered-clients/by-client-id/{clientId}`

- `200 OK` with `RegisteredClientDto`
- `404 Not Found` if no client exists for `clientId`

### 3.4 Create or Update

`POST /api/registered-clients`

- Request body: `RegisteredClientDto`
- Persists via `JpaRegisteredClientRepository.save`
- Response is produced from re-reading persisted entity by `id`
- `500 Internal Server Error` if re-read fails after save

### 3.5 Delete

`DELETE /api/registered-clients/{id}`

- `204 No Content`

## 4. Security Model

### 4.1 Explicit Security Rules in Code

Implemented in `SecurityConfig`:

- Dedicated filter chain for H2 console with highest order
- H2 console requests are permitted
- CSRF is ignored for H2 console matcher
- Frame options are set to `sameOrigin`

### 4.2 Client Management API Security State

For registered-client management endpoints, no dedicated API-level authorization rules are defined in the `clients` module code.

### 4.3 Authorization Server Runtime Endpoints

The service includes Spring Authorization Server runtime endpoints through framework configuration. This specification focuses only on implemented client-management behavior.

