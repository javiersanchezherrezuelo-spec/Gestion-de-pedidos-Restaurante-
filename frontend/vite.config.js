import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

const backend = process.env.BACKEND_URL ?? 'http://localhost:8080'

const proxy = { '/api': { target: backend, changeOrigin: true } }

export default defineConfig({
  plugins: [react()],
  server: { proxy },
  preview: { proxy },
})
