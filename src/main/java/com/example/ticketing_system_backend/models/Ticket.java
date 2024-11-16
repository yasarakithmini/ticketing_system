package com.example.ticketing_system_backend.models;

import jakarta.persistence.*;


import java.sql.Timestamp;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.AVAILABLE;

    @ManyToOne
    @JoinColumn(name = "reserved_by", nullable = true)
    private Customer reservedBy;

    @Column(name = "reservation_time")
    private Timestamp reservationTime;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public Customer getReservedBy() {
        return reservedBy;
    }

    public void setReservedBy(Customer reservedBy) {
        this.reservedBy = reservedBy;
    }

    public Timestamp getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(Timestamp reservationTime) {
        this.reservationTime = reservationTime;
    }

    public enum TicketStatus {
        AVAILABLE,
        RESERVED,
        PURCHASED
    }
}
