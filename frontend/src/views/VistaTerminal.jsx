import { useState } from 'react'
import { Link, useParams } from 'react-router-dom'

import { obtenerTerminal } from '../api/terminales'
import { listarCategorias } from '../api/categorias'
import { listarProductos } from '../api/productos'
import {
    crearPedido,
    obtenerPedido,
    agregarProducto,
    eliminarProducto,
    cambiarEstado
} from '../api/pedidos'

import useConsulta from '../hooks/useConsulta'
import Cargando from '../components/Cargando'
import MensajeError from '../components/MensajeError'
import EstadoVacio from '../components/EstadoVacio'
import TarjetaProducto from '../components/TarjetaProducto'
import FiltroCategorias from '../components/FiltroCategorias'
import ResumenPedido from '../components/ResumenPedido'

import './VistaTerminal.css'

export default function VistaTerminal() {
    const { terminalId } = useParams()

    const [categoria, setCategoria] = useState('')
    const [pedido, setPedido] = useState(null)
    const [cargandoAccion, setCargandoAccion] = useState(false)
    const [errorAccion, setErrorAccion] = useState(null)
    const [pedidoFinalizado, setPedidoFinalizado] = useState(false)

    const {
        datos: terminal,
        cargando: cargandoTerminal,
        error: errorTerminal
    } = useConsulta(() => obtenerTerminal(terminalId), [terminalId])

    const {
        datos: categorias,
        cargando: cargandoCategorias,
        error: errorCategorias
    } = useConsulta(() => listarCategorias(), [])

    const {
        datos: productos,
        cargando: cargandoProductos,
        error: errorProductos,
        recargar: recargarProductos
    } = useConsulta(
        () => listarProductos({
            activos: true,
            ...(categoria ? { categoria } : {})
        }),
        [categoria]
    )

    async function empezarPedido() {
        setCargandoAccion(true)
        setErrorAccion(null)

        try {
            const nuevoPedido = await crearPedido(terminalId)
            const pedidoActualizado = await obtenerPedido(nuevoPedido.id)

            setPedido(pedidoActualizado)
            setPedidoFinalizado(false)
        } catch (error) {
            setErrorAccion(error)
        } finally {
            setCargandoAccion(false)
        }
    }

    async function anadirProducto(producto) {
        if (!pedido || pedidoFinalizado) return

        setCargandoAccion(true)
        setErrorAccion(null)

        try {
            await agregarProducto(pedido.id, producto.id, 1)
            const pedidoActualizado = await obtenerPedido(pedido.id)
            setPedido(pedidoActualizado)
        } catch (error) {
            setErrorAccion(error)
        } finally {
            setCargandoAccion(false)
        }
    }

    async function quitarProducto(productoId) {
        if (!pedido || pedidoFinalizado) return

        setCargandoAccion(true)
        setErrorAccion(null)

        try {
            await eliminarProducto(pedido.id, productoId)
            const pedidoActualizado = await obtenerPedido(pedido.id)
            setPedido(pedidoActualizado)
        } catch (error) {
            setErrorAccion(error)
        } finally {
            setCargandoAccion(false)
        }
    }

    async function finalizarPedido() {
        if (!pedido || pedido.items.length === 0) return

        setCargandoAccion(true)
        setErrorAccion(null)

        try {
            await cambiarEstado(pedido.id, 'EN_PREPARACION')
            const pedidoActualizado = await obtenerPedido(pedido.id)
            setPedido(pedidoActualizado)
            setPedidoFinalizado(true)
        } catch (error) {
            setErrorAccion(error)
        } finally {
            setCargandoAccion(false)
        }
    }

    if (cargandoTerminal) {
        return <Cargando texto="Cargando terminal..." />
    }

    if (errorTerminal) {
        return (
            <section className="vista-terminal">
                <MensajeError error={errorTerminal} />
                <p>
                    <Link to="/terminal">Volver a elegir terminal</Link>
                </p>
            </section>
        )
    }

    if (!terminal) {
        return (
            <section className="vista-terminal">
                <EstadoVacio titulo="No se encontró la terminal">
                    <Link to="/terminal">Volver a elegir terminal</Link>
                </EstadoVacio>
            </section>
        )
    }

    return (
        <section className="vista-terminal">
            <header className="vista-terminal__cabecera">
                <div>
                    <Link to="/terminal">← Cambiar terminal</Link>
                    <h1>{terminal.nombre}</h1>
                    <p>Selecciona los productos para tu pedido.</p>
                </div>

                {!pedido && (
                    <button
                        type="button"
                        className="boton"
                        onClick={empezarPedido}
                        disabled={cargandoAccion}
                    >
                        Empezar pedido
                    </button>
                )}
            </header>

            {errorAccion && (
                <MensajeError
                    error={errorAccion}
                    onReintentar={() => setErrorAccion(null)}
                />
            )}

            {pedidoFinalizado && pedido && (
                <div className="vista-terminal__confirmacion">
                    <h2>¡Pedido finalizado!</h2>
                    <p>Tu código de pedido es:</p>
                    <strong className="vista-terminal__codigo">
                        {pedido.codigo}
                    </strong>
                    <p>
                        Total: {Number(pedido.total ?? 0).toLocaleString('es-ES', {
                        style: 'currency',
                        currency: 'EUR'
                    })}
                    </p>
                    <button
                        type="button"
                        className="boton"
                        onClick={empezarPedido}
                        disabled={cargandoAccion}
                    >
                        Nuevo pedido
                    </button>
                </div>
            )}

            {!pedidoFinalizado && (
                <>
                    <section className="vista-terminal__carta">
                        <h2>Carta</h2>

                        {cargandoCategorias ? (
                            <Cargando texto="Cargando categorías..." />
                        ) : errorCategorias ? (
                            <MensajeError error={errorCategorias} />
                        ) : (
                            <FiltroCategorias
                                categorias={categorias ?? []}
                                categoriaSeleccionada={categoria}
                                onCambiar={setCategoria}
                            />
                        )}

                        {cargandoProductos ? (
                            <Cargando texto="Cargando productos..." />
                        ) : errorProductos ? (
                            <MensajeError
                                error={errorProductos}
                                onReintentar={recargarProductos}
                            />
                        ) : !productos || productos.length === 0 ? (
                            <EstadoVacio titulo="No hay productos disponibles">
                                Prueba con otra categoría.
                            </EstadoVacio>
                        ) : (
                            <div className="vista-terminal__productos">
                                {productos.map((producto) => (
                                    <TarjetaProducto
                                        key={producto.id}
                                        producto={producto}
                                        onAnadir={anadirProducto}
                                        deshabilitado={!pedido || cargandoAccion}
                                    />
                                ))}
                            </div>
                        )}
                    </section>

                    {pedido ? (
                        <ResumenPedido
                            pedido={pedido}
                            onEliminarProducto={quitarProducto}
                            onFinalizar={finalizarPedido}
                            deshabilitado={cargandoAccion}
                        />
                    ) : (
                        <p className="vista-terminal__aviso">
                            Primero pulsa «Empezar pedido» para poder añadir productos.
                        </p>
                    )}
                </>
            )}
        </section>
    )
}
