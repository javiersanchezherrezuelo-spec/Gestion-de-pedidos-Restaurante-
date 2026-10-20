
// El backend envuelve todas las respuestas en ApiRespuesta:
//   { correcta, mensaje, dato, timestamp }
// pedirApi() devuelve directamente "dato" si todo va bien, o lanza un ApiError con el "mensaje" del backend si algo falla, para que las vistas solo tengan que mostrarlo.

const BASE_URL = '/api' // el proxy de Vite lo reenvía al backend (vite.config.js)

const MENSAJE_SIN_CONEXION = 'No se puede conectar con el servidor. ¿Está arrancado el backend?'

export class ApiError extends Error {
  constructor(mensaje, estado = 0, detalles = null) {
    super(mensaje)
    this.name = 'ApiError'
    this.estado = estado
    this.detalles = detalles
  }
}

// Hace una petición a la API. "ruta" va sin el prefijo /api 
// Devuelve el campo "dato" de la respuesta, o null si no hay cuerpo.
export async function pedirApi(ruta, { metodo = 'GET', cuerpo } = {}) {
  const opciones = { method: metodo, headers: { Accept: 'application/json' } }
  if (cuerpo !== undefined) {
    opciones.headers['Content-Type'] = 'application/json'
    opciones.body = JSON.stringify(cuerpo)
  }

  let respuesta
  try {
    respuesta = await fetch(BASE_URL + ruta, opciones)
  } catch {
    // fetch solo falla así cuando no hay red o no responde nadie.
    throw new ApiError(MENSAJE_SIN_CONEXION)
  }

  // 204 No Content: borrados correctos, no hay cuerpo que leer.
  if (respuesta.status === 204) return null

  let json = null
  try {
    json = await respuesta.json()
  } catch {
    
  }

  if (json === null) {
    if (respuesta.status >= 500) throw new ApiError(MENSAJE_SIN_CONEXION, respuesta.status)
    if (!respuesta.ok) throw new ApiError(`Error ${respuesta.status}`, respuesta.status)
    return null
  }

  if (!respuesta.ok || json.correcta === false) {
    throw new ApiError(json.mensaje ?? `Error ${respuesta.status}`, respuesta.status, json.dato ?? null)
  }

  return json.dato
}
