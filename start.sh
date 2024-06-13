#!/bin/bash

# Start authserver
cd authserver
mvn spring-boot:run &
cd ..

# Start resourceserver
cd resourceserver
mvn spring-boot:run &
cd ..

# Start authclient
cd authclient
mvn spring-boot:run &
cd ..

# Wait for all background jobs to complete
wait
