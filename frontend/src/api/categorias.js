import { pedirApi } from './cliente'

// Categorías de la carta. CategoriaDTO: { id, nombre, productos: string[] }

/** GET /api/categorias */
export function listarCategorias() {
  return pedirApi('/categorias')
}
