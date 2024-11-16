package com.example.ticketing_system_backend.repository;


import com.example.ticketing_system_backend.models.TicketLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketLogRepository extends JpaRepository<TicketLog, Long> {

}
