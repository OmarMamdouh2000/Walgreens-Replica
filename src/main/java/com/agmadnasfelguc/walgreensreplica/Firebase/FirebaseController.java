import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/photos")
public class FirebaseController {

    private final FirebaseService storageService;

    @Autowired
    public FirebaseController(FirebaseService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload/{id}")
    public String uploadPhoto(@PathVariable String id, @RequestParam("file") MultipartFile file) throws IOException {
        return storageService.uploadPhoto(id, file);
    }

    @GetMapping("/{id}")
    public String getPhotoUrl(@PathVariable String id) {
        return storageService.getPhotoUrl(id);
    }
}
