package com.triporal;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.triporal.saga.TripBookingActivities;
import com.triporal.saga.TripBookingActivitiesImpl;
import com.triporal.saga.TripBookingWorkflow;
import com.triporal.saga.TripBookingWorkflowImpl;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowException;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

@Path("/")
public class TriporalResource {

    private String TASK_QUEUE = "TripBooking";

    @GET
    @Path("/saga")
    public String helloSaga() {

        // gRPC stubs wrapper that talks to the local docker instance of temporal service.
        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
        // client that can be used to start and signal workflows
        WorkflowClient client = WorkflowClient.newInstance(service);

        // worker factory that can be used to create workers for specific task queues
        WorkerFactory factory = WorkerFactory.newInstance(client);

        // Worker that listens on a task queue and hosts both workflow and activity implementations.
        Worker worker = factory.newWorker(TASK_QUEUE);

        // Workflows are stateful. So you need a type to create instances.
        worker.registerWorkflowImplementationTypes(TripBookingWorkflowImpl.class);

        // Activities are stateless and thread safe. So a shared instance is used.
        TripBookingActivities tripBookingActivities = new TripBookingActivitiesImpl();
        worker.registerActivitiesImplementations(tripBookingActivities);

        // Start all workers created by this factory.
        factory.start();
        System.out.println("Worker started for task queue: " + TASK_QUEUE);

        // now we can start running instances of our saga - its state will be persisted
        WorkflowOptions options = WorkflowOptions.newBuilder().setTaskQueue(TASK_QUEUE).build();
        TripBookingWorkflow trip1 = client.newWorkflowStub(TripBookingWorkflow.class, options);
        try {
            trip1.bookTrip("trip1");
        } catch (WorkflowException e) {
            // Expected
        }

        try {
            TripBookingWorkflow trip2 = client.newWorkflowStub(TripBookingWorkflow.class, options);
            trip2.bookTrip("trip2");
        } catch (WorkflowException e) {
            // Expected
        }

        return "hello";
    }
}