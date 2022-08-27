# Speech 2 Text (Microservice)

This microservice:

- Listens on SQS queue for messages indicating that 
  there is an audio file ready to be transcribed;
- When message is received it processes the file;
- When finished processing it stores transcription in local DB;
- Stored transcriptions are exposed through REST endpoint;
- Finally, it sends a message to SNS topic indicating that
  this instance has a transcription of processed audio file;

Audio files should be placed in a location on S3 bucket.
File format should be Microsoft WAV, (16khz 16bit).
To convert to this format use:
    
    multipass shell

    ffmpeg -i file.mp4 -acodec pcm_s16le -ac 1 -ar 16000 file.wav

---

The code is copied from:

https://www.techgeeknext.com/cloud/aws/amazon-s3-springboot-upload-file-in-s3-bucket#S3ClientConfig
https://www.techgeeknext.com/cloud/aws/amazon-s3-springboot-download-file-in-s3-bucket