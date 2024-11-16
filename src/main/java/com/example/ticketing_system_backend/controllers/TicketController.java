package com.example.ticketing_system_backend.controllers;

import com.example.ticketing_system_backend.services.CustomerService;
import com.example.ticketing_system_backend.services.VendorService;
import com.example.ticketing_system_backend.repository.ReservedTicketPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final CustomerService customerService;
    private final VendorService vendorService;
    private final ReservedTicketPool reservedTicketPool;
    private boolean systemRunning = false;

    @Autowired
    public TicketController(CustomerService customerService, VendorService vendorService, ReservedTicketPool reservedTicketPool) {
        this.customerService = customerService;
        this.vendorService = vendorService;
        this.reservedTicketPool = reservedTicketPool;
    }

    /**
     * Endpoint for customers to reserve a ticket for 20 seconds.
     */
    @PostMapping("/reserve")
    public ResponseEntity<String> reserveTicket(@RequestParam Long ticketId, @RequestParam Long customerId) {
        if (!systemRunning) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("System is currently stopped");
        }

        boolean reserved = customerService.reserveTicket(ticketId, customerId);
        return reserved ? ResponseEntity.ok("Ticket reserved for 20 seconds") :
                ResponseEntity.status(HttpStatus.CONFLICT).body("Ticket unavailable or already reserved");
    }

    /**
     * Endpoint for customers to complete a ticket purchase.
     */
    @PostMapping("/purchase")
    public ResponseEntity<String> completePurchase(@RequestParam Long ticketId, @RequestParam Long customerId) {
        if (!systemRunning) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("System is currently stopped");
        }

        boolean success = customerService.completePurchase(ticketId, customerId);
        return success ? ResponseEntity.ok("Purchase successful") :
                ResponseEntity.status(HttpStatus.CONFLICT).body("Purchase failed or reservation expired");
    }

    /**
     * Endpoint for vendors to add tickets to the available ticket pool.
     */
    @PostMapping("/add")
    public ResponseEntity<String> addTickets(@RequestParam int quantity) {
        if (!systemRunning) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("System is currently stopped");
        }

        vendorService.addTickets(quantity);
        return ResponseEntity.ok(quantity + " tickets added successfully");
    }

    /**
     * Scheduled task to check and expire reservations that exceed the 20-second limit.
     * This task runs every 5 seconds to clear expired reservations.
     */
    @Scheduled(fixedRate = 5000)
    public void checkExpiredReservations() {
        if (systemRunning) {
            reservedTicketPool.checkExpiredReservations();
        }
    }

    /**
     * Manual endpoint for testing reservation expiry check (optional).
     */
    @GetMapping("/check-expired")
    public ResponseEntity<String> manuallyCheckExpiredReservations() {
        reservedTicketPool.checkExpiredReservations();
        return ResponseEntity.ok("Checked expired reservations manually");
    }

    /**
     * Start the ticketing system (enables operations).
     */
    @PostMapping("/start")
    public ResponseEntity<String> startSystem() {
        if (systemRunning) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("System already started");
        }

        systemRunning = true;
        return ResponseEntity.ok("Ticketing system started");
    }

    /**
     * Stop the ticketing system (disables operations).
     */
    @PostMapping("/stop")
    public ResponseEntity<String> stopSystem() {
        if (!systemRunning) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("System already stopped");
        }

        systemRunning = false;
        return ResponseEntity.ok("Ticketing system stopped");
    }
}
