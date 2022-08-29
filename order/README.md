# Order service

This microservice:

- Accepts new transcription orders through REST endpoint;
- Then endpoint receives audio file as a payload;
- It pushes the uploaded file into S3 bucket;
- It publishes a message about file being uploaded to SNS topic;
- It responds with a link to Results endpoint


Links:

https://www.baeldung.com/java-aws-s3-reactive
https://vinsguru.medium.com/spring-webflux-file-upload-f6e3f3d3f5e1