import './comunes.css'

// Mensaje para listas vacías (sin terminales, sin pedidos, etc.).
export default function EstadoVacio({ titulo, children }) {
  return (
    <div className="estado-vacio">
      <p className="estado-vacio__titulo">{titulo}</p>
      {children && <div className="estado-vacio__texto">{children}</div>}
    </div>
  )
}
