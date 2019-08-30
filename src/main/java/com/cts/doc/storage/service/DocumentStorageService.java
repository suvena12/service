package com.cts.doc.storage.service;

import com.cts.doc.storage.exception.DocumentStorageException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


public interface DocumentStorageService {

    public String createFile(MultipartFile doc) ;

    public Resource loadDoc(String docId) throws DocumentStorageException;

    public void updateDoc(String docId, MultipartFile doc) throws DocumentStorageException;

    public void deleteDoc(String docId) throws DocumentStorageException;



}
