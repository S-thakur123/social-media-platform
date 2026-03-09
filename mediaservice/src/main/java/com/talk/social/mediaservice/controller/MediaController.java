package com.talk.social.mediaservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.talk.social.mediaservice.service.StorageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

    private final StorageService storageService;
    
    public MediaController(StorageService storageService) {
		super();
		this.storageService = storageService;
	}

	@PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        // This calls the storage service we wrote earlier
        String fileName = storageService.uploadImage(file);
        return ResponseEntity.ok(fileName);
    }
	@GetMapping("/download/{fileName}")
    public ResponseEntity<String> getDownloadUrl(@PathVariable String fileName) {
        String url = storageService.getDownloadUrl(fileName);
        return ResponseEntity.ok(url);
    }
}