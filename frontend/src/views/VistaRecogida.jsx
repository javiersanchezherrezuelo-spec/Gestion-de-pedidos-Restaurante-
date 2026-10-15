import EnConstruccion from '../components/EnConstruccion'

// Vista /recogida — LA HACE RAFA (bloque 3).
//
// Qué tiene que hacer:
// - listarPedidos(ESTADO_RECOGIDA) con refresco cada INTERVALO_REFRESCO_MS (src/config/estados.js).
// - Mostrar SOLO el código de cada pedido, en grande, en el orden que llega del backend
//   (ya viene ordenado por fecha de creación).
export default function VistaRecogida() {
  return (
    <EnConstruccion
      titulo="Pedidos listos para recoger"
      responsable="Rafa"
      tareas={['Códigos de los pedidos LISTO, en grande', 'Refresco automático cada 5 s']}
    />
  )
}
