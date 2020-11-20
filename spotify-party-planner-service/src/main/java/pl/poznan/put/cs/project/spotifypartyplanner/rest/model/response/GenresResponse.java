package pl.poznan.put.cs.project.spotifypartyplanner.rest.model.response;

import java.util.List;

public class GenresResponse {
    public List<String> genres;

    public GenresResponse(List<String> genres) {
        this.genres = genres;
    }
}
