package pl.poznan.put.cs.project.spotifypartyplanner.spotify.exception;

public class SpotifyAuthorizationException extends SpotifyException {
    public SpotifyAuthorizationException(String msg) {
        super("exception during spotify auth " + msg);
    }
}
