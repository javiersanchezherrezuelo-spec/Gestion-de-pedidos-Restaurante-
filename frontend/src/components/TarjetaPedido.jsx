import { useRef, useState } from 'react'
import { cambiarEstado } from '../api/pedidos'
import { infoEstado } from '../config/estados'
import MensajeError from './MensajeError'
import './TarjetaPedido.css'

export default function TarjetaPedido({ pedido, onActualizado, deshabilitado = false }) {
  const [ocupado, setOcupado] = useState(false)
  const [error, setError] = useState(null)
  const enCurso = useRef(false)
  const estado = infoEstado(pedido.estado)
  const fecha = new Date(pedido.fechaCreacionTimestamp)
  const fechaValida = !Number.isNaN(fecha.getTime())

  async function avanzar() {
    if (enCurso.current || deshabilitado || !estado.siguiente) return
    enCurso.current = true
    setOcupado(true)
    setError(null)
    try {
      await cambiarEstado(pedido.id, estado.siguiente)
      onActualizado()
    } catch (e) {
      setError(e)
    } finally {
      enCurso.current = false
      setOcupado(false)
    }
  }

  return (
    <article className="tarjeta-pedido" aria-label={`Pedido ${pedido.codigo}`}>
      <h3>{pedido.codigo}</h3>
      <p>{pedido.terminal || `Terminal ${pedido.terminalId}`}</p>
      <p>Hora: {fechaValida ? <time dateTime={pedido.fechaCreacionTimestamp} title={fecha.toLocaleString('es-ES')}>{fecha.toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' })}</time> : 'No disponible'}</p>
      <ul>{(pedido.items ?? []).map((item) => <li key={item.id ?? item.productoId}>{item.cantidad} × {item.productoNombre}</li>)}</ul>
      <MensajeError error={error} />
      {estado.siguiente && <button type="button" className="boton" disabled={ocupado || deshabilitado} onClick={avanzar}>{ocupado ? 'Actualizando…' : estado.accion}</button>}
    </article>
  )
}
