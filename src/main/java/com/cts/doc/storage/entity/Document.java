package com.cts.doc.storage.entity;

import java.util.Objects;

public class Document {


    private String id;

    private String fileName;

    private long fileSize;


    public Document(String id, String fileName, long fileSize){
        this.id = id;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return fileSize == document.fileSize &&
                Objects.equals(fileName, document.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, fileSize);
    }

    @Override
    public String toString() {
        return "Document{" +
                "fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                '}';
    }
}
