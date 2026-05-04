package com.lifeops.userservice.service;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.lifeops.userservice.dto.AuthenticatedUser;
import com.lifeops.userservice.exception.InvalidAuthTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;

@Service
public class GoogleTokenVerifierService {
    private final String googleClientId;

    public GoogleTokenVerifierService(
            @Value("${lifeops.security.google.client-id}")  String googleClientId) {
        this.googleClientId = googleClientId;
    }

    public AuthenticatedUser verify(String idTokenValue){
        if(!StringUtils.hasText(idTokenValue)){
            throw new InvalidAuthTokenException("Missing Google ID token");
        }

        if(!StringUtils.hasText(googleClientId)){
            throw new InvalidAuthTokenException("Google client ID is not configured");
        }

        try{
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance()
            )
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenValue);

            if(idToken == null){
                throw new InvalidAuthTokenException("Invalid Google ID token");
            }
            GoogleIdToken.Payload payload = idToken.getPayload();

            return new AuthenticatedUser(
                    payload.getSubject(),
                    payload.get("name").toString(),
                    payload.getEmail(),
                    "GOOGLE"
            );
        }catch (InvalidAuthTokenException exception){
            throw exception;
        } catch (Exception exception) {
            throw new InvalidAuthTokenException("Unable to verify Google ID token");
        }
    }
}
