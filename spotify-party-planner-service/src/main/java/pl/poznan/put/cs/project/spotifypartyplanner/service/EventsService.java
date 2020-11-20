package pl.poznan.put.cs.project.spotifypartyplanner.service;

import org.springframework.stereotype.Service;
import pl.poznan.put.cs.project.spotifypartyplanner.model.Track;
import pl.poznan.put.cs.project.spotifypartyplanner.model.event.Event;
import pl.poznan.put.cs.project.spotifypartyplanner.model.event.Playlist;
import pl.poznan.put.cs.project.spotifypartyplanner.model.event.SuggestionsFrom;
import pl.poznan.put.cs.project.spotifypartyplanner.repository.EventRepository;
import pl.poznan.put.cs.project.spotifypartyplanner.spotify.SpotifyConnector;
import pl.poznan.put.cs.project.spotifypartyplanner.spotify.exception.SpotifyException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.Stream.empty;
import static pl.poznan.put.cs.project.spotifypartyplanner.spotify.SpotifyHelper.emptyIfAuthorizationErrorOrThrow;

@Service
public class EventsService {

    private final EventRepository repository;
    private final SpotifyConnector spotifyConnector;

    private static final Map<String, Float> defaultTunableParameters = Collections.singletonMap("max_liveness", .3f);

    public EventsService(EventRepository repository, SpotifyConnector spotifyConnector) {
        this.repository = repository;
        this.spotifyConnector = spotifyConnector;
    }

    public List<Event> getEventsByUser(String userId) {
        return repository.findByHostId(userId);
    }

    public Event addEvent(Event event) {
        fetchHostname(event);
        return repository.insert(event);
    }

    private void fetchHostname(Event event) {
        Optional.of(event)
                .map(Event::getHostId)
                .flatMap(hostId -> {
                    try {
                        return spotifyConnector.getDisplayname(hostId);
                    } catch (SpotifyException ignored) {
                        return Optional.empty();
                    }
                })
                .ifPresent(event::setHostname);
    }

    public Optional<Event> getEventById(String eventId) {
        return repository.findById(eventId);
    }

    public void deleteEvent(String eventId) { repository.deleteById(eventId); }

    public Event addGuestsSuggestions(String eventId, List<String> genres, List<String> tracks) throws NoSuchElementException, SpotifyException {
        var event = repository.findById(eventId).orElseThrow(NoSuchElementException::new);
        addGuestsSuggestions(event.getPlaylist(), genres, tracks);
        updateTracksRecommendations(event);
        return repository.save(event);
    }

    private void addGuestsSuggestions(Playlist playlist, List<String> genres, List<String> tracks) throws NoSuchElementException {
        addSuggestionsFrom(playlist.getSuggestions().getFromGuests().getTracks(), tracks);
        addSuggestionsFrom(playlist.getSuggestions().getFromGuests().getGenres(), genres);
        updateTracksWithGuestsSuggestions(playlist, tracks);
    }

    private void addSuggestionsFrom(Map<String, Integer> data, List<String> source) {
        source.forEach(i -> data.put(i, data.computeIfAbsent(i, k -> 0)+1));
    }

    private void updateTracksWithGuestsSuggestions(Playlist playlist, List<String> suggestions) {
        var updated = new HashSet<String>();
        updated.addAll(playlist.getTracks());
        updated.addAll(suggestions);
        playlist.setTracks(new ArrayList<>(updated));
    }

    public Event removeGuestsSuggestions(String eventId, List<String> genres, List<String> tracks) throws NoSuchElementException, SpotifyException {
        var event = repository.findById(eventId).orElseThrow(NoSuchElementException::new);
        removeGuestsSuggestions(event.getPlaylist(), genres, tracks);
        updateTracksRecommendations(event);
        return repository.save(event);
    }

    private void removeGuestsSuggestions(Playlist playlist, List<String> genres, List<String> tracks) throws NoSuchElementException {
        removeSuggestionsFrom(playlist.getSuggestions().getFromGuests().getTracks(), tracks);
        removeSuggestionsFrom(playlist.getSuggestions().getFromGuests().getGenres(), genres);
        var tracksToRemove = playlist.getSuggestions()
                .getFromGuests()
                .getTracks()
                .entrySet()
                .stream()
                .filter(t -> t.getValue() <= 0)
                .map(Map.Entry::getKey)
                .collect(toUnmodifiableList());
        removeTracksFromPlaylist(playlist, tracksToRemove);
    }

