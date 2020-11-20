package pl.poznan.put.cs.project.spotifypartyplanner.rest.model.response;

import pl.poznan.put.cs.project.spotifypartyplanner.model.event.Event;
import pl.poznan.put.cs.project.spotifypartyplanner.rest.model.response.event.EventResponse;
import pl.poznan.put.cs.project.spotifypartyplanner.spotify.SpotifyConnector;

import java.util.List;
import java.util.stream.Collectors;


public class UserEventsResponse {
    public String userId;
    public List<EventResponse> events;

    public static UserEventsResponse build(String userId, List<Event> events, SpotifyConnector spotifyConnector) {
        var response = new UserEventsResponse();
        response.userId = userId;
        response.events = events.stream()
                .map(e -> EventResponse.fromEvent(e, spotifyConnector))
                .collect(Collectors.toList());
        return response;
    }
}
