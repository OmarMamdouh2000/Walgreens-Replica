package com.agmadnasfelguc.walgreensreplica.Firebase;

import com.google.cloud.storage.Storage;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Bean
    public static FirebaseApp initializeFirebaseApp() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("C://Users//Eng.Gamal//Desktop//Walgreens-Replica//src//main//resources//google-services.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public static Storage initializeStorage() throws IOException {
        FirebaseApp app = FirebaseApp.getInstance();
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        StorageOptions storageOptions = StorageOptions.newBuilder().setCredentials(credentials).build();
        return storageOptions.getService();
    }
}
