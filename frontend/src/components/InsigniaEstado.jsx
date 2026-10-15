import { infoEstado } from '../config/estados'
import './comunes.css'

// Etiqueta de color con el estado de un pedido. Texto y color salen de config/estados.js.
export default function InsigniaEstado({ estado }) {
  const { etiqueta, color } = infoEstado(estado)
  return (
    <span className="insignia" style={{ '--color-insignia': color }}>
      {etiqueta}
    </span>
  )
}
