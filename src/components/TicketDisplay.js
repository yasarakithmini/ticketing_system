import React from 'react';

const TicketDisplay = ({ tickets }) => (
  <div>
    <h3>Ticket Availability</h3>
    <ul>
      {tickets.map((ticket) => (
        <li key={ticket.id}>Ticket #{ticket.id}</li>
      ))}
    </ul>
  </div>
);

export default TicketDisplay;
