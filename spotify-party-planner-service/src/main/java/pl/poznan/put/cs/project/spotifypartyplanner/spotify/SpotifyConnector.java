package pl.poznan.put.cs.project.spotifypartyplanner.spotify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import pl.poznan.put.cs.project.spotifypartyplanner.model.Album;
import pl.poznan.put.cs.project.spotifypartyplanner.model.Track;
import pl.poznan.put.cs.project.spotifypartyplanner.spotify.exception.SpotifyAuthorizationException;
import pl.poznan.put.cs.project.spotifypartyplanner.spotify.exception.SpotifyRecommendationsSeedSizeException;
import pl.poznan.put.cs.project.spotifypartyplanner.spotify.model.AuthorizationResponse;
import pl.poznan.put.cs.project.spotifypartyplanner.spotify.model.GenresSeedsResponse;
import pl.poznan.put.cs.project.spotifypartyplanner.spotify.model.ItemsArtist;
import pl.poznan.put.cs.project.spotifypartyplanner.spotify.model.SearchResponse;
import pl.poznan.put.cs.project.spotifypartyplanner.spotify.model.SpotifyPlaylistResponse;
import pl.poznan.put.cs.project.spotifypartyplanner.spotify.model.Tracks;
import pl.poznan.put.cs.project.spotifypartyplanner.spotify.model.TracksResponse;
import pl.poznan.put.cs.project.spotifypartyplanner.spotify.model.UserResponse;
import pl.poznan.put.cs.project.spotifypartyplanner.spotify.model.request.AbstractSpotifyRequest;
import pl.poznan.put.cs.project.spotifypartyplanner.spotify.model.request.PlaylistRequest;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.collect.Iterables.partition;
import static java.util.stream.StreamSupport.stream;

@Component
public class SpotifyConnector {

    private static final String AUTH_LINK = "https://accounts.spotify.com/api/token";
    private static final String API_LINK = "https://api.spotify.com/v1";

    private final RestTemplate restTemplate = new RestTemplateBuilder().build();
    private final String authorizationToken;

    public SpotifyConnector(@Value("${spotify.auth}") String authorizationToken) {
        this.authorizationToken = authorizationToken;
    }

