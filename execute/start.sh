#!/bin/sh
echo starting: Gestione-Applicazioni-Service.
#export OTEL_EXPORTER_OTLP_ENDPOINT=http://preprorakze02:8200
#export OTEL_EXPORTER_OTLP_HEADERS="Authorization=Bearer Passw0rd"
#export OTEL_METRICS_EXPORTER=otlp
#export OTEL_LOGS_EXPORTER=otlp
#export OTEL_RESOURCE_ATTRIBUTES=service.name=portale-gestione-applicazioni-service,service.version=1.0.0,deployment.environment=pre-production





nohup java -Dspring.profiles.active=dev  -XX:+UseZGC -XX:+ZGenerational -Xms512m -Xmx512m -XX:+ExitOnOutOfMemoryError   -jar ./gestioneapplicazioni-service-portale.jar --logging.config=./logback.xml 2>&1 &
echo $! > ./pid.file
