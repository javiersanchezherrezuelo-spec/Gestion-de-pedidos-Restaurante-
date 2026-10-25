import { pedirApi } from './cliente'

// Pedidos y sus líneas.
// PedidoDTO: { id, codigo, estado, fechaCreacionTimestamp, total, terminalId, terminal, items }
// PedidoProductoDTO (cada línea de "items"):
//   { id, pedidoId, productoId, productoNombre, cantidad, precioUnitario, subtotal }

export function listarPedidos(estado) {
  const consulta = estado ? `?estado=${encodeURIComponent(estado)}` : ''
  return pedirApi(`/pedidos${consulta}`)
}

// GET /api/pedidos/{id}
export function obtenerPedido(id) {
  return pedirApi(`/pedidos/${id}`)
}

// GET /api/pedidos/codigo/{codigo}
export function obtenerPedidoPorCodigo(codigo) {
  return pedirApi(`/pedidos/codigo/${encodeURIComponent(codigo)}`)
}

// POST /api/pedidos — crea un pedido vacío en estado CREADO; el backend genera el código
export function crearPedido(terminalId) {
  return pedirApi('/pedidos', { metodo: 'POST', cuerpo: { terminalId } })
}

// POST /api/pedidos/{pedidoId}/productos
// Si el producto ya está en el pedido, el backend suma la cantidad.
// Solo funciona con el pedido en CREADO (si no, 400).
export function agregarProducto(pedidoId, productoId, cantidad = 1) {
  return pedirApi(`/pedidos/${pedidoId}/productos`, {
    metodo: 'POST',
    cuerpo: { productoId, cantidad },
  })
}

// DELETE /api/pedidos/{pedidoId}/productos/{productoId} — quita la línea entera
export function eliminarProducto(pedidoId, productoId) {
  return pedirApi(`/pedidos/${pedidoId}/productos/${productoId}`, { metodo: 'DELETE' })
}

// PATCH /api/pedidos/{pedidoId}/estado — el backend no valida el orden de los estados
export function cambiarEstado(pedidoId, estado) {
  return pedirApi(`/pedidos/${pedidoId}/estado`, { metodo: 'PATCH', cuerpo: { estado } })
}