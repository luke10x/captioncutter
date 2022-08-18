package dev.luke10x.captioncutter.speech2text.controller;

import dev.luke10x.captioncutter.speech2text.aws.service.S3BucketStorageService;
import dev.luke10x.captioncutter.speech2text.controller.dto.TranscriptionSummary;
import jdk.jfr.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class S3BucketStorageController {
    @Autowired
    S3BucketStorageService service;

//    @GetMapping("/file/download/{fileName}")
//    public ResponseEntity<String> getListOfFiles(
//            @PathVariable("fileName") String fileName
//    ) {
//        return new ResponseEntity<>(service.downloadFile(fileName), HttpStatus.OK);
//    }

    @PostMapping(value = "/file/upload", produces = "application/json")
    public ResponseEntity<TranscriptionSummary> uploadFile(@RequestParam("fileName") String fileName,
                                                           @RequestParam("file") MultipartFile file) {
        service.uploadFile(fileName, file);

        TranscriptionSummary responseBody = TranscriptionSummary.builder()
                .status("ready to be transcribed")
                .fileName(fileName)
                .transcription("nothing yet")
                .build();
        return new ResponseEntity<TranscriptionSummary>(responseBody, HttpStatus.OK);
    }
}