import { Outlet, Route, Routes } from 'react-router-dom'
import Cabecera from './components/Cabecera'
import Inicio from './views/Inicio'
import SeleccionTerminal from './views/SeleccionTerminal'
import VistaTerminal from './views/VistaTerminal'
import VistaCocina from './views/VistaCocina'
import VistaRecogida from './views/VistaRecogida'
import NoEncontrada from './views/NoEncontrada'

// Estructura común: cabecera arriba y, debajo, la vista de la ruta actual (<Outlet />).
function Plantilla() {
  return (
    <>
      <Cabecera />
      <main className="contenedor">
        <Outlet />
      </main>
    </>
  )
}

// Rutas de la aplicación. Cada vista vive en su propio archivo de src/views.
// Para añadir una vista nueva: crear el archivo en src/views y añadir aquí su <Route>.
export default function App() {
  return (
    <Routes>
      <Route element={<Plantilla />}>
        <Route path="/" element={<Inicio />} />
        <Route path="/terminal" element={<SeleccionTerminal />} />
        <Route path="/terminal/:terminalId" element={<VistaTerminal />} />
        <Route path="/cocina" element={<VistaCocina />} />
        <Route path="/recogida" element={<VistaRecogida />} />
        <Route path="*" element={<NoEncontrada />} />
      </Route>
    </Routes>
  )
}
