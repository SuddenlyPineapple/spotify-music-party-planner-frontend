package pl.poznan.put.cs.project.spotifypartyplanner.spotify.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemsArtist {
    public String name;
}
