package com.bezkoder.spring.files.upload.db.service;
import com.bezkoder.spring.files.upload.db.model.FileResponse;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.bezkoder.spring.files.upload.db.model.FileDB;
import com.bezkoder.spring.files.upload.db.repository.FileDBRepository;

@Service
public class FileStorageService {

  @Autowired
  private FileDBRepository fileDBRepository;

  /**
   * Stores the file with sender and receiver information.
   *
   * @param file     The multipart file to store.
   * @param sender   The username of the sender.
   * @param receiver The username of the receiver.
   * @return The saved FileDB entity.
   * @throws IOException If an I/O error occurs.
   */


  public FileDB store(MultipartFile file, String sender, String receiver) throws IOException {
    String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

    if (fileName.contains("..")) {
      throw new IOException("Invalid path sequence in file name: " + fileName);
    }

    // Use the individual sharing constructor correctly
    FileDB fileDB = new FileDB(fileName, file.getContentType(), file.getBytes(), sender, receiver);

    return fileDBRepository.save(fileDB);
  }


  /**
   * Retrieves a file based on its ID.
   *
   * @param id The ID of the file.
   * @return The FileDB entity.
   */
  public FileDB getFile(String id) {
    return fileDBRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("File not found with id " + id));
  }

  /**
   * Retrieves all files sent by a specific user.
   *
   * @param sender The username of the sender.
   * @return A list of FileDB entities.
   */
  public List<FileDB> getFilesSentByUser(String sender) {
    return fileDBRepository.findBySender(sender);
  }

  /**
   * Retrieves all files received by a specific user.
   *
   * @param receiver The username of the receiver.
   * @return A list of FileDB entities.
   */
  public List<FileDB> getFilesReceivedByUser(String receiver) {
    return fileDBRepository.findByReceiver(receiver);
  }

  /**
   * Retrieves a file based on its name, sender, and receiver.
   *
   * @param name     The name of the file.
   * @param sender   The username of the sender.
   * @param receiver The username of the receiver.
   * @return The FileDB entity.
   */
  public FileDB getFileByNameAndUsers(String name, String sender, String receiver) {
    return fileDBRepository.findByNameAndSenderAndReceiver(name, sender, receiver)
            .orElseThrow(() -> new RuntimeException("File not found with name " + name +
                    " between sender " + sender + " and receiver " + receiver));
  }

  /**
   * Retrieves all files in the repository.
   *
   * @return A stream of FileDB entities.
   */
  public Stream<FileDB> getAllFiles() {
    return fileDBRepository.findAll().stream();
  }

  public FileResponse storeForGroup(MultipartFile file, String groupName) throws IOException {
    String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

    if (fileName.contains("..")) {
      throw new IOException("Invalid path sequence in file name: " + fileName);
    }

    FileDB fileDB = new FileDB(fileName, file.getContentType(), file.getBytes(), groupName);
    FileDB savedFile = fileDBRepository.save(fileDB);

    // Return the response with relevant information
    return new FileResponse(savedFile.getId(), savedFile.getName(), savedFile.getType(), savedFile.getGroupName());
  }


  public Stream<FileDB> getAllFilesForGroup(String groupName) {
    return fileDBRepository.findByGroupName(groupName).stream();
  }

  public FileDB getFileFromGroup(String groupName, String fileName) {
    return fileDBRepository.findByGroupNameAndName(groupName, fileName).orElseThrow(() ->
            new RuntimeException("File not found with name " + fileName + " in group " + groupName));
  }


  public List<FileDB> getFilesInGroup(String groupName) {
    return fileDBRepository.findByGroupName(groupName);
  }
}

