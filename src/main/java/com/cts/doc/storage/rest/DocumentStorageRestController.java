package com.cts.doc.storage.rest;


import com.cts.doc.storage.exception.DocumentStorageException;
import com.cts.doc.storage.service.DocumentStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/storage")
public class DocumentStorageRestController {

    private DocumentStorageService docSer;


    @Autowired
    public DocumentStorageRestController(DocumentStorageService theDocSer) {
        docSer= theDocSer;
    }

    @PostMapping("/documents")
    public ResponseEntity<String> createDoc(@RequestParam("file") MultipartFile file ){
        String fileId = docSer.createFile(file);

        return new ResponseEntity(fileId, HttpStatus.CREATED);
    }

    @GetMapping("/documents/{id}")
    public ResponseEntity<Resource> getDoc(@PathVariable String id){
        Resource resource;
        try {
            resource = docSer.loadDoc(id);
        } catch (DocumentStorageException ex){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(resource, HttpStatus.OK);
    }

    @PutMapping("/documents/{id}")
    public ResponseEntity updateDoc(@PathVariable String id, @RequestParam("file") MultipartFile file){
        try {
            docSer.updateDoc(id, file);
        }  catch (DocumentStorageException ex){
            new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/documents/{id}")
    public ResponseEntity delDoc(@PathVariable String id){
        try {
            docSer.deleteDoc(id);
        } catch (DocumentStorageException ex){
            new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }





}
