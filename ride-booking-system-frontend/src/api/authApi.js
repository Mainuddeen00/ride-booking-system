import axiosInstance from './axiosConfig';

export const register = (userData) => 
  axiosInstance.post('/api/auth/register', userData);

export const login = (credentials) => 
  axiosInstance.post('/api/auth/login', credentials);

export const logout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
};
