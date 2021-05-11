FROM registry.nexus.azi.srv.br/hal/images/base-java11:latest

ENV APP_JAR=patrimonio-mobiliario-api.jar

COPY patrimonio-mobiliario-api-application .

RUN get-jarfile ./pom.xml $APP_JAR
