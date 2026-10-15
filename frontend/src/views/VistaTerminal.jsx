import { useParams } from 'react-router-dom'
import EnConstruccion from '../components/EnConstruccion'

// Vista /terminal/:terminalId — LA HACE MAYRA (bloque 2).
//
// Qué tiene que hacer (todo con funciones de src/api, sin cálculos propios):
// - Nombre de la terminal: obtenerTerminal(terminalId); si da 404, error y enlace a /terminal.
// - Carta: listarCategorias() para el filtro y listarProductos({ activos: true, categoria }).
// - «Empezar pedido»: crearPedido(terminalId). Antes de eso, «Añadir» desactivado.
// - «Añadir» / «+»: agregarProducto(pedido.id, productoId, 1) y después obtenerPedido(pedido.id)
//   para pintar las líneas y el total que calcula el backend.
// - «Quitar»: eliminarProducto(pedido.id, productoId) y volver a cargar el pedido.
// - Bloquear botones mientras hay una petición en curso; errores con <MensajeError />.
// - «Finalizar pedido»: mostrar pedido.codigo en grande y el total; «Nuevo pedido» empieza otro.
// Componentes previstos: TarjetaProducto, FiltroCategorias, ResumenPedido (en src/components).
export default function VistaTerminal() {
  const { terminalId } = useParams()
  return (
    <EnConstruccion
      titulo={`Terminal ${terminalId}`}
      responsable="Mayra"
      tareas={[
        'Carta con filtro por categoría',
        'Crear pedido, añadir y quitar productos',
        'Total del backend y código al finalizar',
      ]}
    />
  )
}
