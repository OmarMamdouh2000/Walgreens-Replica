package com.agmadnasfelguc.walgreensreplica.user.firebase;

import com.google.cloud.storage.Storage;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Bean
    public static Storage initializeStorage() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(FirebaseConfig.class.getClassLoader().getResourceAsStream("google-services.json"));


        StorageOptions storageOptions = StorageOptions.newBuilder()
                .setCredentials(credentials).build();
        return storageOptions.getService();
    }
}
