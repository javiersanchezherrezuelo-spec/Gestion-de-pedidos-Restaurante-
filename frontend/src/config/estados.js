// Flujo de estados de un pedido, igual que el enum EstadoPedido del backend:
//   CREADO -> EN_PREPARACION -> LISTO -> ENTREGADO -> FINALIZADO

//   1. añadirlo al enum EstadoPedido del backend,
//   2. añadir aquí su entrada y ajustar "siguiente" del estado anterior,
//   3. si debe verse en cocina, añadirlo a ESTADOS_COCINA.
//
// - etiqueta:  texto que se muestra al usuario
// - color:     color de la insignia (InsigniaEstado)
// - siguiente: estado al que pasa con el botón de acción (null = último)
// - accion:    texto del botón que lo lleva a "siguiente"
export const ESTADOS = {
  CREADO: { etiqueta: 'Creado', color: '#2563eb', siguiente: 'EN_PREPARACION', accion: 'Empezar a preparar' },
  EN_PREPARACION: { etiqueta: 'En preparación', color: '#d97706', siguiente: 'LISTO', accion: 'Marcar como listo' },
  LISTO: { etiqueta: 'Listo para recoger', color: '#16a34a', siguiente: 'ENTREGADO', accion: 'Entregar' },
  ENTREGADO: { etiqueta: 'Entregado', color: '#6b7280', siguiente: 'FINALIZADO', accion: 'Finalizar' },
  FINALIZADO: { etiqueta: 'Finalizado', color: '#374151', siguiente: null, accion: null },
}

// Columnas de la vista Cocina, en este orden.
export const ESTADOS_COCINA = ['CREADO', 'EN_PREPARACION', 'LISTO']

// Estado cuyos pedidos aparecen en la pantalla de recogida.
export const ESTADO_RECOGIDA = 'LISTO'

// Cada cuánto se refrescan Cocina y Recogida (milisegundos).
export const INTERVALO_REFRESCO_MS = 5000

// Devuelve la configuración de un estado, también si el backend envía uno desconocido.
export function infoEstado(estado) {
  return ESTADOS[estado] ?? { etiqueta: estado, color: '#6b7280', siguiente: null, accion: null }
}
