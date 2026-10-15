import { Link } from 'react-router-dom'
import EstadoVacio from '../components/EstadoVacio'

// Se muestra para cualquier ruta que no exista (ruta "*" en App.jsx).
export default function NoEncontrada() {
  return (
    <EstadoVacio titulo="Página no encontrada">
      <Link to="/">Volver al inicio</Link>
    </EstadoVacio>
  )
}