    // TODO: @Cacheable
    public String authorize() throws SpotifyAuthorizationException {
        var headers = new HttpHeaders();
        headers.setBasicAuth(authorizationToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var map= new LinkedMultiValueMap<String, String>();
        map.add("grant_type", "client_credentials");

        var response = restTemplate.exchange(
                AUTH_LINK,
                HttpMethod.POST,
                new HttpEntity<>(map, headers),
                AuthorizationResponse.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return Objects.requireNonNull(response.getBody()).accessToken;
        }
        else {
            throw new SpotifyAuthorizationException(response.toString());
        }
    }

    public Stream<Track> search(String text) throws SpotifyAuthorizationException {
        var encodedQuery = URLEncoder.encode(text, StandardCharsets.UTF_8);
        return apiRequest(
                "/search?q=" + encodedQuery + "&type=track&market=PL",
                HttpMethod.GET,
                SearchResponse.class
        ).map(HttpEntity::getBody)
                .filter(Objects::nonNull)
                .map(SearchResponse::getTracks)
                .map(Tracks::getItems)
                .flatMap(Collection::stream)
                .map(i ->  new Track(
                        i.id,
                        i.name,
                        mapArtists(i.artists),
                        new Album(i.album.id, i.album.name, mapArtists(i.album.artists), i.album.images),
                        mapDuration(i.durationMs),
                        i.uri
                ));
    }

    public Optional<String> getDisplayname(
            String userId
    ) throws SpotifyAuthorizationException {
        return apiRequest(
                String.format("/users/%s", URLEncoder.encode(userId, StandardCharsets.UTF_8)),
                HttpMethod.GET,
                UserResponse.class
        ).map(HttpEntity::getBody)
                .filter(Objects::nonNull)
                .map(UserResponse::getHostname)
                .findFirst();
    }

    private static String mapArtists(List<ItemsArtist> artists) {
        return artists.stream()
                .map(a -> a.name)
                .collect(Collectors.joining(" & "));
    }

    private static String mapDuration(int ms) {
        int min = ms / 60_000;
        int s = (ms % 60_000) / 1000;
        return min + ":" + (s > 9 ? s : ("0" + s));
    }

    public Stream<String> getGenreSeeds() throws SpotifyAuthorizationException {
        return apiRequest(
                "/recommendations/available-genre-seeds",
                HttpMethod.GET,
                GenresSeedsResponse.class
        ).map(HttpEntity::getBody)
                .filter(Objects::nonNull)
                .map(GenresSeedsResponse::getGenres)
                .flatMap(Collection::stream);
    }

    public Stream<Track> getRecommendations(
            List<String> seedTracks, List<String> seedGenres, Map<String, Float> targets
    ) throws SpotifyAuthorizationException, SpotifyRecommendationsSeedSizeException {
        if (seedTracks.size() + seedGenres.size() > 5) {
            throw new SpotifyRecommendationsSeedSizeException();
        }
        var url = String.format(
                "/recommendations?seed_tracks%s&seed_genres%s&%s&limit=50&market=PL",
                seedTracks.isEmpty() ? "" : "=" + String.join(",", seedTracks),
                seedGenres.isEmpty() ? "" : "=" + String.join(",", seedGenres),
                targets.entrySet().stream().map(p -> p.getKey() + "=" + p.getValue()).collect(Collectors.joining("&"))
        );
        return apiRequest(
                url,
                HttpMethod.GET,
                TracksResponse.class
        ).map(HttpEntity::getBody)
                .filter(Objects::nonNull)
                .map(TracksResponse::getTracks)
                .flatMap(Collection::stream)
                .map(i ->  new Track(
                        i.id,
                        i.name,
                        mapArtists(i.artists),
                        new Album(i.album.id, i.album.name, mapArtists(i.album.artists), i.album.images),
                        mapDuration(i.durationMs),
                        i.uri
                ));
    }

    public Stream<Track> getTracksById(Set<String> trackIds) {
        if (trackIds.isEmpty()) {
            return Stream.empty();
        }

        return stream(partition(trackIds, 20).spliterator(), false)
                .flatMap(chunk -> safeApiRequest(
                        "/tracks?ids=" + String.join(",", chunk),
                        HttpMethod.GET,
                        TracksResponse.class
                ))
                .map(HttpEntity::getBody)
                .filter(Objects::nonNull)
                .map(TracksResponse::getTracks)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .map(i ->  new Track(
                        i.id,
                        i.name,
                        mapArtists(i.artists),
                        new Album(i.album.id, i.album.name, mapArtists(i.album.artists), i.album.images),
                        mapDuration(i.durationMs),
                        i.uri
                ));
    }

    public Optional<String> createSpotifyPlaylist(
            String userId, String token, String name, String description
    ) throws SpotifyAuthorizationException {
        var headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return apiRequest(
                String.format("/users/%s/playlists", URLEncoder.encode(userId, StandardCharsets.UTF_8)),
                HttpMethod.POST,
                new HttpEntity<>(new PlaylistRequest(name, description), headers),
                SpotifyPlaylistResponse.class
        ).map(HttpEntity::getBody)
                .filter(Objects::nonNull)
                .map(SpotifyPlaylistResponse::getId)
                .findFirst();
    }

    public void replaceTracksOnPlaylist(
            String playlistId, List <String> uris, String token
    ) throws SpotifyAuthorizationException {
        var headers = new HttpHeaders();
        headers.setBearerAuth(token);
        var url = String.format("/playlists/%s/tracks?uris=%s", playlistId, String.join(",", uris));
        apiRequest(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(headers),
                Void.class
        );
    }

    private <B> Stream<ResponseEntity<B>> apiRequest(
            String path, HttpMethod method, Class<B> responseType
    ) throws SpotifyAuthorizationException {
        return apiRequest(path, method, new HttpEntity<>(new HttpHeaders()), responseType);
    }

    private <B> Stream<ResponseEntity<B>> apiRequest(
            String path, HttpMethod method, HttpEntity<AbstractSpotifyRequest> request, Class<B> responseType
    ) throws SpotifyAuthorizationException {
        if (!request.getHeaders().containsKey("Authorization")) {
            var headers = new HttpHeaders();
            for(var h : request.getHeaders().entrySet()) {
                headers.addAll(h.getKey(), h.getValue());
            }
            headers.setBearerAuth(authorize());
            request = new HttpEntity<>(request.getBody(), headers);
        }
        return Stream.of(restTemplate.exchange(
                URI.create(API_LINK + path),
                method,
                request,
                responseType
        ));
    }

    private <B> Stream<ResponseEntity<B>> safeApiRequest(
            String path, HttpMethod method, Class<B> bodyType
    ) {
        try {
            return apiRequest(path, method, bodyType);
        } catch (SpotifyAuthorizationException e) {
            throw new RuntimeException(e);
        }
    }

    private final static Logger logger = LoggerFactory.getLogger(SpotifyConnector.class);
}
