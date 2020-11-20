package pl.poznan.put.cs.project.spotifypartyplanner.rest.model.response;

import pl.poznan.put.cs.project.spotifypartyplanner.model.Track;

import java.util.List;

public class ExploreSearchResponse {
    public List<Track> tracks;
    public String query;

    public ExploreSearchResponse(List<Track> tracks, String query) {
        this.tracks = tracks;
        this.query = query;
    }
}
