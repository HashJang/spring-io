package com.springwolfdemo.exposed;

import com.springwolfdemo.flights.PassengerCheckedIn;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ExposedFlightEventListener {
    @KafkaListener(topics = "check-ins", groupId = "check-in-processors")
    public void listenPassengerCheckedIn(PassengerCheckedIn checkIn) {
        System.out.println("Received passenger check-in: " + checkIn);
    }
}
