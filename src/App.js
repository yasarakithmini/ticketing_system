import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { FaPlay, FaStop } from 'react-icons/fa';
import TicketDisplay from './components/TicketDisplay';
import Logs from './components/Logs';
import ConfigForm from './components/ConfigForm';
import './App.css';

const App = () => {
  const [tickets, setTickets] = useState([]);
  const [logs, setLogs] = useState([]);
  const [systemRunning, setSystemRunning] = useState(false);

  // Fetch tickets
  useEffect(() => {
    if (systemRunning) {
      const interval = setInterval(fetchTickets, 2000); // Polling every 2 seconds
      return () => clearInterval(interval);
    }
  }, [systemRunning]);

  const fetchTickets = async () => {
    try {
      const { data } = await axios.get('/api/tickets');
      setTickets(data);
    } catch (error) {
      console.error('Error fetching tickets:', error);
    }
  };

  const startSystem = async () => {
    try {
      await axios.post('/api/start');
      setSystemRunning(true);
      toast.success('System started!');
    } catch (error) {
      toast.error('Error starting system.');
    }
  };

  const stopSystem = async () => {
    try {
      await axios.post('/api/stop');
      setSystemRunning(false);
      toast.success('System stopped!');
    } catch (error) {
      toast.error('Error stopping system.');
    }
  };

  return (
    <div>
      <ToastContainer />
      <h1>Real-Time Ticketing System</h1>
      <ConfigForm />
      <div>
        <button onClick={startSystem} disabled={systemRunning}>
          <FaPlay /> Start
        </button>
        <button onClick={stopSystem} disabled={!systemRunning}>
          <FaStop /> Stop
        </button>
      </div>
      <TicketDisplay tickets={tickets} />
      <Logs logs={logs} />
    </div>
  );
};

export default App;