    private void removeSuggestionsFrom(Map<String, Integer> data, List<String> source) {
        source.forEach(i -> data.computeIfPresent(i, (key, val) -> val > 0 ? val-1 : 0));
    }

    private void removeTracksFromPlaylist(Playlist playlist, List<String> tracksIds) {
        playlist.getTracks().removeAll(tracksIds);
    }

    public void updateTracksRecommendations(Event event) throws SpotifyException {
        var updated = getTracksRecommendations(event).collect(toMap(Track::getId, t -> 1));
        event.getPlaylist().getSuggestions().getFromRecommendations().setTracks(updated);
    }

    private Stream<Track> getTracksRecommendations(Event event) throws SpotifyException {
        var fromGuests = event.getPlaylist().getSuggestions().getFromGuests();
        var currentTracks = new HashSet<>(event.getPlaylist().getTracks());
        return getTracksRecommendations(fromGuests)
                .filter(t -> !currentTracks.contains(t.getId()))
                .limit(24);
    }

    private Stream<Track> getTracksRecommendations(SuggestionsFrom fromGuests) throws SpotifyException {
        var genres = getTopPreferences(fromGuests.getGenres());
        if (genres.size() == 5) {
            return emptyIfAuthorizationErrorOrThrow(
                    () -> spotifyConnector.getRecommendations(emptyList(), genres, defaultTunableParameters)
            );
        } else if (fromGuests.getTracks().size() > 0) {
            var tracks = getTopPreferences(fromGuests.getTracks(), 5 - genres.size());
            return emptyIfAuthorizationErrorOrThrow(
                    () -> spotifyConnector.getRecommendations(tracks, genres, defaultTunableParameters)
            );
        } else if (genres.size() > 0) {
            return emptyIfAuthorizationErrorOrThrow(
                    () -> spotifyConnector.getRecommendations(emptyList(), genres, defaultTunableParameters)
            );
        } else {
            return empty();
        }
    }

    private static List<String> getTopPreferences(Map<String, Integer> preferences) {
        return getTopPreferences(preferences, 5);
    }

    private static List<String> getTopPreferences(Map<String, Integer> preferences, int max) {
        return preferences.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .limit(max)
                .collect(toUnmodifiableList());
    }

    public Event addTracks(String eventId, List<String> trackIds) {
        return getEventById(eventId).map(e -> {
            var tracks = new HashSet<>(e.getPlaylist().getTracks());
            tracks.addAll(trackIds);
            e.getPlaylist().setTracks(new ArrayList<>(tracks));
            addGuestsSuggestions(e.getPlaylist(), emptyList(), trackIds);
            return e;
        }).map(e -> {
            try {
                updateTracksRecommendations(e);
            } catch (SpotifyException ignored) {}
            return e;
        }).map(repository::save).orElseThrow(NoSuchElementException::new);
    }

    public Event removeTracks(String eventId, List<String> trackIds) {
        return getEventById(eventId).map(e -> {
            var tracks = new HashSet<>(e.getPlaylist().getTracks());
            tracks.removeAll(trackIds);
            e.getPlaylist().setTracks(new ArrayList<>(tracks));
            return e;
        }).map(e -> {
            try {
                updateTracksRecommendations(e);
            } catch (SpotifyException ignored) {}
            return e;
        }).map(repository::save).orElseThrow(NoSuchElementException::new);
    }

    public void synchronizePlaylistWithSpotify(String eventId, String userToken) throws SpotifyException {
        var event = getEventById(eventId).orElseThrow(NoSuchElementException::new);

        if (event.getPlaylist().getSpotifyId() == null) {
            var name = String.format("%s Playlist", event.getName());
            var playlistId = spotifyConnector.createSpotifyPlaylist(
                    event.getHostId(), userToken, name, ""
            ).orElseThrow(NullPointerException::new);
            event.getPlaylist().setSpotifyId(playlistId);
            event.getPlaylist().setName(name);
        }

        var tracks = Optional.of(event)
                .map(Event::getPlaylist)
                .map(Playlist::getTracks)
                .map(HashSet::new)
                .map(spotifyConnector::getTracksById)
                .orElse(empty())
                .map(Track::getUri)
                .collect(toUnmodifiableList());

        if (tracks.size() > 0) {
            assert event.getPlaylist() != null;
            spotifyConnector.replaceTracksOnPlaylist(event.getPlaylist().getSpotifyId(), tracks, userToken);
        }

        repository.save(event);
    }

}
