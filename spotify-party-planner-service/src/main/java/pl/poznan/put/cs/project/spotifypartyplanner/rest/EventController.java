package pl.poznan.put.cs.project.spotifypartyplanner.rest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.poznan.put.cs.project.spotifypartyplanner.model.event.Event;
import pl.poznan.put.cs.project.spotifypartyplanner.rest.model.request.PlaylistSuggestionsRequest;
import pl.poznan.put.cs.project.spotifypartyplanner.rest.model.request.PlaylistSynchronizeRequest;
import pl.poznan.put.cs.project.spotifypartyplanner.rest.model.request.PlaylistTracksRequest;
import pl.poznan.put.cs.project.spotifypartyplanner.rest.model.response.UserEventsResponse;
import pl.poznan.put.cs.project.spotifypartyplanner.rest.model.response.event.EventResponse;
import pl.poznan.put.cs.project.spotifypartyplanner.service.EventsService;
import pl.poznan.put.cs.project.spotifypartyplanner.spotify.SpotifyConnector;
import pl.poznan.put.cs.project.spotifypartyplanner.spotify.exception.SpotifyException;

import java.net.URI;
import java.util.NoSuchElementException;

import static pl.poznan.put.cs.project.spotifypartyplanner.rest.model.response.event.EventResponse.fromEvent;

@RestController
@RequestMapping("events")
@CrossOrigin
public class EventController {
    private final EventsService service;
    private final SpotifyConnector spotifyConnector;

    public EventController(EventsService service, SpotifyConnector spotifyConnector) {
        this.service = service;
        this.spotifyConnector = spotifyConnector;
    }

    @GetMapping(value = "/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EventResponse> getEvent(
            @PathVariable String eventId
    ) {
        var event = service.getEventById(eventId);
        if (event.isEmpty()) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok(fromEvent(event.get(), spotifyConnector));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, params = "userId")
    ResponseEntity<UserEventsResponse> getUserEvents(
            @RequestParam String userId
    ) {
        var events = service.getEventsByUser(userId);
        if (events.isEmpty()) return ResponseEntity.noContent().build();
        else return ResponseEntity.ok(UserEventsResponse.build(userId, events, spotifyConnector));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EventResponse> postEvent(
            @RequestBody Event request
    ) {
        var toCreate = new Event(request.getName(), request.getLocation(), request.getDate(), request.getHostId());
        var event = service.addEvent(toCreate);
        return ResponseEntity.created(URI.create("/events/" + event.getId()))
                .body(fromEvent(event, spotifyConnector));
    }

    @DeleteMapping(value = "/{eventId}")
    ResponseEntity<Void> deleteEvent(
            @PathVariable String eventId
    ) {
        service.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{eventId}/suggestions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventResponse> putSuggestions(
            @RequestBody PlaylistSuggestionsRequest request,
            @PathVariable String eventId
    ) throws SpotifyException {
        try {
            var updatedEvent = service.addGuestsSuggestions(eventId, request.genres, request.tracks);
            return ResponseEntity.ok(fromEvent(updatedEvent, spotifyConnector));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/{eventId}/suggestions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventResponse> deleteSuggestions(
            @RequestBody PlaylistSuggestionsRequest request,
            @PathVariable String eventId
    ) throws SpotifyException {
        try {
            var updatedEvent = service.removeGuestsSuggestions(eventId, request.genres, request.tracks);
            return ResponseEntity.ok(fromEvent(updatedEvent, spotifyConnector));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/{eventId}/tracks", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventResponse> putTracks(
            @RequestBody PlaylistTracksRequest request,
            @PathVariable String eventId
    ) {
        try {
            var updatedEvent = service.addTracks(eventId, request.trackIds);
            return ResponseEntity.ok(fromEvent(updatedEvent, spotifyConnector));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/{eventId}/tracks", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventResponse> deleteTracks(
            @RequestBody PlaylistTracksRequest request,
            @PathVariable String eventId
    ) {
        try {
            var updatedEvent = service.removeTracks(eventId, request.trackIds);
            return ResponseEntity.ok(fromEvent(updatedEvent, spotifyConnector));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/{eventId}/synchronize", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> putSynchronize(
            @RequestBody PlaylistSynchronizeRequest request,
            @PathVariable String eventId
    ) throws SpotifyException {
        service.synchronizePlaylistWithSpotify(eventId, request.userToken);
        return ResponseEntity.ok().build();
    }
}
