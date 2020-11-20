package pl.poznan.put.cs.project.spotifypartyplanner.spotify;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.poznan.put.cs.project.spotifypartyplanner.spotify.exception.SpotifyAuthorizationException;
import pl.poznan.put.cs.project.spotifypartyplanner.spotify.exception.SpotifyException;

import java.util.stream.Stream;

public abstract class SpotifyHelper {
    private SpotifyHelper() {}

    public static <B> Stream<B> emptyIfAuthorizationError(SpotifyRequest<B> request) {
        try {
            return request.execute();
        } catch (SpotifyAuthorizationException e) {
            logger.error(e.getMessage());
            return Stream.empty();
        }
    }

    public static <B> Stream<B> emptyIfAuthorizationErrorOrThrow(SpotifyRequestOrThrow<B> request) throws SpotifyException {
        try {
            return request.execute();
        } catch (SpotifyAuthorizationException e) {
            logger.error(e.getMessage());
            return Stream.empty();
        }
    }

    @FunctionalInterface
    public interface SpotifyRequest<B>  {
        Stream<B> execute() throws SpotifyAuthorizationException;
    }

    @FunctionalInterface
    public interface SpotifyRequestOrThrow<B>  {
        Stream<B> execute() throws SpotifyException;
    }

    private static Logger logger = LoggerFactory.getLogger(SpotifyHelper.class);
}
