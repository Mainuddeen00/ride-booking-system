import axiosInstance from './axiosConfig';

export const getUserProfile = (userId) => 
  axiosInstance.get(`/api/v1/users/${userId}`);

export const updateUserProfile = (userId, data) => 
  axiosInstance.put(`/api/v1/users/${userId}`, data);

export const getAllUsers = () => 
  axiosInstance.get('/api/v1/users');
