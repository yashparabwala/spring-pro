package com.bezkoder.spring.files.upload.db.controller;
import com.bezkoder.spring.files.upload.db.model.FileResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bezkoder.spring.files.upload.db.message.ResponseFile;
import com.bezkoder.spring.files.upload.db.message.ResponseMessage;
import com.bezkoder.spring.files.upload.db.model.FileDB;
import com.bezkoder.spring.files.upload.db.service.FileStorageService;

@RestController
@CrossOrigin(origins = "http://localhost:8081")
@RequestMapping("/resource")
public class FileController {

  @Autowired
  private FileStorageService storageService;

  // Upload a file from sender to receiver
  @PostMapping("/{sender}/{receiver}")
  public ResponseEntity<ResponseMessage> uploadFile(
          @PathVariable String sender,
          @PathVariable String receiver,
          @RequestParam("file") MultipartFile file) {
    String message;
    try {
      FileDB savedFile = storageService.store(file, sender, receiver);  // Store the file
      message = "Uploaded the file successfully: " + savedFile.getName();  // Send back the file name
      return ResponseEntity.ok(new ResponseMessage(message));
    } catch (IOException e) {
      message = "Could not upload the file: " + file.getOriginalFilename() + "!";
      return ResponseEntity.status(417).body(new ResponseMessage(message));
    }
  }

  // List all files sent by a specific user
  @GetMapping("/sent/{sender}")
  public ResponseEntity<List<ResponseFile>> getListOfSentFiles(@PathVariable String sender) {
    List<FileDB> files = storageService.getFilesSentByUser(sender);

    List<ResponseFile> responseFiles = files.stream().map(dbFile -> {
      String fileDownloadUri = ServletUriComponentsBuilder
              .fromCurrentContextPath()
              .path("/resource/files/")
              .path(dbFile.getId())
              .toUriString();

      return new ResponseFile(
              dbFile.getName(),
              fileDownloadUri,
              dbFile.getType(),
              dbFile.getData().length
      );
    }).collect(Collectors.toList());

    return ResponseEntity.ok(responseFiles);
  }

  // List all files received by a specific user
  @GetMapping("/received/{receiver}")
  public ResponseEntity<List<ResponseFile>> getListOfReceivedFiles(@PathVariable String receiver) {
    List<FileDB> files = storageService.getFilesReceivedByUser(receiver);

    List<ResponseFile> responseFiles = files.stream().map(dbFile -> {
      String fileDownloadUri = ServletUriComponentsBuilder
              .fromCurrentContextPath()
              .path("/resource/files/")
              .path(dbFile.getId())
              .toUriString();

      return new ResponseFile(
              dbFile.getName(),
              fileDownloadUri,
              dbFile.getType(),
              dbFile.getData().length
      );
    }).collect(Collectors.toList());

    return ResponseEntity.ok(responseFiles);
  }

  // Download a specific file by its ID
  @GetMapping("/files/{id}")
  public ResponseEntity<byte[]> getFile(@PathVariable String id) {
    FileDB fileDB = storageService.getFile(id);

    return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(fileDB.getType()))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
            .body(fileDB.getData());
  }

  // Download a specific file by its name, sender, and receiver
  @GetMapping("/download/{sender}/{receiver}/{name}")
  public ResponseEntity<byte[]> downloadFileByNameAndUsers(
          @PathVariable String sender,
          @PathVariable String receiver,
          @PathVariable String name) {
    FileDB fileDB = storageService.getFileByNameAndUsers(name, sender, receiver);

    return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(fileDB.getType()))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
            .body(fileDB.getData());
  }

  // Upload a file to a specific group
  @PostMapping("/group/{groupName}")
  public ResponseEntity<ResponseMessage> uploadFileToGroup(
          @PathVariable String groupName,
          @RequestParam("file") MultipartFile file) {
    String message;
    try {
      FileResponse savedFile = storageService.storeForGroup(file, groupName);  // Adjusted type
      message = "Uploaded the file successfully, name : " + savedFile.getFileName();  // Use appropriate getter
      return ResponseEntity.ok(new ResponseMessage(message));
    } catch (IOException e) {
      message = "Could not upload the file to group: " + groupName;
      return ResponseEntity.status(417).body(new ResponseMessage(message));
    }
  }

  // List all files in a specific group
  @GetMapping("/group/{groupName}")
  public ResponseEntity<List<ResponseFile>> getFilesInGroup(@PathVariable String groupName) {
    List<FileDB> files = storageService.getFilesInGroup(groupName);

    List<ResponseFile> responseFiles = files.stream().map(dbFile -> {
      String fileDownloadUri = ServletUriComponentsBuilder
              .fromCurrentContextPath()
              .path("/resource/files/")
              .path(dbFile.getId())
              .toUriString();

      return new ResponseFile(
              dbFile.getName(),
              fileDownloadUri,
              dbFile.getType(),
              dbFile.getData().length
      );
    }).collect(Collectors.toList());

    return ResponseEntity.ok(responseFiles);
  }

  // Download a specific file from a group by its name
  @GetMapping("/group/{groupName}/download/{name}")
  public ResponseEntity<byte[]> downloadFileFromGroup(
          @PathVariable String groupName,
          @PathVariable String name) {
    FileDB fileDB = storageService.getFileFromGroup(groupName, name);

    return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(fileDB.getType()))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
            .body(fileDB.getData());
  }
}