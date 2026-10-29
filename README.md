# Gestion-de-pedidos-Restaurante-
Gestion de pedidos Restaurante 

Aplicación web desarrollada con React para gestionar los pedidos de un restaurante.

## Funcionalidades

- Selección de terminal.
- Visualización de productos disponibles.
- Filtrado de productos por categoría.
- Creación y gestión de pedidos.
- Consulta del resumen y del total del pedido.
- Finalización del pedido mediante un código de referencia.

## Tecnologías utilizadas

- React
- JavaScript
- HTML
- CSS
- API REST para la comunicación con el backend

## Vista Terminal

La vista Terminal permite al cliente seleccionar una terminal del restaurante y realizar un pedido.

### Flujo de uso

1. El cliente accede a `/terminal` y consulta las terminales disponibles.
2. Selecciona una terminal y accede a `/terminal/:terminalId`.
3. Consulta la carta y puede filtrar los productos por categoría.
4. Solo se muestran los productos activos.
5. Pulsa «Empezar pedido» para crear un nuevo pedido.
6. Puede añadir productos de uno en uno y quitar líneas del pedido.
7. El resumen muestra las cantidades, los subtotales y el total devuelto por el backend.
8. Pulsa «Finalizar pedido» para cambiar el estado a `EN_PREPARACION`.
9. Se muestra el código del pedido y su total. Con «Nuevo pedido» puede iniciar otro.

### Endpoints utilizados

| Método | Endpoint | Finalidad |
|---|---|---|
| GET | `/api/terminales` | Listar terminales |
| GET | `/api/terminales/{id}` | Consultar una terminal |
| GET | `/api/categorias` | Listar categorías |
| GET | `/api/productos?activos=true` | Listar productos activos |
| POST | `/api/pedidos` | Crear un pedido |
| GET | `/api/pedidos/{id}` | Consultar el pedido y actualizar el total |
| POST | `/api/pedidos/{id}/productos` | Añadir un producto |
| DELETE | `/api/pedidos/{id}/productos/{productoId}` | Quitar un producto |
| PATCH | `/api/pedidos/{id}/estado` | Cambiar el estado del pedido |

Los errores devueltos por la API se muestran en pantalla. Durante las peticiones, los botones relacionados con las acciones se deshabilitan para evitar operaciones simultáneas.

La vista utiliza la capa API y los componentes compartidos del proyecto sin modificarlos.

## Documentación completa

- [Backend](backend/README.md)
- [Frontend, Cocina y Recogida](frontend/README.md)
