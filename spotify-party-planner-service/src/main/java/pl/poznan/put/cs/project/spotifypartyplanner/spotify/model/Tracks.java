package pl.poznan.put.cs.project.spotifypartyplanner.spotify.model;

import java.util.List;

public class Tracks extends SpotifyResponse {
    public List<Item> items;
    public List<Item> getItems() {
        return items;
    }
}
