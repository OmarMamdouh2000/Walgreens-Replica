import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class FirebaseService {

    private final Storage storage;

    public FirebaseService() {
        this.storage = FirebaseConfig.initializeFirebaseApp();
    }

    public String uploadPhoto(String id, MultipartFile file) throws IOException {
        // Generate a filename based on the provided ID
        String fileName = "photos/" + id ;

        // Upload the file to Firebase Storage
        Blob blob = storage.create(Blob.newBuilder(fileName, file.getInputStream()).build());

        // Get the URL of the uploaded file
        return blob.getMediaLink();
    }

    public String getPhotoUrl(String id) {
        // Assuming the photo URL is constructed based on the ID
        return "https://storage.googleapis.com/walgreens-replica.appspot.com/photos/" + id ;
    }
}
