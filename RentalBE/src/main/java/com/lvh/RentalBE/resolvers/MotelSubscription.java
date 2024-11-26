package com.lvh.RentalBE.resolvers;

import com.lvh.RentalBE.model.Motel;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

@Component
public class MotelSubscription {

    private final Sinks.Many<Motel> motelSink = Sinks.many().multicast().onBackpressureBuffer();

    public Publisher<Motel> motelAdded() {
        return motelSink.asFlux();
    }

    public void publish(Motel motel) {
        motelSink.tryEmitNext(motel);
    }
}
