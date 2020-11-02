/*
 *  Copyright (c) 2020 Temporal Technologies, Inc. All Rights Reserved
 *
 *  Copyright 2012-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 *  Modifications copyright (C) 2017 Uber Technologies, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"). You may not
 *  use this file except in compliance with the License. A copy of the License is
 *  located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 *  or in the "license" file accompanying this file. This file is distributed on
 *  an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *  express or implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 */

package com.triporal.saga;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.triporal.client.HotelBookingService;
import com.triporal.model.HotelBooking;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.runtime.annotations.RegisterForReflection;

@Singleton // Activities are stateless and thread safe. So a shared instance is used.
@RegisterForReflection
public class TripBookingActivitiesImpl implements TripBookingActivities {

  @Inject
  @RestClient
  HotelBookingService hotelBookingService;

  @Override
  public String reserveCar(String name) {
    System.out.println("reserve car for '" + name + "'");
    return UUID.randomUUID().toString();
  }

  @Override
  public String bookFlight(String name) {
    //System.out.println("failing to book flight for '" + name + "'");
    return UUID.randomUUID().toString();
    //throw new RuntimeException("Flight booking did not work");
  }

  @Override
  public HotelBooking bookHotel(String transactionId) {

    String dateStr = "30-10-2020";
    int nights = 3;
    String city = "Seattle";

    return hotelBookingService.book(transactionId, dateStr, nights, city);
  }

  @Override
  public String cancelFlight(String reservationID, String name) {
    System.out.println("cancelling flight reservation '" + reservationID + "' for '" + name + "'");
    return UUID.randomUUID().toString();
  }

  @Override
  public String cancelHotel(String reservationID, String name) {
    System.out.println("cancelling hotel reservation '" + reservationID + "' for '" + name + "'");
    return UUID.randomUUID().toString();
  }

  @Override
  public String cancelCar(String reservationID, String name) {
    System.out.println("cancelling car reservation '" + reservationID + "' for '" + name + "'");
    return UUID.randomUUID().toString();
  }
}
