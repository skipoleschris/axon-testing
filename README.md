# Axon Testing

This project provides a bunch of configurations that allow testing out the different capabilities of
the Axon Framework and Axon Server to discover how it works.

## Axon Server

These experiments assume there is an Axon Server running locally. The easiest way to start one is using
docker:

    docker run -d --name axonserver -p 8024:8024 -p 8124:8124 axoniq/axonserver

The Axon Server admin interface is available on http://localhost:8024

## Profiles

The code is arranged as a single codebase that utilises SpringFramework profiles for different combinations
of behaviour. This allows multiple different versions of the app to be started that create microservices with
different sets of capabilities.

The following spring profiles are currently possible:

- *monolith* - Loads all functionality into a single monolithic app. Useful for development
- *initiator* - Loads just the main controller that allows different scenarios to be explored by initiating various http
  calls to different enpoints
- *basic1* - Loads a basic handler for commands and events
- *basic2* - Loads the same as basic1 but using different port configurations
- *additional* - Additional handler for scenarios where we need a different spring application name

The idea of the handler1 and handler2 profiles is to allow two versions of the same microservice to be running to
see how multiple instances deal with commands, queries and events.

## Command Experiments

The first experiments look at how commands are dispatched and handled. The *initiator* profile must be running.
There are a number of command experiments that can be dispatched:

    curl -v http://localhost:8080/axon-test/command/{experiment}/{id}

The `id` parameter is a unique number that can be used to trace commands through the logs. The `experiment`
parameter controls which experiment is executed and supports the following values:

* `local`
* `remote`
* `retry`
* `exception`

### Experiment 1 - Create a locally handled command

    curl -v http://localhost:8080/axon-test/command/local/1

This should always work as the command is dispatched and handled by the initiator.

### Experiment 2 - Create a remotely handled command while no basic service is running

    curl -v http://localhost:8080/axon-test/command/remote/2

Generates an immediate error because the command cannot be dispatched as there are no
handlers available to process it. This demonstrates that commands are not buffered or cached.

    curl -v http://localhost:8080/axon-test/command/retry/3

Loops through 10 attempts to dispatch the command at 6 second intervals. As there
are still no handlers available, an error is returned once all the retries are used. This demonstrates
the ability to configure retry functionality within the command gateway.

### Experiment 3 - Create a remotely handled command with one basic service running

    curl -v http://localhost:8080/axon-test/command/remote/4

The command is dispatched by the initiator and handled by the basic service.

### Experiment 4 - Create remotely handled commands with two basic services running

    curl -v http://localhost:8080/axon-test/command/remote/5
    curl -v http://localhost:8080/axon-test/command/remote/6
    curl -v http://localhost:8080/axon-test/command/remote/7
    curl -v http://localhost:8080/axon-test/command/remote/8

The commands are dispatched by the initiator. They are handled by either on basic service or the other,
but we can see they are only ever handled once.

### Experiment 5 - Start a handler after a retryable command has already been dispatched

    curl -v http://localhost:8080/axon-test/command/retry/9

Only the initiator is running when the command is sent. While it is looping through the retries,
a basic service is started and the command is successfully handled.

### Experiment 6 - Send a command that causes a business exception

    curl -v http://localhost:8080/axon-test/command/exception/10

The business exception is thrown by the basic service and propagated back to the caller. Demonstrates
the correct way that we should be working with commands.

## Query Experiments

These experiments work pretty much the same as for commands and look at how queries are dispatched and handled.
The *initiator* profile must be running. There are a number of query experiments that can be dispatched:

    curl -v http://localhost:8080/axon-test/query/{experiment}/{id}

The {id} parameter is a unique number that can be used to trace queries through the logs. The `experiment`
parameter controls which experiment is executed and supports the following values:

* `local`
* `remote`
* `multiple`
* `subscribe`

All requests return a simple json document containing the id and response details related to the experiment
that has been completed.

### Experiment 1 - Create a locally handled query

    curl -v http://localhost:8080/axon-test/query/local/1

This should always work as the query is dispatched and handled by the initiator.

### Experiment 2 - Create a remotely handled query while no basic service is running

    curl -v http://localhost:8080/axon-test/query/remote/2

This generates an error because the query cannot be dispatched as there are no handlers available to process
it. This demonstrates that query are not buffered or cached.

### Experiment 3 - Create a remotely handled query with one basic service running

    curl -v http://localhost:8080/axon-test/query/remote/3

The query is dispatched by the initiator and handled by the basic service.

### Experiment 4 - Create remotely handled queries with two basic services running

    curl -v http://localhost:8080/axon-test/query/remote/4
    curl -v http://localhost:8080/axon-test/query/remote/5
    curl -v http://localhost:8080/axon-test/query/remote/6
    curl -v http://localhost:8080/axon-test/query/remote/7

The queries are dispatched by the initiator. They are handled by either one basic service or the other,
but we can see they are only ever handled once.

### Experiment 5 - Create a scatter-gather query with only the two basic services running

    curl -v http://localhost:8080/axon-test/query/multiple/8

The query is dispatched and is handled by one of the basic services. Note that although
there are query handlers in both basic instances, only one is invoked due to them having
the same Spring application name.

### Experiment 6 - Create a scatter-gather query with also the additional service running

    curl -v http://localhost:8080/axon-test/query/multiple/9

The query handler in both the basic and additional service is invoked and the results from
both are combined and returned.

### Experiment 7 - Create a subscription query

    curl -v http://localhost:8080/axon-test/query/subscribe/10

A subscription query is created that gets an initial set of results and then waits for 10 seconds
for additional updates. These are then joined together and returned via the web api. The console
logs for the initiator and basic services show the updates being emitted and consumed.

## Non-Aggregate Event Experiments

In these experiments we look at how events are handled when the event handle is not inside an aggregate.
This will typically be the case for event handlers that populate view models or that react to events to
carry out other operations.

### Experiment 1 - Simple Events

In this experiment we call an endpoint on the initiator that dispatches a locally handled command. The
command handler then generates a simple event that isn't tied to any particular aggregate:

    curl -v http://localhost:8080/axon-test/event/simple/1

#### Experiment 1a - No consumers of the event are running

In this version we don't have any instances of the basic microservice running. We can generate the event
and if we go to the Axon Server console we can see the event in the event store list. Multiple events
can be generated, and it is possible to see them added to the store.

#### Experiment 1b - A single basic service is started

When we then start one instance of the basic microservice then we can see it consuming and processing
the events that were added to the store in part 1a.

#### Experiment 1c - Stop and restart the basic service

When the service is restarted we see it consume and process all the same events again. This maybe not
what you would expect to happen. If processing those events initiated some external activity like moving
money then each restart would carry out all operations again! Useful though if we need to re-process all the
events to build up some internal application state.

#### Experiment 1d - Start a second instance of the basic service

Again, then the second instance is started then we see it also consume and process all the events. Again,
maybe not what was expected and potentially bad if those events trigger some non-idempotent processing.

So, the question from the above experiment is why are the events getting replayed each time we start up
a new microservice instance with an event handler for th SimpleEvent type?

