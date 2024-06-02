package com.example.demo.cassandraFirebase;

import com.google.cloud.storage.Storage;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class FirebaseConfig {

    /*@Bean
    public static FirebaseApp initializeFirebaseApp() throws IOException {
        System.out.println("Omar is saying hi");
        InputStream serviceAccount = FirebaseConfig.class.getClassLoader().getResourceAsStream("google-services.json");
        System.out.println(serviceAccount);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        System.out.println("Haneen btheb messi");
        return FirebaseApp.initializeApp(options);
    }*/

    @Bean
    public static Storage initializeStorage() throws IOException {
        System.out.println("Ana Sara?");
        //FirebaseApp app = initializeFirebaseApp();
        Path path = Paths.get("app/resources/google-services.json");
        ///app/resources/google-services.json
        GoogleCredentials credentials = GoogleCredentials.fromStream(Files.newInputStream(path));
        System.out.println("credentials:"+ credentials);

        StorageOptions storageOptions = StorageOptions.newBuilder()
                .setCredentials(credentials).build();
        return storageOptions.getService();
    }

    /*public static Storage initializeStorage() throws IOException {
        FirebaseApp app = initializeFirebaseApp();


        // Use FirebaseApp's configuration for Google Cloud Storage
        StorageOptions storageOptions = StorageOptions.newBuilder()
                .setCredentials(app.getCredential(GoogleCredentials.class))
                .build();

        // Return the Storage service
        return storageOptions.getService();
    }*/
}