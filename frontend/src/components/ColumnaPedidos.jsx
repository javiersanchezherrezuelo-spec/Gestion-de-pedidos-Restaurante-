import { infoEstado } from '../config/estados'
import EstadoVacio from './EstadoVacio'
import TarjetaPedido from './TarjetaPedido'
import './ColumnaPedidos.css'

export default function ColumnaPedidos({ estado, pedidos, onActualizado, deshabilitado }) {
  const titulo = estado === 'LISTO' ? 'Listos' : infoEstado(estado).etiqueta
  return (
    <section className="columna-pedidos" aria-label={titulo}>
      <h2>{titulo} ({pedidos.length})</h2>
      {pedidos.length === 0 ? <EstadoVacio titulo="No hay pedidos" /> : pedidos.map((pedido) => <TarjetaPedido key={pedido.id} pedido={pedido} onActualizado={onActualizado} deshabilitado={deshabilitado} />)}
    </section>
  )
}
