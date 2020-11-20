package pl.poznan.put.cs.project.spotifypartyplanner.spotify.model;

import java.util.List;

public class Item extends SpotifyResponse {
    public String id;
    public String name;
    public ItemsAlbum album;
    public List<ItemsArtist> artists;
    public int durationMs;
    public String uri;
}
