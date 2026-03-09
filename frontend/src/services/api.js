import axios from 'axios';

// Create a custom instance
const api = axios.create({
  // Since we set up the Vite Proxy, we use '/api' as the base.
  // Vite will catch this and forward it to http://localhost:8080.
  baseURL: '/api',
  timeout: 10000, // 10 seconds timeout
  headers: {
    'Content-Type': 'application/json',
  },
});

// 🛡️ Request Interceptor (Perfect for JWT)
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// 🚨 Response Interceptor (Global Error Handling)
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Handle Unauthorized (e.g., redirect to login)
      console.error("Unauthorized! Redirecting...");
    }
    return Promise.reject(error);
  }
);

export default api;