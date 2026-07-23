import axiosInstance from './axiosConfig';

export const processPayment = (data) => 
  axiosInstance.post('/api/v1/payments/process', data);

export const getPaymentById = (id) => 
  axiosInstance.get(`/api/v1/payments/${id}`);

export const getAllPayments = () => 
  axiosInstance.get('/api/v1/payments');

export const getPaymentsByUser = (userId) => 
  axiosInstance.get(`/api/v1/payments/user/${userId}`);

export const getWallet = (userId) => 
  axiosInstance.get(`/api/v1/wallet/${userId}`);

export const addMoneyToWallet = (data) => 
  axiosInstance.post('/api/v1/wallet/add-money', data);

export const generateInvoice = (paymentId) => 
  axiosInstance.post(`/api/v1/invoices/generate/${paymentId}`);

export const getInvoice = (paymentId) => 
  axiosInstance.get(`/api/v1/invoices/${paymentId}`);
