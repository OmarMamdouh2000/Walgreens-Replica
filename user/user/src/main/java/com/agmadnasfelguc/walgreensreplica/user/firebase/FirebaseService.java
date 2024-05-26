package com.agmadnasfelguc.walgreensreplica.user.firebase;

import com.google.cloud.storage.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;


@Service
public class FirebaseService {

    @Autowired
    private Storage storage;
    Logger logger = LoggerFactory.getLogger(FirebaseService.class);

    public FirebaseService(FirebaseConfig firebaseConfig) {

    }

    public String uploadPhoto(String id, MultipartFile file) throws IOException {
        String fileName = "photos/" + id;
        BlobId blobId = BlobId.of("walgreens-replica.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();
        Blob blob = storage.create(blobInfo, file.getBytes());
        return blob.getMediaLink();
    }

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
                logger.info("Signed URL: " + photoURL);
            } else {
                throw new StorageException(404, "Photo not found");
            }
        } catch (StorageException e) {
            logger.error("Failed to get signed URL", e);
        }
        return photoURL;
    }

    public boolean deleteImage(String id) {
        BlobId blobId = BlobId.of("walgreens-replica.appspot.com", "photos/" + id);
        boolean deleted = storage.delete(blobId);
        if (deleted) {
            logger.info("Image deleted successfully");
        } else {
            logger.error("Failed to delete image");
        }
        return deleted;
    }
}
