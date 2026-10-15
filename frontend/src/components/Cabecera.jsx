import { NavLink } from 'react-router-dom'
import './Cabecera.css'

// Barra superior con enlaces a las vistas. NavLink añade la clase "active" al enlace de la ruta actual.
const ENLACES = [
  { ruta: '/terminal', texto: 'Terminal' },
  { ruta: '/cocina', texto: 'Cocina' },
  { ruta: '/recogida', texto: 'Recogida' },
]

export default function Cabecera() {
  return (
    <header className="cabecera">
      <NavLink to="/" className="cabecera__marca">
        Gestión de pedidos
      </NavLink>
      <nav className="cabecera__nav">
        {ENLACES.map((enlace) => (
          <NavLink key={enlace.ruta} to={enlace.ruta} className="cabecera__enlace">
            {enlace.texto}
          </NavLink>
        ))}
      </nav>
    </header>
  )
}
