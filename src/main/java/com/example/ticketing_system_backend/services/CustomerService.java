package com.example.ticketing_system_backend.services;


import com.example.ticketing_system_backend.repository.ReservedTicketPool;
import com.example.ticketing_system_backend.repository.TicketPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final TicketPool ticketPool;
    private final ReservedTicketPool reservedTicketPool;

    @Autowired
    public CustomerService(TicketPool ticketPool, ReservedTicketPool reservedTicketPool) {
        this.ticketPool = ticketPool;
        this.reservedTicketPool = reservedTicketPool;
    }

    /**
     * Reserves a ticket for a customer for 20 seconds.
     *
     * @param ticketId the ID of the ticket to reserve
     * @param customerId the ID of the customer
     * @return true if the reservation was successful, otherwise false
     */
    public boolean reserveTicket(Long ticketId, Long customerId) {
        return reservedTicketPool.reserveTicket(ticketId, customerId);
    }

    /**
     * Completes the ticket purchase if the ticket is still reserved for the customer.
     *
     * @param ticketId the ID of the ticket to complete the purchase for
     * @param customerId the ID of the customer completing the purchase
     * @return true if the purchase was successful, otherwise false
     */
    public boolean completePurchase(Long ticketId, Long customerId) {
        return reservedTicketPool.completePurchase(ticketId, customerId);
    }
}
