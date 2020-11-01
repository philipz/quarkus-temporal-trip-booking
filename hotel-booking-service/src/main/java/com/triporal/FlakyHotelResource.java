package com.triporal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/hotel")
public class FlakyHotelResource {

    private String[] hotels = {
        "Grand Hyatt",
        "Hyatt House",
        "Hyatt Regency",
        "Residence Inn",
        "Hampton Inn",
        "Holiday Inn",
        "Fairmont Olympic",
        "Lotte",
        "Embassy Suites",
        "The Paramount",
        "Four Seasons",
        "Inn at the Market",
        "Pan Pacific",
        "Kimpton",
        "Loews",
        "DoubleTree",
        "The Edgewater",
        "Motel 6"
    };


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public HotelBooking book(@QueryParam("transactionId") String transactionId,
                             @QueryParam("date") String dateStr, 
                             @QueryParam("nights") int nights,
                             @QueryParam("city") String city) throws InterruptedException {

        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate checkInDate = LocalDate.parse(dateStr, dtf);
        LocalDate checkOutDate = checkInDate.plusDays(nights);

        // Generate a random confirmation #
        int confirmationNumber = 100000 + new Random().nextInt(900000);

        // Generate a random rate
        int rate = new Random().nextInt(150) + 80;

        int total = rate * nights;

        // Generate a random sleep time to simulate latency and delay for processing
        int sleepTime = new Random().nextInt(2000) + 50;
        
        // 10% of the time hang for 6 seconds
        sleepTime = new Random().nextInt(9) == 0 ? 6000 : sleepTime;

        // Fail 10% of the time
        boolean failed = new Random().nextInt(9) == 0;

        // Simulate delay
        Thread.sleep(sleepTime);

        if(failed) {
            throw new RuntimeException("Failed to book a hotel");
        }

        // Pick a random hotel
        int randomHotelRef = new Random().nextInt(hotels.length-1);
        String hotel = hotels[randomHotelRef];


        HotelBooking hotelBooking = new HotelBooking(transactionId, city, hotel, rate, total, checkInDate, checkOutDate, Integer.toString(confirmationNumber));

        return hotelBooking;
    }
}