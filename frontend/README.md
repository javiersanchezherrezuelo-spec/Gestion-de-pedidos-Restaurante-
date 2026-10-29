# Frontend — Gestión de pedidos

Interfaz React para los puestos de terminal, cocina y recogida. Consume el backend de `../backend`; este conserva la autoridad sobre códigos, precios, totales y estados. El vocabulario y los estados corresponden al [README del backend](../backend/README.md).

## Índice

- [Tecnologías](#tecnologías)
- [Vistas y rutas](#vistas-y-rutas)
- [Flujo de estados](#flujo-de-estados)
- [API consumida](#api-consumida)
- [Manejo de errores y consultas](#manejo-de-errores-y-consultas)
- [Estructura](#estructura)
- [Ejecución local](#ejecución-local)
- [Suposiciones, limitaciones y mejoras](#suposiciones-limitaciones-y-mejoras)

## Tecnologías

React 19, Vite, React Router, `fetch`, hooks de React y CSS propio. `oxlint` revisa el código.

## Vistas y rutas

| Ruta | Vista | Comportamiento |
|---|---|---|
| `/` | Inicio | Enlaces a las vistas y comprobación de conexión. |
| `/terminal` | Selección de terminal | Lista terminales y permite elegir una. |
| `/terminal/:terminalId` | Terminal | Carga carta activa con filtro de categoría; crea pedido, añade una unidad, quita una línea, muestra total/código y avanza el pedido a `EN_PREPARACION`. |
| `/cocina` | Cocina | Tres columnas para `CREADO`, `EN_PREPARACION` y `LISTO`; tarjetas con código, terminal, hora y líneas; botón para avanzar al estado siguiente; refresco cada 5 s. Los pedidos vacíos se ocultan en `CREADO`. |
| `/recogida` | Recogida | Muestra los códigos grandes de pedidos `LISTO`, en el orden de creación del backend; refresco cada 5 s. |
| `*` | No encontrada | Ruta para URL desconocida. |

Flujo disponible en la interfaz: la terminal crea `CREADO` (la columna Cocina lo oculta mientras no tenga productos); «Finalizar pedido» lo cambia directamente a `EN_PREPARACION`; Cocina lo avanza a `LISTO`. La tarjeta de `LISTO` en Cocina ofrece «Entregar» y cambia el estado a `ENTREGADO`. Recogida es una pantalla pasiva: muestra los códigos de pedidos `LISTO` y no contiene una acción de entrega. `FINALIZADO` existe en backend y configuración, pero no se muestra en Cocina ni Recogida y la interfaz no lo alcanza.

## Flujo de estados

El backend define `CREADO → EN_PREPARACION → LISTO → ENTREGADO → FINALIZADO`. La terminal crea el pedido en `CREADO` y, al pulsar «Finalizar pedido», lo cambia directamente a `EN_PREPARACION`. Cocina muestra pedidos `CREADO`, `EN_PREPARACION` y `LISTO`, con acciones «Empezar a preparar», «Marcar como listo» y «Entregar»; esta última acción está en la tarjeta de la columna `LISTO` de Cocina. Recogida muestra los códigos `LISTO` sin controles de cambio de estado. La API permite saltar entre cualesquiera estados válidos, de modo que el orden depende de la interfaz o del cliente que la consuma.

## API consumida

Las funciones se encuentran en `src/api/`; todas usan `pedirApi()`.

| Funciones | Endpoints |
|---|---|
| `listarTerminales`, `obtenerTerminal` | `GET /api/terminales`, `GET /api/terminales/{id}` |
| `listarCategorias` | `GET /api/categorias` |
| `listarProductos` | `GET /api/productos?activos=&categoria=&ordenacion=&tipo_ord=` (filtros opcionales) |
| `listarPedidos` | `GET /api/pedidos?estado=` (estado opcional) |
| `obtenerPedido`, `obtenerPedidoPorCodigo` | `GET /api/pedidos/{id}`, `GET /api/pedidos/codigo/{codigo}` |
| `crearPedido` | `POST /api/pedidos` |
| `agregarProducto` | `POST /api/pedidos/{pedidoId}/productos` |
| `eliminarProducto` | `DELETE /api/pedidos/{pedidoId}/productos/{productoId}` |
| `cambiarEstado` | `PATCH /api/pedidos/{pedidoId}/estado` |

Las respuestas con cuerpo siguen `ApiRespuesta` (`correcta`, `mensaje`, `dato`, `timestamp`); los borrados exitosos son `204 No Content`. Consulta el README backend para cuerpos, filtros y semántica completa.

## Manejo de errores y consultas

`pedirApi()` desempaqueta `dato` en respuestas correctas y lanza `ApiError` con el mensaje, estado HTTP y, para validación, detalles de `dato` del backend. Si falla `fetch`, muestra «No se puede conectar con el servidor. ¿Está arrancado el backend?». `MensajeError` presenta errores y puede ofrecer reintento. `useConsulta` gestiona carga, datos y error, permite recargar y admite refresco periódico. Cocina y Recogida consultan cada 5 segundos. En Cocina se conservan visualmente los datos previos si una actualización falla, se avisa que pueden estar desactualizados y se bloquean acciones; Recogida muestra el error y no presenta los códigos mientras hay error.

La terminal mantiene el pedido y su estado de finalización en memoria de la vista. Si se recarga la página, no recupera automáticamente un pedido en curso mediante su ID. Quitar un producto borra la línea completa, aunque tenga cantidad mayor que uno. Los precios y totales mostrados se vuelven a consultar al backend.

## Estructura

```text
src/
├── api/          # cliente y funciones por recurso
├── components/   # componentes comunes y de terminal
├── config/       # etiquetas, transiciones y constantes de estado
├── hooks/        # useConsulta
└── views/        # una vista por ruta
```

## Ejecución local

Requisitos: Node.js `^20.19.0` o `>=22.12.0` (engines de Vite/Oxlint en `package-lock.json`), JDK 21 y MySQL. Primero arranca el backend según [`../backend/README.md`](../backend/README.md): crea `backend/.env` desde `.env.example` y configura la contraseña; inicia con `./mvnw spring-boot:run` (Windows `mvnw.cmd spring-boot:run`).

En esta carpeta:

```bash
npm install
npm run dev
```

Abre `http://localhost:5173`. El proxy de Vite reenvía `/api` a `http://localhost:8080`; `BACKEND_URL` permite cambiar el destino en desarrollo. También hay `npm run build`, `npm run preview` y `npm run lint`.

Prepara terminales, categorías y productos mediante los endpoints documentados en el README del backend antes de crear pedidos.

## Suposiciones, limitaciones y mejoras

- Cocina y Recogida se refrescan por sondeo cada 5 segundos; no hay canal en tiempo real y una pestaña o red inactiva puede retrasar la actualización.
- Los estados se pueden saltar a través de la API; el cliente debe restringir las acciones para mantener el proceso del restaurante.
- No se conserva una cesta/pedido en el almacenamiento del navegador y no se reanuda tras recarga.
- Mejoras posibles: añadir actualización en tiempo real, persistir y recuperar pedidos en curso, y automatizar el recorrido completo y la comprobación visual adaptable.

Para ejecutar las comprobaciones del frontend: `npm run lint` y `npm run build`. La configuración de MySQL y el arranque del backend se describen en su README.
