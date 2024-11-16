package com.example.ticketing_system_backend.config;




import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public int maxTicketCapacity() {
        return 100;
    }

    @Bean
    public int ticketReleaseRate() {
        return 1; // seconds
    }

    @Bean
    public int customerReservationDelay() {
        return 2; // seconds
    }
}
