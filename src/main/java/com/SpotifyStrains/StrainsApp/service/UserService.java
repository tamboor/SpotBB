package com.SpotifyStrains.StrainsApp.service;

import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.special.SnapshotResult;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.data.playlists.AddItemsToPlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final SpotifyApi spotifyApi;

    public Playlist generateEmptyPlaylist(String name) {
        User currUser = getCurrentUser();
        return createPlaylist(currUser.getId());
    }

    public User getCurrentUser() {
        try {
            return spotifyApi.getCurrentUsersProfile().build().execute();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            e.getMessage();
            return null;
        }
    }

    public Playlist createPlaylist(String userID) {
        try {
            CreatePlaylistRequest createPlaylistRequest = spotifyApi.createPlaylist(userID, "TEMP").build();
            return createPlaylistRequest.execute();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            e.getMessage();
            return null;
        }
    }

    public void addTracksToPlaylist(Playlist playlist, List<TrackSimplified> tracksToAdd) {
        int allTracksLength = tracksToAdd.size();
        int leftAmount = allTracksLength;
        int offset = 0;
        final int limit = allTracksLength >= 80 ? 80 : allTracksLength;


        while (leftAmount > 0) {
            String[] uris = tracksToAdd.subList(offset, offset + (leftAmount < limit ? leftAmount : limit)).stream().map(track -> track.getUri()).toArray(String[]::new);

            AddItemsToPlaylistRequest addItemsToPlaylistRequest =
                    spotifyApi.addItemsToPlaylist(playlist.getId(), uris).build();

            try {
                addItemsToPlaylistRequest.execute();
                leftAmount -= limit;
                offset += limit;
            } catch (IOException | SpotifyWebApiException | ParseException e) {
                e.getMessage();
            }
        }
    }

}
