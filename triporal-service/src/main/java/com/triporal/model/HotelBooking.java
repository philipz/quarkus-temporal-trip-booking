package com.triporal.model;

import java.beans.ConstructorProperties;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class HotelBooking {
    public final String transactionId;
    public final String city;
    public final String hotel;
    public final int rate;
    public final int total;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public final LocalDate checkInDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public final LocalDate checkOutDate;
    public final String confirmationNumber;
    

    @ConstructorProperties({"transactionId","city", "hotel", "rate", "total", "checkInDate", "checkOutDate", "confirmationNumber"})
    public HotelBooking(String transactionId, String city, String hotel, int rate, int total, LocalDate checkInDate, LocalDate checkOutDate, String confirmationNumber) {
        this.transactionId = transactionId;
        this.city = city;
        this.hotel = hotel;
        this.rate = rate;
        this.total = total;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.confirmationNumber = confirmationNumber;
    }
}
