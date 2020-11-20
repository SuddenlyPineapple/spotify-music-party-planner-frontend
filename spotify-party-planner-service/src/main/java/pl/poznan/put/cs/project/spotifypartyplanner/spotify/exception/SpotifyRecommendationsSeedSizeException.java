package pl.poznan.put.cs.project.spotifypartyplanner.spotify.exception;

public class SpotifyRecommendationsSeedSizeException extends SpotifyException {
    public SpotifyRecommendationsSeedSizeException() {
        super("Seeds total size must be in <0, 5>");
    }
}
