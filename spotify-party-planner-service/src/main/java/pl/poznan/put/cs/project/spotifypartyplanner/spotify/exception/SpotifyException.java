package pl.poznan.put.cs.project.spotifypartyplanner.spotify.exception;

public abstract class SpotifyException extends Exception {
    protected SpotifyException(String message) {
        super(message);
    }
}
