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

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.operators.multi.processors.UnicastProcessor;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.failure.ActivityFailure;
import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;
import io.vertx.core.json.JsonObject;

import java.time.Duration;

import com.triporal.model.HotelBooking;

@RegisterForReflection
public class TripBookingWorkflowImpl implements TripBookingWorkflow {

  private final ActivityOptions options =
      ActivityOptions.newBuilder()
          .setScheduleToCloseTimeout(Duration.ofHours(1))
          .setRetryOptions(RetryOptions.newBuilder().setMaximumAttempts(2).build())
          .build();

  private final TripBookingActivities activities = Workflow.newActivityStub(TripBookingActivities.class, options);

  @Override
  public HotelBooking bookTrip(String name) {

    UnicastProcessor<String> processor = UnicastProcessor.create();
    Multi<JsonObject> bookingStream = processor.onItem().transform(JsonObject::mapFrom);
    
    // Configure SAGA to run compensation activities in parallel
    Saga.Options sagaOptions = new Saga.Options.Builder().setParallelCompensation(true).build();
    Saga saga = new Saga(sagaOptions);

    try {

      Promise<String> carReservationIDPromise = Async.function(activities::reserveCar, name);

      carReservationIDPromise.thenApply(carReservationID -> {
        saga.addCompensation(activities::cancelCar, carReservationID, name);
        processor.onNext(carReservationID);
        return carReservationID;
      });



      //String carReservationID = activities.reserveCar(name);
      //saga.addCompensation(activities::cancelCar, carReservationID, name);

      HotelBooking hotelBooking = activities.bookHotel(name);
      saga.addCompensation(activities::cancelHotel, hotelBooking.transactionId, name);

      String flightReservationID = activities.bookFlight(name);
      saga.addCompensation(activities::cancelFlight, flightReservationID, name);

      return hotelBooking;
    } catch (ActivityFailure e) {
      saga.compensate();
      throw e;
    }
  }
}
