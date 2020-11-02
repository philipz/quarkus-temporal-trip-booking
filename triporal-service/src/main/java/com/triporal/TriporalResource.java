package com.triporal;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.triporal.model.HotelBooking;
import com.triporal.saga.TripBookingWorkflow;

import io.quarkus.runtime.Startup;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowException;
import io.temporal.client.WorkflowOptions;
import io.temporal.worker.Worker;

@Startup
@Path("/")
public class TriporalResource {

    @Inject
    WorkflowClient workflowClient;

    @Inject
    @Named("tripBookingWorker")
    Worker tripBookingWorker;

    @GET
    @Path("/saga")
    @Produces(MediaType.APPLICATION_JSON)
    public HotelBooking helloSaga() {

        try {
            String transactionId = UUID.randomUUID().toString();
            System.out.println("Booking trip " + transactionId);

            WorkflowOptions workflowOptions = WorkflowOptions.newBuilder()
                .setTaskQueue(tripBookingWorker.getTaskQueue())
                .setWorkflowId("workflow-" + transactionId)
                .build();

            // now we can start running instances of our saga - its state will be persisted
            TripBookingWorkflow tripBookingWorkflow = workflowClient.newWorkflowStub(TripBookingWorkflow.class, workflowOptions);

            return tripBookingWorkflow.bookTrip(transactionId);

        } catch (WorkflowException e) {
            return null;
            // Expected
        }

        
    }
}