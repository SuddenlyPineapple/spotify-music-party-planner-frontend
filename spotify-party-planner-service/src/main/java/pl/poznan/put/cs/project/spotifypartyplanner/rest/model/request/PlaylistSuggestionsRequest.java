package pl.poznan.put.cs.project.spotifypartyplanner.rest.model.request;

import java.util.Collections;
import java.util.List;

public class PlaylistSuggestionsRequest {
    public List<String> genres = Collections.emptyList();
    public List<String> tracks = Collections.emptyList();
}
