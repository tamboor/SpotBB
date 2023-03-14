package com.SpotifyStrains.StrainsApp.controller;

import com.SpotifyStrains.StrainsApp.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
public class Endpoints {

    private final AuthenticationService authService;

    @ShellMethod
    public void login(){
        System.out.println(authService.getAuthURI());
    }


}
