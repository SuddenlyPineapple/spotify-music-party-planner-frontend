package pl.poznan.put.cs.project.spotifypartyplanner.model.event;

import java.util.HashMap;
import java.util.Map;

public class SuggestionsFrom {
    private Map<String, Integer> genres = new HashMap<>();
    private Map<String, Integer> tracks = new HashMap<>();

    public Map<String, Integer> getGenres() {
        return genres;
    }

    public void setGenres(HashMap<String, Integer> genres) {
        this.genres = genres;
    }

    public Map<String, Integer> getTracks() {
        return tracks;
    }

    public void setTracks(Map<String, Integer> tracks) {
        this.tracks = tracks;
    }
}
