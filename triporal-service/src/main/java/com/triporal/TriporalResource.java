package com.triporal;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.triporal.saga.TripBookingWorkflow;

import io.quarkus.runtime.Startup;
import io.temporal.client.WorkflowException;

@Startup
@Path("/")
public class TriporalResource {

    @Inject
    TripBookingWorkflow tripBookingWorkflow;

    @GET
    @Path("/saga")
    public String helloSaga() {

        try {
            tripBookingWorkflow.bookTrip("trip1");
        } catch (WorkflowException e) {
            // Expected
        }

        return "hello";
    }
}