package com.example.demo.cassandraFirebase;

import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.StorageException;
import com.google.auth.oauth2.GoogleCredentials;

import java.net.URL;
import java.util.concurrent.TimeUnit;


@Service
public class FirebaseService {

    @Autowired
    private Storage storage;
    //private final FirebaseConfig firebaseConfig;
    
    public FirebaseService()
    {
    	
    }

    public FirebaseService(FirebaseConfig firebaseConfig) {
        /*this.firebaseConfig = firebaseConfig;
        try {

            this.storage = firebaseConfig.initializeStorage();
        } catch (IOException e) {
            // Handle the exception, e.g., logging or throwing a runtime exception
            e.printStackTrace();
            //System.out.println("Haneen?");// Or log it
            throw new RuntimeException("Failed to initialize Firebase Storage", e);
        }*/
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

//    public String getPhotoUrl(String id) {
//        // Assuming the photo URL is constructed based on the ID
//        return "https://storage.googleapis.com/walgreens-replica.appspot.com/photos/" + id ;
//    }
    public String getPhotoUrl(String id) {
        String photoURL = "";
        try {
            BlobId blobId = BlobId.of("walgreens-replica.appspot.com", "photos/" + id);
            Blob blob = storage.get(blobId);
            
            if (blob != null) {
                URL signedUrl = storage.signUrl(
                    BlobInfo.newBuilder(blobId).build(),
                    7, // URL expiration in days
                    TimeUnit.DAYS,
                    Storage.SignUrlOption.withV4Signature()
                );
                photoURL = signedUrl.toString();
            } else {
                throw new StorageException(404, "Photo not found");
            }
        } catch (StorageException e) {
            //logger.error("Failed to get signed URL", e);
        }
        return photoURL;
    }
}
