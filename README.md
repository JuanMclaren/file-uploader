# file-uploader
This is a java/ spring boot / spring batch project.

Consists of 2 main packages 
1 - mvc package which exposes a restful API to accept a file through a POST call and places the file in a location.
2- batch package which reads the file and process it and persists onto a db.

This project can be further microserviced into 3 projects

1 - MVC
2- Batch read
3 - Batch process and write. - This can be achieved by changing the implementation of the writer from JDBC Writer to one of the following https://docs.spring.io/spring-batch/docs/current/reference/html/readersAndWriters.html#messagingReadersAndWriters

The data is persisted on a H2 due to dev constraints(on maintaining the batch tables) but would ideally prefer a sequel db - mostly oracle.

Spring batch is preferred as it has a lot of featueres to process huge data and can be processed in batch sizes and the batch tables helps us in tracking jobs in case of failures.

Test cases have been added to test the batch logic.

Dockerfile is used to create an image and a dockercompose is used to run the db and app on the same instance.
