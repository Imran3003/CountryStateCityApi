FROM openjdk:11-jre-slim

WORKDIR /usr/src/app

# Create necessary directories
RUN mkdir -p /usr/src/app/database /usr/src/app/script

RUN apt-get update && apt-get install -y default-mysql-client netcat && rm -rf /var/lib/apt/lists/*

# Copy the Spring Boot JAR and other files
COPY build/libs/CountryStateCityApi-0.0.1-SNAPSHOT.jar /usr/src/app/CountryStateCityApi-0.0.1-SNAPSHOT.jar
COPY src/main/resources/database/COUNTRY_STATE_CITY.sql /usr/src/app/database/COUNTRY_STATE_CITY.sql
COPY init.sh /usr/src/app/script/init-db.sh

# Expose the application port
EXPOSE 8080

# Make the shell script executable
RUN chmod +x /usr/src/app/script/init-db.sh

# Set environment variables with default values
ENV MYSQL_HOST=mysql
ENV MYSQL_USER=user1
ENV MYSQL_PASSWORD=password1
ENV MYSQL_DB=CountryStateCity

# Run the shell script to source the SQL file before starting the app
ENTRYPOINT ["/usr/src/app/script/init-db.sh"]
