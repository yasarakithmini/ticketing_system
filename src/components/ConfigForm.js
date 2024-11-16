import React, { useState } from 'react';
import axios from 'axios';

const ConfigForm = () => {
  const [config, setConfig] = useState({
    totalTickets: 0,
    ticketReleaseRate: 0,
    customerRetrievalRate: 0,
    maxTicketCapacity: 0,
  });

  const handleChange = (e) => {
    setConfig({ ...config, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post('/api/config', config);
      alert('Configuration saved successfully!');
    } catch (error) {
      console.error('Error saving configuration:', error);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h3>Configuration</h3>
      <input
        type="number"
        name="totalTickets"
        placeholder="Total Tickets"
        onChange={handleChange}
      />
      <input
        type="number"
        name="ticketReleaseRate"
        placeholder="Ticket Release Rate"
        onChange={handleChange}
      />
      <input
        type="number"
        name="customerRetrievalRate"
        placeholder="Customer Retrieval Rate"
        onChange={handleChange}
      />
      <input
        type="number"
        name="maxTicketCapacity"
        placeholder="Max Ticket Capacity"
        onChange={handleChange}
      />
      <button type="submit">Save Config</button>
    </form>
  );
};

export default ConfigForm;
