import axiosInstance from './axiosConfig';

export const createDriver = (data) => 
  axiosInstance.post('/api/v1/drivers', data);

export const getDriverById = (id) => 
  axiosInstance.get(`/api/v1/drivers/${id}`);

export const getAllDrivers = () => 
  axiosInstance.get('/api/v1/drivers');

export const updateDriver = (id, data) => 
  axiosInstance.put(`/api/v1/drivers/${id}`, data);

export const updateDriverAvailability = (id, status) => 
  axiosInstance.patch(`/api/v1/drivers/${id}/availability?status=${status}`);
