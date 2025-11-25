import axios from 'axios'

const baseURL = import.meta.env.VITE_API_URL || 'http://localhost:7070'

export const api = axios.create({
  baseURL,
  withCredentials: true,
})

export const setAuthToken = (token) => {
  if (token) api.defaults.headers.common['Authorization'] = `Bearer ${token}`
  else delete api.defaults.headers.common['Authorization']
}
