import axios from 'axios';

const API = axios.create({
  baseURL: 'http://localhost:8080',
});


export const fetchTickets = () => API.get('/api/tickets');
export const startSystem = () => API.post('/api/tickets/start');
export const stopSystem = () => API.post('/api/tickets/stop');
export const updateConfig = (config) => API.post('/api/config', config);

export default API;
