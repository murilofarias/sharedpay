FROM openjdk:8


WORKDIR /opt/spring_boot

COPY ./target/sharedpay-0.0.1-SNAPSHOT.jar sharedpay-0.0.1-SNAPSHOT.jar

SHELL ["/bin/sh", "-c"]

EXPOSE 5005
EXPOSE 8080

CMD java -jar sharedpay-0.0.1-SNAPSHOT.jar