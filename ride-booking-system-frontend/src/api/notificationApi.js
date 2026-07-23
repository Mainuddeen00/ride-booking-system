import axiosInstance from './axiosConfig';

export const createNotification = (data) => 
  axiosInstance.post('/api/v1/notifications', data);

export const getNotificationById = (id) => 
  axiosInstance.get(`/api/v1/notifications/${id}`);

export const getAllNotifications = () => 
  axiosInstance.get('/api/v1/notifications');

export const getNotificationsByUser = (userId) => 
  axiosInstance.get(`/api/v1/notifications/user/${userId}`);

export const sendNotification = (id) => 
  axiosInstance.put(`/api/v1/notifications/${id}/send`);
