FROM openjdk:11-jre-slim
COPY maven/ /app
ENV REDIS_HOST=redis
CMD ["java", "-jar","/app/${project.artifactId}-${project.version}.jar"]
EXPOSE 8081

