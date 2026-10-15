import EnConstruccion from '../components/EnConstruccion'

// Vista /terminal — LA HACE MAYRA (bloque 2).
//
// Qué tiene que hacer:
// - Cargar las terminales con useConsulta(() => listarTerminales(), []) (src/api/terminales.js).
// - Mostrar un botón por terminal que navegue a /terminal/:id (useNavigate o <Link>).
// - Si no hay terminales, EstadoVacio indicando que se creen con la colección de Postman.
// - Mientras carga, <Cargando />; si falla, <MensajeError error={error} onReintentar={recargar} />.
export default function SeleccionTerminal() {
  return (
    <EnConstruccion
      titulo="Elegir terminal"
      responsable="Mayra"
      tareas={[
        'Listar terminales (GET /api/terminales)',
        'Botón por terminal que lleve a /terminal/:id',
      ]}
    />
  )
}
