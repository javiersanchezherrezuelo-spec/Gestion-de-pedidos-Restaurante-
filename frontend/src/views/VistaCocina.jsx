import EnConstruccion from '../components/EnConstruccion'

// Vista /cocina — LA HACE RAFA (bloque 3).
//
// Qué tiene que hacer:
// - Una columna por cada estado de ESTADOS_COCINA (src/config/estados.js).
// - Cargar los pedidos de cada estado con listarPedidos(estado), refrescando cada
//   INTERVALO_REFRESCO_MS con useConsulta(..., [], { intervalo: INTERVALO_REFRESCO_MS }).
// - Cada tarjeta: código, terminal, hora (fechaCreacionTimestamp) y "cantidad × productoNombre".
// - Botón con infoEstado(estado).accion que llama a cambiarEstado(id, infoEstado(estado).siguiente)
//   y después recargar().
// - En CREADO, ocultar los pedidos sin productos (items vacío).
// Componentes previstos: TarjetaPedido, ColumnaPedidos (en src/components).
export default function VistaCocina() {
  return (
    <EnConstruccion
      titulo="Cocina"
      responsable="Rafa"
      tareas={[
        'Columnas Creado, En preparación y Listos',
        'Botón para pasar al estado siguiente',
        'Refresco automático cada 5 s',
      ]}
    />
  )
}
