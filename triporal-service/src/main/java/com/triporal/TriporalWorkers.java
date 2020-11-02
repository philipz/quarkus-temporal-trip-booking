package com.triporal;

import javax.inject.Named;
import javax.inject.Singleton;

import com.triporal.saga.TripBookingActivities;
import com.triporal.saga.TripBookingWorkflowImpl;

import io.quarkus.runtime.Startup;

import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class TriporalWorkers {

    @Startup
    @Singleton
    @Named("tripBookingWorker")
    Worker tripBookingWorker(WorkerFactory factory, TripBookingActivities tripBookingActivities) {
        
        // Worker that listens on a task queue and hosts both workflow and activity implementations.
        Worker worker = factory.newWorker("TripBooking");

        // Workflows are stateful. So you need a type to create instances.
        worker.registerWorkflowImplementationTypes(TripBookingWorkflowImpl.class);
        worker.registerActivitiesImplementations(tripBookingActivities);

        // Start all workers created by this factory.
        factory.start();

        return worker;
    }
}
