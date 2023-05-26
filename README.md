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

The idea of the handler1 and handler2 profiles is to allow two versions of the same microservice to be running to
see how multiple instances deal with commands, queries and events.

## Command Experiments

The first experiment looks at how commands are dispatched and handled. The *initiator* profile must be running.
There are two commands that can be dispatched, one that will be handled locally by the initiator microservice:

    curl -v http://localhost:8080/axon-test/command/local/{id}

where {id} is a unique number that can be used to trace commands through the logs. The second dispatches
a command that cannot be handled by the initiator but needs to be handled remotely by an instance of the
*basic* microservices:

    curl -v http://localhost:8080/axon-test/command/remote/{id}

### Experiment 1 - Create a locally handled command

This should always work as the command is dispatched and handled by the initator.

### Experiment 2 - Create a remotely handled command while no basic service is running

This generates an error because the command cannot be dispatched as there are no handlers available to process
it. This demonstrates that commands are not buffered or cached.

### Experiment 3 - Create a remotely handled command with one basic service running

The command is dispatched by the initiator and handled by the basic service.

### Experiment 4 - Create remotely handled commands with two basic services running

The commands are dispatched by the initiator. They are handled by either on basic service or the other,
but we can see they are only ever handled once.
