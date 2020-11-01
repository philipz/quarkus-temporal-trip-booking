package com.triporal.client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.triporal.model.HotelBooking;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/hotel")
@RegisterRestClient
public interface HotelBookingService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public HotelBooking book(@QueryParam("transactionId") String transactionId,
                             @QueryParam("date") String dateStr, 
                             @QueryParam("nights") int nights,
                             @QueryParam("city") String city);
}
