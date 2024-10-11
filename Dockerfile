FROM alpine:3.20.3
COPY . .
RUN apk add openjdk17 maven
RUN mvn clean package -DskipTests
EXPOSE 8080
EXPOSE 8082