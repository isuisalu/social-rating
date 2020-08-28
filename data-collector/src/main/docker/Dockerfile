FROM openjdk:11-jre-slim
COPY maven/ /app
ENV KAFKA_BROKER_HOST=kafka1
ENV KAFKA_BROKER_PORT=9093
CMD ["java", "-jar","/app/${project.artifactId}-${project.version}.jar"]
EXPOSE 8080

