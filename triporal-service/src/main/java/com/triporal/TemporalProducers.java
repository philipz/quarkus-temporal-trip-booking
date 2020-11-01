package com.triporal;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.runtime.Startup;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.WorkerFactory;

class TemporalProducers {

  @Startup
  @ApplicationScoped
  WorkflowServiceStubs workflowService() {
    return WorkflowServiceStubs.newInstance();
  }

  @Startup
  @ApplicationScoped
  WorkflowClient workflowClient(WorkflowServiceStubs service) {
    return WorkflowClient.newInstance(service);
  }

  @Startup
  @ApplicationScoped
  WorkerFactory workflowFactory(WorkflowClient client) {
    return WorkerFactory.newInstance(client);
  }

}
