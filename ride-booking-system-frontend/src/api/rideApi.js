import axiosInstance from './axiosConfig';

export const requestRide = (data) => 
  axiosInstance.post('/api/rides/request', data);

export const acceptRide = (id) => 
  axiosInstance.post(`/api/rides/${id}/accept`);

export const startRide = (id) => 
  axiosInstance.post(`/api/rides/${id}/start`);

export const completeRide = (id) => 
  axiosInstance.post(`/api/rides/${id}/complete`);

export const cancelRide = (id) => 
  axiosInstance.post(`/api/rides/${id}/cancel`);

export const getRideById = (id) => 
  axiosInstance.get(`/api/rides/${id}`);

export const getRidesByRider = (riderId) => 
  axiosInstance.get(`/api/rides/rider/${riderId}`);
