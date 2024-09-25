package com.springwolfdemo.exposed;

import com.springwolfdemo.flights.BookingCancelled;
import com.springwolfdemo.flights.BookingConfirmed;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import io.github.springwolf.core.asyncapi.annotations.AsyncPublisher;
import io.github.springwolf.plugins.kafka.asyncapi.annotations.KafkaAsyncOperationBinding;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class FlightEventPublisher {

    private KafkaTemplate<String, BookingConfirmed> bookingConfirmedTemplate;
    private KafkaTemplate<String, BookingCancelled> bookingCancelledTemplate;


    public FlightEventPublisher(KafkaTemplate<String, BookingConfirmed> bookingConfirmedTemplate,
                                KafkaTemplate<String, BookingCancelled> bookingCancelledTemplate) {
        this.bookingConfirmedTemplate = bookingConfirmedTemplate;
        this.bookingCancelledTemplate = bookingCancelledTemplate;
    }

    @AsyncPublisher(
            operation = @AsyncOperation(
                    channelName = "confirmed-bookings",
                    description = "This channel is used to exchange messages about confirmed booking events",
                    servers = {"production", "staging"}
            )
    )
    @KafkaAsyncOperationBinding
    public void sendBookingConfirmation(BookingConfirmed booking) {
        bookingConfirmedTemplate.send("bookings", booking);
    }

    @AsyncPublisher(
            operation = @AsyncOperation(
                    channelName = "cancelled-bookings",
                    description = "This channel is used to exchange messages about cancelled booking events",
                    servers = {"production", "staging"}
            )
    )
    @KafkaAsyncOperationBinding
    public void sendBookingCancelled(BookingCancelled cancellation) {
        bookingCancelledTemplate.send("bookings", cancellation);
    }
}
