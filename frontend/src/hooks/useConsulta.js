import { useCallback, useEffect, useState } from 'react'

/**
 * Carga datos de la API y gestiona los estados de carga y error.
 *
 *   const { datos, cargando, error, recargar } = useConsulta(() => listarTerminales(), [])
 *
 * @param {() => Promise<any>} funcion  función que llama a la API (las de src/api)
 * @param {Array} dependencias  cuando cambia alguna, se vuelve a pedir (como en useEffect)
 * @param {{intervalo?: number}} opciones  si se indica, se vuelve a pedir cada "intervalo" ms
 * @returns {{datos: any, cargando: boolean, error: Error|null, recargar: () => void}}
 *
 * - "cargando" solo es true en la primera carga, para que el refresco automático no haga parpadear la vista.
 * - Si la vista se desmonta o cambian las dependencias, se ignoran las respuestas que lleguen tarde.
 */
export default function useConsulta(funcion, dependencias = [], { intervalo } = {}) {
  const [datos, setDatos] = useState(null)
  const [cargando, setCargando] = useState(true)
  const [error, setError] = useState(null)
  // Al incrementarlo, el useEffect se vuelve a ejecutar: así funciona recargar().
  const [version, setVersion] = useState(0)

  useEffect(() => {
    let activo = true

    const cargar = () => {
      funcion()
        .then((resultado) => {
          if (!activo) return
          setDatos(resultado)
          setError(null)
        })
        .catch((e) => {
          if (activo) setError(e)
        })
        .finally(() => {
          if (activo) setCargando(false)
        })
    }

    cargar()
    const temporizador = intervalo ? setInterval(cargar, intervalo) : null

    // Limpieza: al desmontar o antes de volver a ejecutar el efecto.
    return () => {
      activo = false
      if (temporizador) clearInterval(temporizador)
    }
    // "funcion" se crea en cada render, por eso se depende de "dependencias" y no de ella.
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [...dependencias, version, intervalo])

  const recargar = useCallback(() => setVersion((v) => v + 1), [])

  return { datos, cargando, error, recargar }
}
