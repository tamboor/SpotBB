package com.SpotifyStrains.StrainsApp.controller;

import com.SpotifyStrains.StrainsApp.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;


    @GetMapping("/loginPage")
    public String getLoginPage() {
        return authenticationService.getAuthURI().toString();
    }

    //TODO: handle redirect better
    @GetMapping("/setCode")
    public boolean setCode(@RequestParam String code) {
        return authenticationService.setTokensByCode(code);
    }


}
