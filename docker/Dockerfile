FROM maven:3.8.6-jdk-11 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM tomcat:9.0-jdk11-openjdk

# Download PostgreSQL JDBC driver
RUN curl -o /usr/local/tomcat/lib/postgresql.jar https://jdbc.postgresql.org/download/postgresql-42.5.4.jar

# Set JVM options to fix cgroups issues with Java 11 in Docker
ENV JAVA_OPTS="-XX:-UseContainerSupport -Djdk.attach.allowAttachSelf=true"

COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
