package com.SpotifyStrains.StrainsApp.service;

import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.data.library.CheckUsersSavedTracksRequest;
import se.michaelthelin.spotify.requests.data.library.GetCurrentUsersSavedAlbumsRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class LibraryService {
    private final SpotifyApi spotifyApi;

    private final UserService userService;

    public List<TrackSimplified> getTracksFromBarelyListenedAlbums() {
        GetCurrentUsersSavedAlbumsRequest getCurrentUsersSavedAlbumsRequest = spotifyApi.getCurrentUsersSavedAlbums().limit(50).build();
        List<TrackSimplified> playlistTracks = new ArrayList<>();
        try {
            Paging<SavedAlbum> resultAlbumsPaging = getCurrentUsersSavedAlbumsRequest.execute();
            SavedAlbum[] savedAlbums = resultAlbumsPaging.getItems();

            for (SavedAlbum album : savedAlbums) {

                List<TrackSimplified> tracks = checkForKnownAlbum(album);

                if (tracks == null) continue;

                playlistTracks.addAll(tracks);
            }
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            e.getMessage();
        }
        return playlistTracks;
    }

    private List<TrackSimplified> checkForKnownAlbum(SavedAlbum savedAlbum) {
        TrackSimplified[] albumTracks = savedAlbum.getAlbum().getTracks().getItems();
        String ids = Arrays.stream(albumTracks)
                .map(TrackSimplified::getId)
                .collect(Collectors.joining(","));

        int length = albumTracks.length;

        CheckUsersSavedTracksRequest checkUsersSavedTracksRequest =
                spotifyApi.checkUsersSavedTracks(ids).build();

        try {
            Boolean[] answers = checkUsersSavedTracksRequest.execute();
            int amount = (int) Arrays.stream(answers).filter(Boolean::booleanValue).count();

            if (amount > (0.35 * length)) return null;

            List<TrackSimplified> tracks = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                if (answers[i]) continue;

                tracks.add(albumTracks[i]);

            }
            return tracks;

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            e.getMessage();
            return null;
        }
    }
}

//        try {
//                Paging<SavedAlbum> resultAlbumsPaging = getCurrentUsersSavedAlbumsRequest.execute();
//        } catch (IOException | SpotifyWebApiException | ParseException e) {
//            e.printStackTrace();
//        }
