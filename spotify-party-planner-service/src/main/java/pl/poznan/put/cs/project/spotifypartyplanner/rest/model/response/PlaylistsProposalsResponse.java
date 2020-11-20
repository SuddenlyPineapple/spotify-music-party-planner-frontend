package pl.poznan.put.cs.project.spotifypartyplanner.rest.model.response;

import pl.poznan.put.cs.project.spotifypartyplanner.model.Track;

import java.util.List;

public class PlaylistsProposalsResponse {
    public List<Track> tracks;

    public PlaylistsProposalsResponse(List<Track> tracks) {
        this.tracks = tracks;
    }
}
