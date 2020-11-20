package pl.poznan.put.cs.project.spotifypartyplanner.rest.model.response.event;

import pl.poznan.put.cs.project.spotifypartyplanner.model.event.Event;
import pl.poznan.put.cs.project.spotifypartyplanner.spotify.SpotifyConnector;

import java.time.Instant;

public class EventResponse {
    public String id;
    public String name;
    public String location;
    public Instant date;
    public String hostId;
    public String hostname;
    public PlaylistResponse playlist;
    public boolean open;

    public static EventResponse fromEvent(final Event event, SpotifyConnector spotifyConnector) {
        var response = new EventResponse();
        response.id = event.getId();
        response.name = event.getName();
        response.location = event.getLocation();
        response.date = event.getDate();
        response.hostId = event.getHostId();
        response.playlist = PlaylistResponse.fromPlaylist(event.getPlaylist(), spotifyConnector);
        response.hostname = event.getHostname();
        response.open = event.isOpen();
        return  response;
    }
}
