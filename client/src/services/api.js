import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const api = axios.create({ baseURL: API_URL });

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) config.headers.Authorization = `Bearer ${token}`;
    if (!(config.data instanceof FormData))
      config.headers['Content-Type'] = 'application/json';
    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/account';
    }
    return Promise.reject(error);
  }
);

export const authAPI = {
  login: (username, password) =>
    api.post('/v1/auth/login', { username, password }),
  register: (username, email, password) =>
    api.post('/v1/auth/register', { username, email, password }),
};

export const userAPI = {
  getCurrentUser: () => api.get('/v1/users/me'),
  getUserByUsername: (username) => api.get(`/v1/users/${username}`),
  getUserById: (id) => api.get(`/v1/users/${id}`),
  updateProfile: (data) => api.put('/v1/users/me', data),
  getAllUsers: () => api.get('/v1/users'),
  deleteUser: (id) => api.delete(`/v1/users/${id}`),
  updateUserRole: (id, role) => api.put(`/v1/users/${id}/role`, { role }),
};

export const socialsAPI = {
  getSocials: () => api.get('/v1/users/me/socials'),
  updateSocials: (data) => api.put('/v1/users/me/socials', data),
};

export const projectsAPI = {
  getMyProjects: () => api.get('/v1/projects/my'),
  getPublishedByUser: (username) =>
    api.get(`/v1/projects/published/${username}`),
  getAllProjects: () => api.get('/v1/projects/all'),
  deleteProject: (id) => api.delete(`/v1/projects/${id}`),
  unpublishProject: (id) => api.put(`/v1/projects/${id}/unpublish`),
};
