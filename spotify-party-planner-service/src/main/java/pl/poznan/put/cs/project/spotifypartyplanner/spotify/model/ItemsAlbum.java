package pl.poznan.put.cs.project.spotifypartyplanner.spotify.model;

import pl.poznan.put.cs.project.spotifypartyplanner.model.CoverImage;

import java.util.List;

public class ItemsAlbum extends SpotifyResponse {
    public String id;
    public List<ItemsArtist> artists;
    public String name;
    public List<CoverImage> images;
}
