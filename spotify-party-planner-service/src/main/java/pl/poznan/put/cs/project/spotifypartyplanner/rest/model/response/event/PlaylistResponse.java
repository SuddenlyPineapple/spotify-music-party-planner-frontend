package pl.poznan.put.cs.project.spotifypartyplanner.rest.model.response.event;

import pl.poznan.put.cs.project.spotifypartyplanner.model.Track;
import pl.poznan.put.cs.project.spotifypartyplanner.model.event.Playlist;
import pl.poznan.put.cs.project.spotifypartyplanner.spotify.SpotifyConnector;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static pl.poznan.put.cs.project.spotifypartyplanner.spotify.SpotifyHelper.emptyIfAuthorizationError;

public class PlaylistResponse {
    public String name = null;
    public String spotifyId = null;
    public SuggestionsResponse suggestions = new SuggestionsResponse();
    public List<Track> tracks = emptyList();

    public static PlaylistResponse fromPlaylist(final Playlist playlist, SpotifyConnector spotifyConnector) {
        Set<Track> tracks = emptyIfAuthorizationError(
                () -> spotifyConnector.getTracksById(new HashSet<>(playlist.getTracks()))
        ).collect(Collectors.toSet());

        var response = new PlaylistResponse();
        response.name = playlist.getName();
        response.spotifyId = playlist.getName();
        response.suggestions = SuggestionsResponse.fromSuggestions(playlist.getSuggestions(), spotifyConnector);
        response.tracks = playlist.getTracks()
                .stream()
                .map(id -> tracks.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(new Track(id)))
                .collect(Collectors.toList());
        return response;
    }
}
