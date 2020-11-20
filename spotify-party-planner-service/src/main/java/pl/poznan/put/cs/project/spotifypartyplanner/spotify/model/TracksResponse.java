package pl.poznan.put.cs.project.spotifypartyplanner.spotify.model;

import java.util.List;

public class TracksResponse extends SpotifyResponse {
    public List<Item> tracks;

    public List<Item> getTracks() {
        return tracks;
    }
}
