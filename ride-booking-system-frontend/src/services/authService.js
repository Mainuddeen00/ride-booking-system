import { login, register, logout } from '../api/authApi';

export const authService = {
  login: async (credentials) => {
    const response = await login(credentials);
    if (response.data.token) {
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('user', JSON.stringify(response.data.user));
    }
    return response.data;
  },
  register: async (userData) => {
    const response = await register(userData);
    return response.data;
  },
  logout: () => {
    logout();
  },
  getCurrentUser: () => {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  },
  isAuthenticated: () => {
    return !!localStorage.getItem('token');
  }
};
