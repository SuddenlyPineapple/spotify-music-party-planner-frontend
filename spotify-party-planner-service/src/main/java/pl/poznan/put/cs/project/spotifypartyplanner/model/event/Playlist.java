package pl.poznan.put.cs.project.spotifypartyplanner.model.event;

import java.util.List;

import static java.util.Collections.emptyList;


public class Playlist {
    private String name = null;
    private String spotifyId = null;
    private Suggestions suggestions = new Suggestions();
    private List<String> tracks = emptyList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public Suggestions getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(Suggestions suggestions) {
        this.suggestions = suggestions;
    }

    public List<String> getTracks() {
        return tracks;
    }

    public void setTracks(List<String> tracks) {
        this.tracks = tracks;
    }
}
