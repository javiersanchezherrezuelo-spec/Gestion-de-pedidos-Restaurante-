import './comunes.css'

// Muestra un error de la API (ApiError de src/api/cliente.js) o cualquier Error.
// - error.message: el "mensaje" que envía el backend.
// - error.detalles: en errores de validación, objeto { campo: mensaje } que se lista debajo.
// - onReintentar (opcional): muestra un botón para volver a intentarlo.
export default function MensajeError({ error, onReintentar }) {
  if (!error) return null

  const detalles =
    error.detalles && typeof error.detalles === 'object' ? Object.entries(error.detalles) : []

  return (
    <div className="mensaje-error" role="alert">
      <p className="mensaje-error__texto">{error.message}</p>
      {detalles.length > 0 && (
        <ul className="mensaje-error__detalles">
          {detalles.map(([campo, mensaje]) => (
            <li key={campo}>
              <strong>{campo}:</strong> {String(mensaje)}
            </li>
          ))}
        </ul>
      )}
      {onReintentar && (
        <button type="button" className="boton boton--secundario" onClick={onReintentar}>
          Reintentar
        </button>
      )}
    </div>
  )
}
