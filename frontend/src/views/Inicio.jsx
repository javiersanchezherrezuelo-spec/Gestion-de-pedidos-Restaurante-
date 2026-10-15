import { Link } from 'react-router-dom'
import { listarTerminales } from '../api/terminales'
import useConsulta from '../hooks/useConsulta'
import Cargando from '../components/Cargando'
import MensajeError from '../components/MensajeError'
import './Inicio.css'

// Pantalla de entrada: accesos a las tres vistas y comprobación de que el backend responde.
const VISTAS = [
  { ruta: '/terminal', titulo: 'Terminal', texto: 'Crear pedidos: elegir productos y obtener el código.' },
  { ruta: '/cocina', titulo: 'Cocina', texto: 'Ver los pedidos pendientes y cambiar su estado.' },
  { ruta: '/recogida', titulo: 'Recogida', texto: 'Pantalla con los códigos de los pedidos listos.' },
]

export default function Inicio() {
  // Se usa GET /api/terminales como prueba de conexión con el backend.
  const { datos: terminales, cargando, error, recargar } = useConsulta(() => listarTerminales(), [])

  return (
    <section className="inicio">
      <h1>Gestión de pedidos</h1>
      <p className="inicio__intro">Elige la vista según el puesto: terminal, cocina o pantalla de recogida.</p>

      <div className="inicio__vistas">
        {VISTAS.map((vista) => (
          <Link key={vista.ruta} to={vista.ruta} className="inicio__vista">
            <h2>{vista.titulo}</h2>
            <p>{vista.texto}</p>
          </Link>
        ))}
      </div>

      <div className="inicio__conexion">
        {cargando && <Cargando texto="Comprobando conexión con el backend…" />}
        {error && <MensajeError error={error} onReintentar={recargar} />}
        {!cargando && !error && (
          <p className="inicio__ok">
            Conectado con el backend · {terminales.length}{' '}
            {terminales.length === 1 ? 'terminal disponible' : 'terminales disponibles'}
          </p>
        )}
      </div>
    </section>
  )
}
