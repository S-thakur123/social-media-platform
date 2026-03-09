import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      // Any request starting with "/api" will be sent to the Gateway
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
      // Proxying WebSockets for your Chat Service
      '/ws-chat': {
        target: 'http://localhost:8080',
        ws: true,
        changeOrigin: true,
      }
    }
  }
})