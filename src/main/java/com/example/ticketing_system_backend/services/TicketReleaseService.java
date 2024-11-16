package com.example.ticketing_system_backend.services;



import com.example.ticketing_system_backend.models.Ticket;
import com.example.ticketing_system_backend.repository.ReservedTicketPool ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TicketReleaseService {
    private final ReservedTicketPool reservedTicketPool;

    @Autowired
    public TicketReleaseService(ReservedTicketPool reservedTicketPool) {
        this.reservedTicketPool = reservedTicketPool;
    }

    @Async
    public void releaseExpiredReservations() {
        while (true) {
            reservedTicketPool.checkExpiredReservations();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

