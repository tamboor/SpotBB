package com.SpotifyStrains.StrainsApp.controller;

import com.SpotifyStrains.StrainsApp.service.AuthenticationService;
import com.SpotifyStrains.StrainsApp.service.LibraryService;
import com.SpotifyStrains.StrainsApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import se.michaelthelin.spotify.model_objects.specification.Playlist;

@ShellComponent
@RequiredArgsConstructor
public class Endpoints {

    private final AuthenticationService authService;

    private final UserService userService;
    private final LibraryService libraryService;

    @ShellMethod
    public void login(){
        System.out.println(authService.getAuthURI());
    }

    @ShellMethod(value = "generate a playlist of the albums." , key = "getp")
    public void generatePlaylist(){
        Playlist playlist = userService.generateEmptyPlaylist("temp :)");
        userService.addTracksToPlaylist(playlist, libraryService.getTracksFromBarelyListenedAlbums());
    }


}
