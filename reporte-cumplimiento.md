# Reporte de Cumplimiento — Gestion-de-Usuarios
**Fecha:** 2026-06-01  
**Proyecto:** App web full-stack con autenticación segura y gestión de cuentas por roles

---

## 1. Autenticación

| Requisito | Estado | Evidencia | Observación |
|---|---|---|---|
| Registro con nombre, correo y contraseña | ⚠️ Parcial | `RegisterUserRequest.java:11-22` / `RegistrationService.java` | Registra `username`+`cedula`+`email`+`password`. El campo es `username`, no `name`. Falta campo `name` real. |
| Login con **correo** y contraseña | ❌ Falta | `AuthRequest.java:7` | Login usa `username`, no `email`. El requisito pide email. |
| Protección de rutas según rol | ❌ Falta | — | No existe `SecurityConfig`, no hay JWT, no hay middleware. `requireAuth()` referenciada en `admin.html` pero `main.js` está **vacío** (0 bytes). |
| Cierre de sesión | ⚠️ Parcial | `admin.html:22` `onclick="logout()"` | `logout()` está referenciada pero no definida en ningún archivo JS. |

---

## 2. Dashboard del usuario

| Requisito | Estado | Evidencia | Observación |
|---|---|---|---|
| Mensaje de bienvenida con nombre | ❌ Falta | — | No existe `dashboard.html`. Solo existe `index.html` (login) y `admin.html`. |
| Tarjetas: fecha registro, último acceso, estado | ❌ Falta | — | Archivo inexistente. Además, `User.java` no tiene campos `createdAt` ni `lastLogin`. |
| Info de cuenta: nombre, correo, rol | ❌ Falta | — | Archivo inexistente. `User.java` tampoco tiene campo `role`. |
| Botón "Profile" en navbar con modal de edición | ❌ Falta | — | Archivo inexistente. |

---

## 3. Gestión del perfil (rol: usuario)

| Requisito | Estado | Evidencia | Observación |
|---|---|---|---|
| Editar nombre y correo desde modal | ❌ Falta | — | No hay endpoint PUT/PATCH en el backend ni pantalla frontend. |
| Cambiar contraseña (opcional) | ❌ Falta | — | Ningún endpoint de cambio de contraseña existe. |
| Dar de baja propia cuenta con confirmación | ❌ Falta | — | No hay endpoint DELETE para auto-eliminación ni UI. |

---

## 4. Panel del admin

| Requisito | Estado | Evidencia | Observación |
|---|---|---|---|
| Lista completa de usuarios | ⚠️ Parcial | `admin.html:117-145` | Tabla existe pero con **datos hardcodeados** (Ana, Juan, María). No consume API. |
| Buscar/filtrar por nombre o correo | ✅ Cumple | `admin.html:153` `filterUsers()` + script | Filtro funcional sobre DOM (no API). |
| Crear usuario asignando rol | ⚠️ Parcial | `admin.html:171-213` | Modal UI completo, pero el botón "Guardar" no tiene lógica conectada a API. |
| Editar info y rol de cualquier usuario | ⚠️ Parcial | `admin.html:script` `openEditUser()` | Modal se abre y puebla, pero sin persistencia ni API. |
| Eliminar con confirmación (no self-delete) | ✅ Cumple | `admin.html:script` `confirmDelete()` | Confirmación implementada, self-delete bloqueado. Solo elimina del DOM, no de BD. |

---

## Plan de Trabajo

### Prioridad Alta

| # | Tarea | Archivos | Esfuerzo | Depende de |
|---|---|---|---|---|
| 1 | Agregar campo `role`, `createdAt`, `lastLogin` al modelo `User` | `User.java`, `RegisterUserRequest.java` | 1h | — |
| 2 | Implementar JWT: generar token en login, incluirlo en `AuthResponse` | `AuthResponse.java`, `JwtUtil.java` (nuevo), `SecurityConfig.java` (nuevo) | 3h | — |
| 3 | Middleware de protección de rutas por rol (Spring Security) | `SecurityConfig.java`, `JwtFilter.java` (nuevo) | 2h | #2 |
| 4 | Crear `main.js` con `requireAuth()`, `getUsername()`, `getEmail()`, `logout()` | `src/main.js` | 1h | #2 |
| 5 | Cambiar login para aceptar `email` en lugar de `username` | `AuthRequest.java`, `AuthenticationService.java` | 1h | — |
| 6 | Crear `register.html` (nombre, email, contraseña) | `register.html` (nuevo) | 1h | #4 |
| 7 | Crear `dashboard.html` para rol usuario (bienvenida, tarjetas, info cuenta) | `dashboard.html` (nuevo) | 2h | #4, #1 |

### Prioridad Media

| # | Tarea | Archivos | Esfuerzo | Depende de |
|---|---|---|---|---|
| 8 | Endpoint `GET /api/users` (admin) — lista usuarios reales | `UserController.java` (nuevo), `UserService.java` (nuevo) | 1h | #3 |
| 9 | Endpoint `POST /api/users` (admin — crear usuario con rol) | `UserController.java` | 0.5h | #8 |
| 10 | Endpoint `PUT /api/users/{id}` (editar nombre, email, rol) | `UserController.java` | 1h | #8 |
| 11 | Endpoint `DELETE /api/users/{id}` (admin, con guard self-delete) | `UserController.java` | 0.5h | #8 |
| 12 | Conectar `admin.html` a la API real (reemplazar datos hardcodeados) | `admin.html` | 2h | #8–#11 |

### Prioridad Baja

| # | Tarea | Archivos | Esfuerzo | Depende de |
|---|---|---|---|---|
| 13 | Endpoint `PUT /api/users/me` — editar propio perfil | `UserController.java` | 1h | #3 |
| 14 | Endpoint `PUT /api/users/me/password` — cambiar contraseña | `UserController.java` | 0.5h | #13 |
| 15 | Endpoint `DELETE /api/users/me` — dar de baja propia cuenta | `UserController.java` | 0.5h | #13 |
| 16 | Modal de perfil en `dashboard.html` (edición + baja + cambio de contraseña) | `dashboard.html` | 2h | #13–#15 |

---

### Orden sugerido de implementación

```
1 → 5 → 2 → 3 → 4 → 6 → 7 → 8 → 9 → 10 → 11 → 12 → 13 → 14 → 15 → 16
```

**Esfuerzo total estimado: ~20–22 horas**

---

### Resumen ejecutivo

El proyecto tiene una base funcional de registro/login en el backend (BCrypt, validaciones, Postgres) y una UI de admin con buena estructura visual, pero le faltan piezas críticas:

- **`main.js` está vacío** — ninguna función JS del frontend existe realmente.
- **No hay JWT ni sesión real** — el backend autentica pero no retorna token.
- **No existe `dashboard.html`** — la página principal del usuario referenciada en el redirect.
- **El modelo `User` no tiene `role`** — imposible implementar control de acceso por rol.
- **Todos los datos del panel admin son hardcodeados** — frontend y backend están prácticamente desconectados.
