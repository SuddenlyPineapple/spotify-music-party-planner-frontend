package pl.poznan.put.cs.project.spotifypartyplanner.rest.model.response.event;

import pl.poznan.put.cs.project.spotifypartyplanner.model.event.Suggestions;
import pl.poznan.put.cs.project.spotifypartyplanner.spotify.SpotifyConnector;

public class SuggestionsResponse {
    public SuggestionsFromResponse fromGuests = new SuggestionsFromResponse();
    public SuggestionsFromResponse fromRecommendations = new SuggestionsFromResponse();

    public static SuggestionsResponse fromSuggestions(final Suggestions suggestions, SpotifyConnector spotifyConnector) {
        var response = new SuggestionsResponse();
        response.fromGuests = SuggestionsFromResponse.fromSuggestionsFrom(suggestions.getFromGuests(), spotifyConnector);
        response.fromRecommendations = SuggestionsFromResponse.fromSuggestionsFrom(suggestions.getFromRecommendations(), spotifyConnector);
        return response;
    }
}
