package com.example.ticketing_system_backend.repository;

import com.example.ticketing_system_backend.models.Ticket;
import com.example.ticketing_system_backend.util.LoggerUtil;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ReservedTicketPool {

    private final Map<Long, ReservedTicket> reservedTickets = new ConcurrentHashMap<>();

    /**
     * Reserves a ticket for a customer.
     *
     * @param ticketId the ID of the ticket to reserve
     * @param customerId the ID of the customer reserving the ticket
     * @return true if the ticket is successfully reserved, otherwise false
     */
    public boolean reserveTicket(Long ticketId, Long customerId) {
        synchronized (this) {
            if (reservedTickets.containsKey(ticketId)) {
                LoggerUtil.log("Ticket " + ticketId + " is already reserved.");
                return false;
            }
            ReservedTicket reservedTicket = new ReservedTicket(ticketId, customerId, System.currentTimeMillis());
            reservedTickets.put(ticketId, reservedTicket);

            // Schedule a task to release the ticket after 20 seconds if not purchased
            new Thread(() -> {
                try {
                    Thread.sleep(20000); // 20 seconds
                    synchronized (this) {
                        if (reservedTickets.containsKey(ticketId) &&
                                reservedTickets.get(ticketId).getCustomerId().equals(customerId)) {
                            reservedTickets.remove(ticketId);
                            LoggerUtil.log("Reservation expired for ticket " + ticketId);
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();

            LoggerUtil.log("Ticket " + ticketId + " reserved by customer " + customerId);
            return true;
        }
    }

    /**
     * Completes the purchase of a reserved ticket.
     *
     * @param ticketId the ID of the reserved ticket
     * @param customerId the ID of the customer completing the purchase
     * @return true if the purchase was successful, otherwise false
     */
    public boolean completePurchase(Long ticketId, Long customerId) {
        synchronized (this) {
            ReservedTicket reservedTicket = reservedTickets.get(ticketId);
            if (reservedTicket != null && reservedTicket.getCustomerId().equals(customerId)) {
                reservedTickets.remove(ticketId);
                LoggerUtil.log("Ticket " + ticketId + " purchased by customer " + customerId);
                return true;
            } else {
                LoggerUtil.log("Purchase failed for ticket " + ticketId + " by customer " + customerId);
                return false;
            }
        }
    }

    /**
     * Retrieves a reserved ticket by its ID and customer ID.
     *
     * @param ticketId   the ID of the ticket
     * @param customerId the ID of the customer
     * @return an Optional containing the ReservedTicket if found
     */
    public Optional<ReservedTicket> getReservedTicket(Long ticketId, Long customerId) {
        synchronized (this) {
            ReservedTicket reservedTicket = reservedTickets.get(ticketId);
            if (reservedTicket != null && reservedTicket.getCustomerId().equals(customerId)) {
                return Optional.of(reservedTicket);
            }
            return Optional.empty();
        }
    }
    /**
     * Checks for and removes expired reservations.
     */
    public void checkExpiredReservations() {
        long currentTime = System.currentTimeMillis();
        reservedTickets.entrySet().removeIf(entry ->
                currentTime - entry.getValue().getReservationTime() > 20000
        );
    }


    /**
     * Represents a reserved ticket with metadata.
     */
    private static class ReservedTicket {
        private final Long ticketId;
        private final Long customerId;
        private final long reservationTime;

        public ReservedTicket(Long ticketId, Long customerId, long reservationTime) {
            this.ticketId = ticketId;
            this.customerId = customerId;
            this.reservationTime = reservationTime;
        }

        public Long getTicketId() {
            return ticketId;
        }

        public Long getCustomerId() {
            return customerId;
        }

        public long getReservationTime() {
            return reservationTime;
        }
    }
}
