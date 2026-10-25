
import './TarjetaProducto.css'

export default function TarjetaProducto({
                                            producto,
                                            onAnadir,
                                            deshabilitado = false
                                        }) {
    return (
        <article className="tarjeta-producto">
            {producto.urlFoto && (
                <img
                    className="tarjeta-producto__imagen"
                    src={producto.urlFoto}
                    alt={producto.nombre}
                    loading="lazy"
                />
            )}

            <div className="tarjeta-producto__contenido">
                <h3 className="tarjeta-producto__nombre">
                    {producto.nombre}
                </h3>

                <p className="tarjeta-producto__precio">
                    {Number(producto.precio).toLocaleString('es-ES', {
                        style: 'currency',
                        currency: 'EUR'
                    })}
                </p>

                <button
                    type="button"
                    className="boton"
                    onClick={() => onAnadir(producto)}
                    disabled={deshabilitado}
                >
                    Añadir
                </button>
            </div>
        </article>
    )
}
