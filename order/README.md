# Order service

This microservice:

- Accepts new transcription orders through REST endpoint;
- Then endpoint receives audio file as a payload;
- It pushes the uploaded file into S3 bucket;
- It publishes a message about file being uploaded to SNS topic;
- It responds with a link to Results endpoint