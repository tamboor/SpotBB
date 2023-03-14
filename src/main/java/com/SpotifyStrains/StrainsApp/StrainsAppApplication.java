package com.SpotifyStrains.StrainsApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import se.michaelthelin.spotify.SpotifyApi;

import java.net.URI;

@SpringBootApplication
public class StrainsAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(StrainsAppApplication.class, args);
	}



	@Bean
	public SpotifyApi getSpotifyApi(){

		return SpotifyApi.builder()
				//TODO: set this outside of intellij
				.setClientId(System.getenv("SPOTIFY_CLIENT_ID"))
				.setClientSecret(System.getenv("SPOTIFY_SECRET"))
				//TODO: move this
				.setRedirectUri(URI.create("http://localhost:8080/auth/setCode"))
				.build();
	}

}
