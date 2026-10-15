import { pedirApi } from './cliente'

// Terminales: los puntos de venta desde los que se crean pedidos.
// TerminalDTO: { id, nombre, pedidos: PedidoDTO[] }

/** GET /api/terminales */
export function listarTerminales() {
  return pedirApi('/terminales')
}

/** GET /api/terminales/{id} — 404 si no existe */
export function obtenerTerminal(id) {
  return pedirApi(`/terminales/${id}`)
}
