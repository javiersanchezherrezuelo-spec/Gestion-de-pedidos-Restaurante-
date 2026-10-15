# Proyecto II — Frontend de Gestión de pedidos

Frontend en React + JavaScript que consume la API REST del backend (Proyecto I, carpeta `../backend`).
Es la interfaz de los tres puestos del restaurante: **terminal**, **cocina** y **pantalla de recogida**.
No contiene lógica de negocio: todo (códigos, totales, estados) lo decide el backend y el frontend lo muestra.

> Estado: base del proyecto hecha (bloque 1). Las vistas Terminal (Mayra) y Cocina/Recogida (Rafa)
> están marcadas como «En construcción». Los apartados con *(pendiente)* los completa quien hace esa parte.

## Vistas implementadas

| Ruta | Vista | Estado |
|---|---|---|
| `/` | Inicio: accesos a las vistas y comprobación de conexión con el backend | Hecha |
| `/terminal` | Elegir terminal | *(pendiente — Mayra)* |
| `/terminal/:terminalId` | Terminal: crear pedido, añadir/quitar productos, total y código | *(pendiente — Mayra)* |
| `/cocina` | Cocina: pedidos CREADO, EN_PREPARACION y LISTO, cambio de estado | *(pendiente — Rafa)* |
| `/recogida` | Pantalla de recogida: códigos de los pedidos LISTO | *(pendiente — Rafa)* |

## Endpoints consumidos

Todas las funciones están en `src/api/` y ya están implementadas.

| Función | Endpoint |
|---|---|
| `listarTerminales()` / `obtenerTerminal(id)` | `GET /api/terminales` / `GET /api/terminales/{id}` |
| `listarCategorias()` | `GET /api/categorias` |
| `listarProductos({ activos, categoria, ordenacion, tipoOrden })` | `GET /api/productos?activos=&categoria=&ordenacion=&tipo_ord=` |
| `listarPedidos(estado)` | `GET /api/pedidos?estado=` |
| `obtenerPedido(id)` / `obtenerPedidoPorCodigo(codigo)` | `GET /api/pedidos/{id}` / `GET /api/pedidos/codigo/{codigo}` |
| `crearPedido(terminalId)` | `POST /api/pedidos` |
| `agregarProducto(pedidoId, productoId, cantidad)` | `POST /api/pedidos/{pedidoId}/productos` |
| `eliminarProducto(pedidoId, productoId)` | `DELETE /api/pedidos/{pedidoId}/productos/{productoId}` |
| `cambiarEstado(pedidoId, estado)` | `PATCH /api/pedidos/{pedidoId}/estado` |

## Flujo de estados

Mismo flujo que el enum `EstadoPedido` del backend, configurado en `src/config/estados.js`:

```
CREADO → EN_PREPARACION → LISTO → ENTREGADO → FINALIZADO
```

## Flujo de usuario

*(pendiente — Mayra y Rafa: terminal → cocina → recogida)*

## Tecnologías utilizadas

- React 19 + Vite
- React Router (`react-router-dom`) para las rutas
- `fetch` para las llamadas a la API
- `useState` / `useEffect` (encapsulados en el hook `useConsulta`)
- CSS propio con variables, sin librerías de UI
- oxlint para revisar el código

## Estructura del proyecto

```
src/
├── main.jsx              # arranque: BrowserRouter + App
├── App.jsx               # rutas y plantilla común (cabecera + vista)
├── index.css             # variables de diseño y estilos base
├── api/                  # comunicación con el backend
│   ├── cliente.js        # pedirApi() y ApiError: único sitio con fetch
│   ├── terminales.js · categorias.js · productos.js · pedidos.js
├── config/
│   └── estados.js        # flujo de estados: etiquetas, colores, estado siguiente
├── hooks/
│   └── useConsulta.js    # carga de datos con estados de carga/error y refresco opcional
├── components/           # piezas reutilizables
│   ├── Cabecera · MensajeError · Cargando · EstadoVacio · InsigniaEstado · EnConstruccion
└── views/                # una vista por ruta
    ├── Inicio · SeleccionTerminal · VistaTerminal · VistaCocina · VistaRecogida · NoEncontrada
```

**Cómo ampliarlo**
- Nuevo estado: añadirlo al enum del backend y a `src/config/estados.js` (y a `ESTADOS_COCINA` si va en cocina).
- Nueva vista: crear el archivo en `src/views` y añadir su `<Route>` en `App.jsx` y su enlace en `Cabecera`.

## Manejo de errores

- `pedirApi()` (`src/api/cliente.js`) devuelve el campo `dato` de `ApiRespuesta` si todo va bien.
- Si el backend responde con error, lanza `ApiError` con el `mensaje` del backend, el código HTTP (`estado`)
  y, en errores de validación, los errores por campo (`detalles`).
- Si el backend no responde, el mensaje es «No se puede conectar con el servidor. ¿Está arrancado el backend?».
- El componente `MensajeError` muestra el mensaje, la lista de errores por campo y, opcionalmente, un botón «Reintentar».

## Cómo ejecutar la aplicación

Requisitos: Node 20+, Java 21, MySQL.

1. **Base de datos**: arrancar MySQL (por ejemplo `brew services start mysql`).
2. **Backend** (`../backend`):
   - Copiar `.env.example` a `.env` y poner la contraseña de MySQL en `SPRING_DATASOURCE_PASSWORD`.
   - Arrancar `GestionDePedidosApplication` desde IntelliJ o con `./mvnw spring-boot:run`. Queda en `http://localhost:8080`.
   - Cargar datos de prueba con la colección `Testers Postman_collection.json`.
3. **Frontend** (esta carpeta):
   ```bash
   npm install
   npm run dev
   ```
   Abrir `http://localhost:5173`.

El proxy de Vite (`vite.config.js`) reenvía `/api` a `http://localhost:8080`, así que no hace falta CORS.
Si el backend está en otra dirección: `BACKEND_URL=http://otra-direccion:8080 npm run dev`.

Otros comandos: `npm run build` (versión de producción en `dist/`), `npm run preview` (sirve esa versión), `npm run lint`.

## Suposiciones y limitaciones

*(pendiente — Rafa)*

## Posibles mejoras

*(pendiente — Rafa)*
