package com.example.ticketing_system_backend.repository;



import com.example.ticketing_system_backend.models.Ticket ;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class TicketPool {
    private final Queue<Ticket> availableTickets = new ConcurrentLinkedQueue<>();

    public TicketPool() {
        for (int i = 0; i < 100; i++) {  // Set the capacity
            availableTickets.add(new Ticket());
        }
    }

    public Ticket removeTicket() {
        return availableTickets.poll();
    }

    public void addTicket(Ticket ticket) {
        availableTickets.offer(ticket);
    }
}
