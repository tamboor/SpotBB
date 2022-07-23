package com.SpotifyStrains.StrainsApp.service;

import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.data.browse.GetRecommendationsRequest;
import se.michaelthelin.spotify.requests.data.library.CheckUsersSavedTracksRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import se.michaelthelin.spotify.requests.data.playlists.AddItemsToPlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscoveryService {

    private final SpotifyApi spotifyApi;

    public void generateNewPlaylist() {
        User currUser = getCurrentUser();
        Playlist playlist = createPlaylist(currUser.getId());
        GetUsersTopTracksRequest initialRequest = spotifyApi.getUsersTopTracks().build();

        List<String> addedTracks = new ArrayList<>();

        try {
            Paging<Track> tracksPaging = initialRequest.execute();
            Track[] tracks = tracksPaging.getItems();

            for (Track track : tracks) {
                GetRecommendationsRequest recommendationsRequest = spotifyApi.getRecommendations().seed_tracks(track.getId()).build();
                Recommendations recommendations = recommendationsRequest.execute();
                TrackSimplified[] trackRecommendations = recommendations.getTracks();

                String ids = "";
                for (TrackSimplified trackSimplified : trackRecommendations) {
                    ids += trackSimplified.getId() + ",";
                }
                ids = ids.substring(0, ids.length() - 1);

                CheckUsersSavedTracksRequest checkUsersSavedTracksRequest = spotifyApi.checkUsersSavedTracks(ids).build();
                Boolean[] checkTrackResults = checkUsersSavedTracksRequest.execute();

                for (int i = 0; i < trackRecommendations.length; i++){
                    if (checkTrackResults[i]){
                        addedTracks.add(trackRecommendations[i].getId());
                    }
                }
            }




//            AddItemsToPlaylistRequest addItemsToPlaylistRequest = spotifyApi.addItemsToPlaylist()

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            e.printStackTrace();
        }
    }

    public User getCurrentUser() {
        try {
            return spotifyApi.getCurrentUsersProfile().build().execute();
//            return getUsersProfileRequest.getId();
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

}
