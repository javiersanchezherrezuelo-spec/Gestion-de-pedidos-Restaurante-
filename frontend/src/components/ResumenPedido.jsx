
import './ResumenPedido.css'

export default function ResumenPedido({
                                          pedido,
                                          onEliminarProducto,
                                          onFinalizar,
                                          deshabilitado = false
                                      }) {
    const items = pedido?.items ?? []

    const formatoEuros = (cantidad) =>
        Number(cantidad ?? 0).toLocaleString('es-ES', {
            style: 'currency',
            currency: 'EUR'
        })

    return (
        <section className="resumen-pedido">
            <h2>Resumen del pedido</h2>

            {items.length === 0 ? (
                <p>Aún no has añadido productos.</p>
            ) : (
                <ul className="resumen-pedido__lista">
                    {items.map((item) => (
                        <li
                            key={item.id}
                            className="resumen-pedido__producto"
                        >
                            <div>
                                <strong>{item.productoNombre}</strong>
                                <p>
                                    Cantidad: {item.cantidad}
                                </p>
                                <p>
                                    Subtotal: {formatoEuros(item.subtotal)}
                                </p>
                            </div>

                            <button
                                type="button"
                                className="boton boton--secundario"
                                onClick={() => onEliminarProducto(item.productoId)}
                                disabled={deshabilitado}
                            >
                                Quitar
                            </button>
                        </li>
                    ))}
                </ul>
            )}

            <p className="resumen-pedido__total">
                Total: {formatoEuros(pedido?.total)}
            </p>

            <button
                type="button"
                className="boton"
                onClick={onFinalizar}
                disabled={deshabilitado || items.length === 0}
            >
                Finalizar pedido
            </button>
        </section>
    )
}
