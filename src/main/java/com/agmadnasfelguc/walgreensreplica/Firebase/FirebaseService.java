package com.agmadnasfelguc.walgreensreplica.Firebase;

import com.google.cloud.storage.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


@Service
public class FirebaseService {

    private final Storage storage;
    private final FirebaseConfig firebaseConfig;

    public FirebaseService(FirebaseConfig firebaseConfig) {
        this.firebaseConfig = firebaseConfig;
        try {

            this.storage = firebaseConfig.initializeStorage();
        } catch (IOException e) {
            // Handle the exception, e.g., logging or throwing a runtime exception
            e.printStackTrace(); // Or log it
            throw new RuntimeException("Failed to initialize Firebase Storage", e);
        }
    }

    /*public String uploadPhoto(String id, MultipartFile file) throws IOException {
        // Generate a filename based on the provided ID
        String fileName = "photos/" + id ;

        // Upload the file to Firebase Storage
        Blob blob = storage.create(Blob.newBuilder(fileName, file.getInputStream()).build());

        // Get the URL of the uploaded file
        return blob.getMediaLink();
    }*/

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
        return "https://storage.googleapis.com/walgreens-replica.appspot.com/photos/" + id ;
    }
}
