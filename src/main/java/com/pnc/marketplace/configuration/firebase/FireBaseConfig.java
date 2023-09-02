package com.pnc.marketplace.configuration.firebase;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class FireBaseConfig {

    /**
     * The above function initializes the Firebase app with the provided service account credentials
     * and sets the storage bucket.
     */
    @PostConstruct
    public void initialize() throws IOException {

        InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("pnckey.json");
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("pncf-392002.appspot.com")
                .build();

                try {
                        FirebaseApp.initializeApp(options);
                } catch (Exception e) {
                    log.error("Error {}",e.getMessage());
                }
    }
}
