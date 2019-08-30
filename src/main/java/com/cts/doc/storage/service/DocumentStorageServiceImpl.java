package com.cts.doc.storage.service;

import com.cts.doc.storage.entity.Document;
import com.cts.doc.storage.exception.DocumentStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.HashMap;

@Service

public class DocumentStorageServiceImpl implements DocumentStorageService {

    private final Path docStorageLocation;

    private static HashMap<String, Document> docMap = new HashMap<String, Document>();

    @Autowired
    DocumentStorageServiceImpl(DocumentStorageProperties docStorageProperties) {
        this.docStorageLocation = Paths.get(docStorageProperties.getDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.docStorageLocation);
        } catch (Exception ex) {
            throw new DocumentStorageException("Could not create the directory where the files will be stored.", ex);
        }
    }


    public String createFile(MultipartFile document) {

        Document doc = new Document(getAlphaNumericString(), document.getOriginalFilename(), document.getSize());
        docMap.put(doc.getId(), doc);
        String docName = StringUtils.cleanPath(document.getOriginalFilename());

        try {
            Path targetLocation = this.docStorageLocation.resolve(docName);
            Files.copy(document.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return doc.getId();
        } catch (IOException ex) {
            throw new DocumentStorageException("Could not save the document " + docName + ". Please try again!", ex);
        }
    }

    // function to generate a random string of length n
    static String getAlphaNumericString()
    {
       int n = 20;

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }


    public Resource loadDoc(String docId) throws DocumentStorageException {

        Document doc = docMap.get(docId);
        if(doc == null){
            throw new DocumentStorageException("Document not found "+ docId);
        }
        String fileName = doc.getFileName();
        try {

            Path filePath = this.docStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new DocumentStorageException("Document not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new DocumentStorageException("Document not found " + fileName);
        }
    }

    public void updateDoc(String docId, String newData) throws DocumentStorageException {
        Document doc = docMap.get(docId);
        if(doc == null){
            throw new DocumentStorageException("Document not found "+ docId);
        }

        String fileName = doc.getFileName();

        try {
            Path targetLocation = this.docStorageLocation.resolve(fileName);
            System.out.println("Document location is " + targetLocation);
            Files.write(targetLocation, newData.getBytes(), StandardOpenOption.WRITE);
           // Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException ex) {
            throw new DocumentStorageException("Could not update the document " + fileName + ". Please try again!", ex);
        }
    }

    public void deleteDoc(String docId) throws DocumentStorageException {

         Document doc = docMap.get(docId);
         if(doc == null){
             throw new DocumentStorageException("Document not found "+ docId);
         }
        String fileName = doc.getFileName();
        try {
            Path targetLocation = this.docStorageLocation.resolve(fileName);
            Files.delete(targetLocation);

        } catch (IOException ex) {
            throw new DocumentStorageException("Could not delete the document " + fileName + ". Please try again!", ex);
        }

    }
}
