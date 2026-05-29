# Descripción del proyecto `auth-backend-api`

## Archivos principales

### `pom.xml`
- Configura Maven y las dependencias.
- Incluye:
  - Spring Boot Web
  - Spring Data JPA
  - PostgreSQL
  - Swagger/OpenAPI
  - Lombok
  - Pruebas

### `mvnw`, `mvnw.cmd`
- Wrappers de Maven.
- Permiten usar Maven sin instalarlo en el sistema.

---

## Carpetas y su función

### `src/main/java/com/juanfedevmaster/authbackendapi`

#### `AuthBackendApiApplication.java`
- Clase principal.
- Arranca la aplicación Spring Boot.

### `src/main/java/com/juanfedevmaster/authbackendapi/controller`

#### `AuthController.java`
- Controla las rutas HTTP.
- Define:
  - `POST /api/auth/register`
  - `POST /api/auth/login`
- Recibe los datos del cliente y llama al servicio.

### `src/main/java/com/juanfedevmaster/authbackendapi/services`

#### `AuthService.java`
- Contiene la lógica de autenticación.
- `register(...)`
  - Valida que no existan username, email o cédula duplicados.
  - Crea y guarda un usuario nuevo.
- `login(...)`
  - Verifica que el usuario exista.
  - Comprueba la contraseña.

### `src/main/java/com/juanfedevmaster/authbackendapi/repository`

#### `UserRepository.java`
- Habla con la base de datos.
- Ofrece métodos para buscar usuarios por:
  - `cedula`
  - `username`
- También verifica si ya existe:
  - `username`
  - `email`
  - `cedula`

### `src/main/java/com/juanfedevmaster/authbackendapi/entity`

#### `User.java`
- Representa la tabla `users`.
- Campos:
  - `cedula` (ID)
  - `username`
  - `email`
  - `password`
  - `enabled`

### `src/main/java/com/juanfedevmaster/authbackendapi/entity/dto`

#### `AuthRequest.java`
- Objeto para login.
- Tiene:
  - `username`
  - `password`

#### `RegisterUserRequest.java`
- Objeto para registro.
- Tiene:
  - `cedula`
  - `username`
  - `email`
  - `password`

### `src/main/resources`

#### `application.properties`
- Configura la app:
  - nombre
  - puerto (`9090`)
  - conexión a PostgreSQL
  - ajustes de JPA/Hibernate

### `src/test/java/.../AuthBackendApiApplicationTests.java`
- Prueba básica.
- Verifica que la aplicación arranca.

---

## Cumplimiento SOLID (explicación simple)

- `S` (Responsabilidad única)
  - Sí, cada clase hace una sola cosa.
- `O` (Abierto/Cerrado)
  - Parcial: la estructura ayuda, pero no es muy fácil agregar nuevas formas de login sin cambiar código.
- `L` (Sustitución de Liskov)
  - No hay mucha herencia, así que no hay problema claro.
- `I` (Segregación de interfaces)
  - Sí, el repositorio solo trae lo que necesita.
- `D` (Inversión de dependencias)
  - Sí: las clases reciben sus dependencias, no las crean ellas mismas.

---

## Resumen final

- Es un backend de autenticación.
- Está bien organizado en carpetas de:
  - controladores
  - servicios
  - repositorios
  - entidades
  - DTOs
- La estructura es clara y fácil de entender.
```