
export default function FiltroCategorias({
                                             categorias,
                                             categoriaSeleccionada,
                                             onCambiar
                                         }) {
    return (
        <div className="filtro-categorias">
            <label htmlFor="filtro-categoria">
                Filtrar por categoría:
            </label>

            <select
                id="filtro-categoria"
                value={categoriaSeleccionada}
                onChange={(evento) => onCambiar(evento.target.value)}
            >
                <option value="">Todas las categorías</option>

                {categorias.map((categoria) => (
                    <option key={categoria.id} value={categoria.id}>
                        {categoria.nombre}
                    </option>
                ))}
            </select>
        </div>
    )
}
