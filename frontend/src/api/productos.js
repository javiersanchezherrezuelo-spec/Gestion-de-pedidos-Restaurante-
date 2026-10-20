import { pedirApi } from './cliente'

// Productos de la carta.
// ProductoDTO: { id, nombre, precio, urlFoto, activo, categoriaId, categoria }

// GET /api/productos con filtros opcionales. El filtrado y la ordenación los hace el backend.
export function listarProductos({ activos, categoria, ordenacion, tipoOrden } = {}) {
  const parametros = new URLSearchParams()
  if (activos !== undefined) parametros.set('activos', activos)
  if (categoria) parametros.set('categoria', categoria)
  if (ordenacion) parametros.set('ordenacion', ordenacion)
  if (tipoOrden) parametros.set('tipo_ord', tipoOrden)
  const consulta = parametros.toString()
  return pedirApi(`/productos${consulta ? `?${consulta}` : ''}`)
}

// GET /api/productos/{id}
export function obtenerProducto(id) {
  return pedirApi(`/productos/${id}`)
}
