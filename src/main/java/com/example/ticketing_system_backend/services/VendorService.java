package com.example.ticketing_system_backend.services;



import com.example.ticketing_system_backend.models.Ticket ;
import com.example.ticketing_system_backend.repository.TicketPool ;
import com.example.ticketing_system_backend.util.LoggerUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class VendorService {
    private final TicketPool ticketPool;

    public VendorService(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    @Async
    public void addTickets(int quantity) {
        while (true) {
            Ticket ticket = new Ticket();
            ticketPool.addTicket(ticket);
            LoggerUtil.log("Vendor added " + ticket);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
