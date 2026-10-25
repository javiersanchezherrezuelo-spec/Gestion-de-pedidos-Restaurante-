
import { useNavigate } from 'react-router-dom'
import { listarTerminales } from '../api/terminales'
import useConsulta from '../hooks/useConsulta'
import Cargando from '../components/Cargando'
import MensajeError from '../components/MensajeError'
import EstadoVacio from '../components/EstadoVacio'
import './SeleccionTerminal.css'

export default function SeleccionTerminal() {
    const navegar = useNavigate()
    const { datos: terminales, cargando, error, recargar } =
        useConsulta(() => listarTerminales(), [])

    if (cargando) {
        return <Cargando texto="Cargando terminales..." />
    }

    if (error) {
        return <MensajeError error={error} onReintentar={recargar} />
    }

    if (!terminales || terminales.length === 0) {
        return (
            <section className="seleccion-terminal">
                <h1>Elegir terminal</h1>
                <EstadoVacio titulo="No hay terminales disponibles">
                    Crea una terminal utilizando la colección de Postman y vuelve a intentarlo.
                </EstadoVacio>
                <button
                    type="button"
                    className="boton boton--secundario"
                    onClick={recargar}
                >
                    Volver a comprobar
                </button>
            </section>
        )
    }

    return (
        <section className="seleccion-terminal">
            <h1>Elegir terminal</h1>
            <p className="seleccion-terminal__descripcion">
                Selecciona la terminal desde la que quieres gestionar un pedido.
            </p>

            <div className="seleccion-terminal__lista">
                {terminales.map((terminal) => (
                    <button
                        key={terminal.id}
                        type="button"
                        className="seleccion-terminal__boton"
                        onClick={() => navegar(`/terminal/${terminal.id}`)}
                    >
            <span className="seleccion-terminal__nombre">
              {terminal.nombre}
            </span>
                        <span className="seleccion-terminal__flecha" aria-hidden="true">
              →
            </span>
                    </button>
                ))}
            </div>
        </section>
    )
}
