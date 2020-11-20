package pl.poznan.put.cs.project.spotifypartyplanner.service;

import org.springframework.stereotype.Service;
import pl.poznan.put.cs.project.spotifypartyplanner.model.Track;
import pl.poznan.put.cs.project.spotifypartyplanner.spotify.SpotifyConnector;

import java.util.stream.Stream;

import static pl.poznan.put.cs.project.spotifypartyplanner.spotify.SpotifyHelper.emptyIfAuthorizationError;

@Service
public class ExploreService {

    private final SpotifyConnector spotifyConnector;

    public ExploreService(SpotifyConnector spotifyConnector) {
        this.spotifyConnector = spotifyConnector;
    }

    public Stream<Track> search(String text) {
        return emptyIfAuthorizationError(() -> spotifyConnector.search(text));
    }

    public Stream<String> getGenres() {
        return emptyIfAuthorizationError(spotifyConnector::getGenreSeeds);
    }
}
