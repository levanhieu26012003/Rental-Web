//package com.lvh.RentalBE.resolvers;
//
//import com.lvh.RentalBE.model.Motel;
//import graphql.kickstart.tools.GraphQLSubscriptionResolver;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.Flow;
//
//@Component
//public class MotelSubscriptionResolver implements GraphQLSubscriptionResolver {
//
//    private final MotelSubscription motelSubscription;
//
//    public MotelSubscriptionResolver(MotelSubscription motelSubscription) {
//        this.motelSubscription = motelSubscription;
//    }
//
//    public Flow.Publisher<Motel> motelAdded() {
//        return (Flow.Publisher<Motel>) motelSubscription.motelAdded();
//    }
//}
//
