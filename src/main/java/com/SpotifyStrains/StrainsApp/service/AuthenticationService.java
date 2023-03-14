package com.SpotifyStrains.StrainsApp.service;

import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

@Service
public class AuthenticationService {
    //TODO: change to global variable
    private final String scopes = "playlist-modify-public playlist-read-private playlist-modify-private user-library-read user-library-modify";

    //TODO: add logger
//    @Autowired
//    private final Logger logger;

    @Autowired
    private final SpotifyApi spotifyApi;
    private final AuthorizationCodeUriRequest authorizationCodeUriRequest;

    public AuthenticationService(SpotifyApi spotifyApi) {
        this.spotifyApi = spotifyApi;

        this.authorizationCodeUriRequest = this.spotifyApi.authorizationCodeUri()
                .scope(scopes)
                .build();
    }

    public URI getAuthURI() {
        return authorizationCodeUriRequest.execute();
    }

    public boolean setTokensByCode(String code) {

        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code).build();

        try {
            AuthorizationCodeCredentials credentials = authorizationCodeRequest.execute();
            spotifyApi.setAccessToken(credentials.getAccessToken());
            spotifyApi.setRefreshToken(credentials.getRefreshToken());
            return true;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            e.getMessage();
            return false;
        }
    }
}
