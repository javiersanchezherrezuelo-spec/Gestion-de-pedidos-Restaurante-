import { useCallback, useEffect, useState } from 'react'

// Carga datos de la API y gestiona los estados de carga y error.
//   const { datos, cargando, error, recargar } = useConsulta(() => listarTerminales(), [])
// - Vuelve a pedir los datos cuando cambia algo de "dependencias" o al llamar a recargar().
// - Con { intervalo: 5000 } los vuelve a pedir solo cada 5 segundos.
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
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [...dependencias, version, intervalo])

  const recargar = useCallback(() => setVersion((v) => v + 1), [])

  return { datos, cargando, error, recargar }
}
