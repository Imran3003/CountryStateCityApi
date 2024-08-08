#!/bin/sh

# Wait for the MySQL service to be available
echo "Waiting for MySQL to be available..."
until mysql -h $MYSQL_HOST -u $MYSQL_USER -p$MYSQL_PASSWORD -e "select 1" > /dev/null 2>&1; do
  echo "MySQL is not yet available, retrying in 5 seconds..."
  sleep 5
done

# Execute the SQL script to set up the database
echo "Sourcing the SQL file..."
mysql -h $MYSQL_HOST -u $MYSQL_USER -p$MYSQL_PASSWORD $MYSQL_DB < /usr/src/app/database/COUNTRY_STATE_CITY.sql

# Start the Spring Boot application
echo "Starting the Spring Boot application..."
exec java -jar /usr/src/app/CountryStateCityApi-0.0.1-SNAPSHOT.jar
