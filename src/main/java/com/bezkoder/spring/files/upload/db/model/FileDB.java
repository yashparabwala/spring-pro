package com.bezkoder.spring.files.upload.db.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "files")
public class FileDB {
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;

  private String name;

  private String type;

  @Lob
  private byte[] data;

  private String sender;

  private String receiver;

  private String groupName;

  public FileDB() {
  }

//  public FileDB(String name, String type, byte[] data, String sender, String receiver, String groupName) {
//    this.name = name;
//    this.type = type;
//    this.data = data;
//    this.sender = sender;
//    this.receiver = receiver;
//    this.groupName = groupName;
//  }

  public FileDB(String fileName, String contentType, byte[] bytes, String sender, String receiver) {
    this.name = fileName;
    this.type = contentType;
    this.data = bytes;
    this.sender = sender;
    this.receiver = receiver;
  }

//  public FileDB(String fileName, String contentType, byte[] bytes, String sender, String receiver) {
//  }

  // Getters and Setters

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }
}
