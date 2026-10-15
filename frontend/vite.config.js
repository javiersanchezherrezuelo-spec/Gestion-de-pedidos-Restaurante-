import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

// Dirección del backend (Proyecto I). Se puede cambiar con la variable BACKEND_URL.
const backend = process.env.BACKEND_URL ?? 'http://localhost:8080'

// El proxy reenvía al backend todas las peticiones que empiezan por /api.
// Así el navegador cree que frontend y API están en el mismo origen y no hace falta CORS.
// Se configura igual para "npm run dev" (server) y para "npm run preview" (preview).
const proxy = { '/api': { target: backend, changeOrigin: true } }

export default defineConfig({
  plugins: [react()],
  server: { proxy },
  preview: { proxy },
})
