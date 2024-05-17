FROM amazoncorretto:17

WORKDIR /app

COPY "target/walgreensreplica-0.0.1-SNAPSHOT.jar" /app/

ENV KAFKA_BOOTSTRAP_SERVERS=host.docker.internal:29092,host.docker.internal:39092,host.docker.internal:49092

CMD ["java", "-jar", "walgreensreplica-0.0.1-SNAPSHOT.jar"]


# Run from the root of the Repo

# docker build --no-cache -f Docker/userManagment-mini-service/Dockerfile -t  walgreens/mini-service/user-management .
# docker run -it walgreens/mini-service/user-management