import './comunes.css'

// Marcador para las vistas que aún no están hechas. Muestra quién la hace y qué falta.
export default function EnConstruccion({ titulo, responsable, tareas = [] }) {
  return (
    <section className="en-construccion">
      <h1>{titulo}</h1>
      <p className="en-construccion__aviso">
        En construcción · la hace <strong>{responsable}</strong>
      </p>
      {tareas.length > 0 && (
        <ul>
          {tareas.map((tarea) => (
            <li key={tarea}>{tarea}</li>
          ))}
        </ul>
      )}
    </section>
  )
}
