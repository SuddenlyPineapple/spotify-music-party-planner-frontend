package pl.poznan.put.cs.project.spotifypartyplanner.rest.model.response.event;

import pl.poznan.put.cs.project.spotifypartyplanner.model.Track;
import pl.poznan.put.cs.project.spotifypartyplanner.model.event.SuggestionsFrom;
import pl.poznan.put.cs.project.spotifypartyplanner.spotify.SpotifyConnector;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static pl.poznan.put.cs.project.spotifypartyplanner.spotify.SpotifyHelper.emptyIfAuthorizationError;

public class SuggestionsFromResponse {
    public List<SuggestionsElementResponse<String>> genres = emptyList();
    public List<SuggestionsElementResponse<Track>> tracks = emptyList();

    public static SuggestionsFromResponse fromSuggestionsFrom(final SuggestionsFrom suggestionsFrom, SpotifyConnector spotifyConnector) {
        var response = new SuggestionsFromResponse();
        response.genres = SuggestionsElementResponse.fromMap(suggestionsFrom.getGenres(), id -> id);

        Set<Track> tracks = emptyIfAuthorizationError(
                () -> spotifyConnector.getTracksById(suggestionsFrom.getTracks().keySet())
        ).collect(Collectors.toSet());
        response.tracks = SuggestionsElementResponse.fromMap(
                suggestionsFrom.getTracks(),
                id -> tracks.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(new Track(id))
        );
        return response;
    }
}
