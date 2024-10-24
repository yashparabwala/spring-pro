package com.bezkoder.spring.files.upload.db.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bezkoder.spring.files.upload.db.model.FileDB;

@Repository
public interface FileDBRepository extends JpaRepository<FileDB, String> {
    List<FileDB> findBySender(String sender);
    List<FileDB> findByReceiver(String receiver);
    Optional<FileDB> findByNameAndSenderAndReceiver(String name, String sender, String receiver);
    List<FileDB> findByGroupName(String groupName);
    Optional<FileDB> findByGroupNameAndName(String groupName, String name);
}
