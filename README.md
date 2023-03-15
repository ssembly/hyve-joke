# Hyve Jokes

## Running the application

Before you can run this project, you must run a clean install maven command. This will build the jar file that's needed to run the application.

```
mvn clean install
```

Make sure you have Docker installed on your machine.
[Docker]: https://www.docker.com/

On the root directory of the project, run the following docker command to build the docker image.

```
docker build -t joke-application:latest .
```

After building your image, you can run with the following docker command:

```
docker run -p 8081:8081 joke-application
```

Once your application is up and running, you can access it at http://localhost:8081.

The following sample requests can be used to run the available endpoints and start laughing.
[Sample HTTP Payloads](Sample%20payloads.http)

And the following users have been created for testing.
```
id;username;first_name;last_name
1;kgomotso;Kgomotso;Mfubha
2;skye;Skye;Hyve
3;didier;Didier;Hyve
```

