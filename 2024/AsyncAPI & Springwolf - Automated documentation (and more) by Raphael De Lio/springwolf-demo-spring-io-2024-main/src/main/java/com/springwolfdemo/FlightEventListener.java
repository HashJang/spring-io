package com.springwolfdemo;

import com.springwolfdemo.flights.BookingCancelled;
import com.springwolfdemo.flights.BookingConfirmed;
import com.springwolfdemo.flights.PassengerCheckedIn;
import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import io.github.springwolf.plugins.kafka.asyncapi.annotations.KafkaAsyncOperationBinding;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class FlightEventListener {

    @KafkaListener(topics = "confirmed-bookings")
    public void listenBookingConfirmed(BookingConfirmed booking) {
        System.out.println("Received booking confirmation: " + booking);
    }

    @KafkaListener(topics = "cancelled-bookings")
    public void listenBookingCancelled(BookingCancelled cancellation) {
        System.out.println("Received booking cancellation: " + cancellation);
    }
}
