package com.agmadnasfelguc.walgreensreplica.user.firebase;

import com.google.cloud.storage.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


@Service
public class FirebaseService {

    @Autowired
    private Storage storage;
    Logger logger = LoggerFactory.getLogger(FirebaseService.class);
    //private final FirebaseConfig firebaseConfig;

    public FirebaseService(FirebaseConfig firebaseConfig) {

    }

    public String uploadPhoto(String id, MultipartFile file) throws IOException {
        // Generate a filename based on the provided ID
        String fileName = "photos/" + id;

        // Define the BlobId with the bucket name and the generated filename
        BlobId blobId = BlobId.of("walgreens-replica.appspot.com", fileName);

        // Create BlobInfo with the generated BlobId and set the content type
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        // Upload the file to Firebase Storage
        Blob blob = storage.create(blobInfo, file.getBytes());

        // Get the URL of the uploaded file
        return blob.getMediaLink();
    }

    public String getPhotoUrl(String id) {
        // Assuming the photo URL is constructed based on the ID
        String photoURL = "";
        try{
            photoURL =  "https://storage.googleapis.com/walgreens-replica.appspot.com/photos/" + id ;
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        return photoURL;

    }
}
