package com.muhammet.controller;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
@CrossOrigin(origins = "*",methods = {RequestMethod.POST,RequestMethod.GET})
public class MediaController {
    @Autowired
    private Storage storage;

    @PostMapping(value = "/add-storage-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadAvatarFile(@RequestParam("file")MultipartFile file) throws IOException {
            String UUID = java.util.UUID.randomUUID().toString();
               storage.create(
                BlobInfo.newBuilder("java-boost-14", "avatars/"+UUID+".png").build(),
                file.getInputStream()
        );
    }

    @PostMapping(value = "/add-storage-post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadPostFile(@RequestParam("file")MultipartFile file) throws IOException {
        String UUID = java.util.UUID.randomUUID().toString();
        storage.create(
                BlobInfo.newBuilder("java-boost-14", "post-photos/"+UUID+".png").build(),
                file.getInputStream()
        );
    }

}
