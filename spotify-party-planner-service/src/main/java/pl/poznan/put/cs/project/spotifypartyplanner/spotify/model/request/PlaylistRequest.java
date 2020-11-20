package pl.poznan.put.cs.project.spotifypartyplanner.spotify.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlaylistRequest extends AbstractSpotifyRequest {
    public String name;
    public String description;
    public boolean collaborative = false;
    @JsonProperty("public")
    public boolean isPublic = true;

    public PlaylistRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
