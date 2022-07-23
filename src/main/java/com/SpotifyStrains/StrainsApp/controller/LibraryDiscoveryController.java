package com.SpotifyStrains.StrainsApp.controller;

import com.SpotifyStrains.StrainsApp.service.LibraryService;
import com.SpotifyStrains.StrainsApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.model_objects.specification.Playlist;

@RestController
@RequestMapping("/lib")
@RequiredArgsConstructor
public class LibraryDiscoveryController {

    private final UserService userService;
    private final LibraryService libraryService;

    @PostMapping("/generate")
    public void generatePlaylist() {
        Playlist playlist = userService.generateEmptyPlaylist("temp :)");
        userService.addTracksToPlaylist(playlist, libraryService.getTracksFromBarelyListenedAlbums());
    }
}
