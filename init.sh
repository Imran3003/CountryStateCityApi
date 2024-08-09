#!/bin/sh

# Wait for the MySQL service to be available
echo "Waiting for MySQL to be available..."
until mysql -h $MYSQL_HOST -u $MYSQL_USER -p$MYSQL_PASSWORD -e "select 1" > /dev/null 2>&1; do
  echo "MySQL is not yet available, retrying in 5 seconds..."
  sleep 5
done

# Check if the database has already been set up
echo "Checking if the database setup has been completed..."
DB_SETUP_FLAG_TABLE="setup_flag"
DB_SETUP_QUERY="SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = '$MYSQL_DB' AND table_name = '$DB_SETUP_FLAG_TABLE';"
DB_SETUP_INSERT="INSERT INTO $DB_SETUP_FLAG_TABLE (flag) VALUES ('done');"

# Check if the flag table exists
FLAG_TABLE_EXISTS=$(mysql -h $MYSQL_HOST -u $MYSQL_USER -p$MYSQL_PASSWORD -e "$DB_SETUP_QUERY" -s -N)

if [ "$FLAG_TABLE_EXISTS" -eq 0 ]; then
  # If the table does not exist, execute the SQL script
  echo "Sourcing the SQL file..."
  mysql -h $MYSQL_HOST -u $MYSQL_USER -p$MYSQL_PASSWORD $MYSQL_DB < /usr/src/app/database/COUNTRY_STATE_CITY.sql

  # Create the flag table and insert a record to indicate setup is complete
  echo "Creating flag table..."
  mysql -h $MYSQL_HOST -u $MYSQL_USER -p$MYSQL_PASSWORD $MYSQL_DB -e "CREATE TABLE $DB_SETUP_FLAG_TABLE (flag VARCHAR(255));"
  mysql -h $MYSQL_HOST -u $MYSQL_USER -p$MYSQL_PASSWORD $MYSQL_DB -e "$DB_SETUP_INSERT"
else
  echo "Database setup already completed."
fi

# Start the Spring Boot application
echo "Starting the Spring Boot application..."
exec java -jar /usr/src/app/CountryStateCityApi-0.0.1-SNAPSHOT.jar
