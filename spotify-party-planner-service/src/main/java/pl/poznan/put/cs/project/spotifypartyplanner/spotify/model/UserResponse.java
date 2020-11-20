package pl.poznan.put.cs.project.spotifypartyplanner.spotify.model;

public class UserResponse extends SpotifyResponse {
    public String display_name;

    public String getHostname() {
        return display_name;
    }
}
