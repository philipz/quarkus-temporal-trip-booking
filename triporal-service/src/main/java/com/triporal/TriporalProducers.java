package com.triporal;

import javax.enterprise.context.ApplicationScoped;

import com.triporal.saga.TripBookingActivities;
import com.triporal.saga.TripBookingActivitiesImpl;
import com.triporal.saga.TripBookingWorkflow;
import com.triporal.saga.TripBookingWorkflowImpl;

import io.quarkus.runtime.Startup;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class TriporalProducers {
    
    @Startup
    @ApplicationScoped
    TripBookingWorkflow tripBookingWorkflow(WorkerFactory factory, WorkflowClient client, TripBookingActivities tripBookingActivities) {
        final String TASK_QUEUE = "TripBooking";
        
        // Worker that listens on a task queue and hosts both workflow and activity implementations.
        Worker worker = factory.newWorker(TASK_QUEUE);

        // Workflows are stateful. So you need a type to create instances.
        worker.registerWorkflowImplementationTypes(TripBookingWorkflowImpl.class);
        worker.registerActivitiesImplementations(tripBookingActivities);

        // Start all workers created by this factory.
        factory.start();

        // now we can start running instances of our saga - its state will be persisted
        WorkflowOptions options = WorkflowOptions.newBuilder().setTaskQueue(TASK_QUEUE).build();      
        return client.newWorkflowStub(TripBookingWorkflow.class, options);
    }
}
