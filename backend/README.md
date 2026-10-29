# Backend — Gestión de pedidos

API REST para gestionar terminales, categorías, productos y el ciclo de vida de pedidos. La aplicación Spring Boot arranca desde `com.hackaboss.gestionpedidos.GestionDePedidosApplication` (clase `GestionDePedidosApplication`).

## Tecnologías

- Java 21, Spring Boot 3.5.5, Spring Web, Spring Data JPA y Bean Validation.
- MySQL Connector/J, Lombok y SpringDoc OpenAPI 2.8.13.
- Maven Wrapper (`mvnw` / `mvnw.cmd`).

## Requisitos y arranque

Se necesita JDK 21 y MySQL accesible. Desde esta carpeta:

1. Copia `.env.example` como `.env` y configura `SPRING_DATASOURCE_PASSWORD`.
2. Ejecuta `./mvnw spring-boot:run` (Windows: `mvnw.cmd spring-boot:run`). La URL predeterminada crea la base `gestion_pedidos` si el usuario de MySQL tiene permisos.
3. La API queda en `http://localhost:8080`.

`application.properties` importa opcionalmente `.env` en el directorio de trabajo. También acepta las variables de entorno `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` y `SPRING_DATASOURCE_PASSWORD`. La URL predeterminada es `jdbc:mysql://localhost:3306/gestion_pedidos?useSSL=false&createDatabaseIfNotExist=true&serverTimezone=UTC`, el usuario predeterminado es `root` y `SPRING_DATASOURCE_PASSWORD:` tiene un valor alternativo explícitamente vacío. Por tanto, una contraseña no configurada se pasa vacía y MySQL puede rechazar la conexión. `.env` no está incluido en el repositorio.

**Nota de configuración:** el reparto pide que la contraseña no tenga valor por defecto. La configuración actual conserva `${SPRING_DATASOURCE_PASSWORD:}`, cuyo valor alternativo es la cadena vacía; esto no es una contraseña real, pero sí es un fallback definido y no cumple literalmente ese requisito. No se ha cambiado aquí porque esta documentación no modifica la configuración.

La configuración JPA usa `ddl-auto=update` y muestra SQL. No es una configuración de producción ni una estrategia de migraciones.

## Swagger y OpenAPI

