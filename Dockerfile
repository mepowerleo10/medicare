FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
WORKDIR /opt/medicare/
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]