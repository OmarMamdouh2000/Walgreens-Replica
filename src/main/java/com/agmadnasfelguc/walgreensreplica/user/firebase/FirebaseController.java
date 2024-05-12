package com.agmadnasfelguc.walgreensreplica.user.firebase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/photos")
public class FirebaseController {

    private final FirebaseService firebaseService;

    @Autowired
    public FirebaseController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @PostMapping("/upload")
    public String uploadPhoto(@RequestParam("id") String id, @RequestParam("file") MultipartFile file) throws IOException {
        return firebaseService.uploadPhoto(id, file);
    }


    @GetMapping("/photo")
    public String getPhotoUrl(@RequestParam("id") String id) {
        return firebaseService.getPhotoUrl(id);
    }

}
