import { listarPedidos } from '../api/pedidos'
import { ESTADO_RECOGIDA, INTERVALO_REFRESCO_MS } from '../config/estados'
import useConsulta from '../hooks/useConsulta'
import Cargando from '../components/Cargando'
import MensajeError from '../components/MensajeError'
import EstadoVacio from '../components/EstadoVacio'
import './VistaRecogida.css'

export default function VistaRecogida() {
  const { datos, cargando, error, recargar } = useConsulta(
    () => listarPedidos(ESTADO_RECOGIDA), [], { intervalo: INTERVALO_REFRESCO_MS },
  )
  // El backend devuelve los pedidos ordenados por fecha de creación ascendente.
  return (
    <section className="vista-recogida">
      <h1>Pedidos listos para recoger</h1>
      <p>Actualización automática cada 5 segundos.</p>
      <MensajeError error={error} onReintentar={recargar} />
      {cargando && <Cargando texto="Cargando pedidos…" />}
      {!cargando && !error && (datos?.length ? <ul className="vista-recogida__codigos" aria-label="Códigos listos para recoger" aria-live="polite">{datos.map((pedido) => <li key={pedido.id}>{pedido.codigo}</li>)}</ul> : <EstadoVacio titulo="No hay pedidos listos para recoger" />)}
    </section>
  )
}
