import { listarPedidos } from '../api/pedidos'
import { ESTADOS_COCINA, INTERVALO_REFRESCO_MS } from '../config/estados'
import useConsulta from '../hooks/useConsulta'
import Cargando from '../components/Cargando'
import MensajeError from '../components/MensajeError'
import ColumnaPedidos from '../components/ColumnaPedidos'
import './VistaCocina.css'

export default function VistaCocina() {
  const { datos, cargando, error, recargar } = useConsulta(
    () => Promise.all(ESTADOS_COCINA.map((estado) => listarPedidos(estado))),
    [], { intervalo: INTERVALO_REFRESCO_MS },
  )

  return (
    <section className="vista-cocina">
      <h1>Cocina</h1>
      <p>Actualización automática cada 5 segundos.</p>
      <MensajeError error={error} onReintentar={recargar} />
      {cargando && <Cargando texto="Cargando pedidos…" />}
      {datos && <>
        {error && <p role="status">Los pedidos mostrados pueden estar desactualizados.</p>}
        <div className="vista-cocina__columnas">
          {ESTADOS_COCINA.map((estado, indice) => <ColumnaPedidos key={estado} estado={estado} pedidos={(datos[indice] ?? []).filter((pedido) => estado !== 'CREADO' || pedido.items?.length > 0)} onActualizado={recargar} deshabilitado={Boolean(error)} />)}
        </div>
      </>}
    </section>
  )
}