Con el servidor iniciado, la interfaz Swagger UI está en [`http://localhost:8080/swagger-ui.html`](http://localhost:8080/swagger-ui.html) y el documento OpenAPI en `http://localhost:8080/v3/api-docs`.

## Vocabulario y estados

- **Terminal**: puesto desde el que se inicia un pedido.
- **Categoría**: agrupación de productos.
- **Producto**: elemento de la carta; puede estar activo o desactivado.
- **Pedido**: compra asociada a una terminal, con código, fecha, estado, total y líneas (`items`).
- **Línea de pedido**: producto, cantidad y precio unitario guardado al añadirlo; el subtotal es precio unitario por cantidad.

Estados válidos: `CREADO → EN_PREPARACION → LISTO → ENTREGADO → FINALIZADO`. El pedido nuevo empieza en `CREADO`; el frontend propone este orden. La API acepta cualquier estado válido en el `PATCH`, sin comprobar que sea el siguiente. Solo se pueden añadir o quitar líneas mientras el estado sea `CREADO`. El código de pedido lo genera el backend con formato `A100`–`A999` (900 posibilidades).

## Formato de respuesta y errores

Las respuestas JSON con cuerpo siguen `ApiRespuesta<T>`:

```json
{"correcta":true,"mensaje":"Pedido encontrado","dato":{},"timestamp":"2026-10-29T12:00:00"}
```

En errores, `correcta` es `false`; `dato` normalmente es `null`, salvo errores de validación donde contiene un mapa campo → mensaje:

```json
{"correcta":false,"mensaje":"Hay datos inválidos en la petición","dato":{"nombre":"no debe estar vacío"},"timestamp":"2026-10-29T12:00:00"}
```

El manejador global usa HTTP 400 para petición/validación/formato incorrecto, 404 para recursos no encontrados, 409 para conflictos de integridad y 500 para errores no controlados. Las operaciones `DELETE` exitosas de recursos y líneas devuelven `204 No Content` sin envoltorio. El borrado de datos de prueba devuelve `ApiRespuesta`.

## Endpoints

En las tablas, `id` representa un identificador numérico. Los cuerpos son JSON; los DTO validan campos obligatorios y restricciones anotadas.

### Terminales — `/api/terminales`

| Método y ruta | Uso |
|---|---|
| `GET /api/terminales` | Listar terminales. |
| `GET /api/terminales/{id}` | Consultar una terminal. |
| `POST /api/terminales` | Crear; cuerpo con `nombre`. |
| `PUT /api/terminales/{id}` | Actualizar; cuerpo con `nombre`. |
| `DELETE /api/terminales/{id}` | Eliminar (204). Puede dar 409 si hay datos relacionados. |

### Categorías — `/api/categorias`

| Método y ruta | Uso |
|---|---|
| `GET /api/categorias` | Listar categorías. |
| `GET /api/categorias/{id}` | Consultar una categoría. |
| `POST /api/categorias` | Crear; cuerpo con `nombre`. |
| `PUT /api/categorias/{id}` | Actualizar; cuerpo con `nombre`. |
| `DELETE /api/categorias/{id}` | Eliminar (204). Puede dar 409 si hay productos relacionados. |

### Productos — `/api/productos`

| Método y ruta | Uso |
|---|---|
| `GET /api/productos` | Listar; filtros opcionales descritos abajo. |
| `GET /api/productos/{id}` | Consultar un producto. |
| `POST /api/productos` | Crear; requiere `nombre`, `precio`, `categoriaId`; producto nuevo activo. |
| `PUT /api/productos/{id}` | Actualizar campos del DTO (`nombre`, `precio`, `categoriaId`, opcionalmente `urlFoto` y `activo`). |
| `PATCH /api/productos/{id}/desactivar` | Marcar como inactivo. |
| `DELETE /api/productos/{id}` | Eliminar físicamente (204); puede dar 409 si existen referencias. |

Filtros y ordenación de `GET /api/productos`:

| Parámetro | Significado |
|---|---|
| `activos=true\|false` | Filtra por estado activo. |
| `categoria={id}` | Filtra por ID de categoría. |
| `ordenacion=nombre\|precio\|categoria` | Campo de ordenación; predeterminado `nombre`. `ord` es un alias compatible. |
| `tipo_ord=ASC\|DESC` | Dirección, predeterminada `ASC`. |

Si se envían `ordenacion` y `ord` con valores distintos la API responde 400. `tipo_ord` y el criterio no válidos también responden 400.

### Pedidos — `/api/pedidos`

| Método y ruta | Uso |
|---|---|
| `GET /api/pedidos` | Listar por fecha de creación ascendente; filtro opcional `estado=CREADO\|EN_PREPARACION\|LISTO\|ENTREGADO\|FINALIZADO`. |
| `GET /api/pedidos/{id}` | Consultar por ID. |
| `GET /api/pedidos/codigo/{codigo}` | Consultar por código (se normaliza a mayúsculas). |
| `POST /api/pedidos` | Crear pedido vacío `CREADO`; cuerpo `{"terminalId":1}`. Código y total inicial los asigna el backend. |
| `POST /api/pedidos/{pedidoId}/productos` | Añadir/ acumular línea; cuerpo `{"productoId":1,"cantidad":1}`. Solo en `CREADO`; cantidad positiva y producto activo. |
| `DELETE /api/pedidos/{pedidoId}/productos/{productoId}` | Quitar toda la línea de ese producto (204); solo en `CREADO`. |
| `PATCH /api/pedidos/{pedidoId}/estado` | Cambiar estado; cuerpo `{"estado":"EN_PREPARACION"}`. Acepta los estados del enum, sin validar secuencia. |

El filtro `estado` no distingue mayúsculas/minúsculas; un valor desconocido responde 400. Al agregar una línea existente se suma su cantidad, y el total se recalcula en el servidor. Los errores comunes incluyen pedido/producto/terminal inexistente (404), datos inválidos, producto inactivo o pedido no editable (400), y referencias en conflicto (409).

### Utilidad de pruebas

| Método y ruta | Uso |
|---|---|
| `DELETE /api/pruebas/datos` | Vacía las tablas de pedidos, líneas, productos, categorías y terminales. Operación destructiva de desarrollo; úsala solo con datos de prueba. |

## Datos para desarrollo

Prepara los datos mediante los endpoints anteriores: crea primero una terminal y una categoría, después productos asociados a esa categoría y finalmente pedidos asociados a la terminal. El endpoint de borrado de pruebas está descrito arriba.

## Comprobaciones automatizadas

Hay pruebas web de controladores en `src/test/java/.../ControladoresWebTest.java`. Para ejecutarlas: `./mvnw test` (Windows: `mvnw.cmd test`). La documentación describe código y configuración inspeccionados; no implica que el backend se haya arrancado, conectado a MySQL o probado interactivamente en esta sesión.
