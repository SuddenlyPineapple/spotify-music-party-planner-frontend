package pl.poznan.put.cs.project.spotifypartyplanner.model.event;


import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
public class Event {
    @Id
    private String id;
    private String name;
    private String location;
    private Instant date;
    private String hostId;
    private String hostname;
    private Playlist playlist;
    private boolean open;

    @JsonCreator
    @PersistenceConstructor
    public Event(String id, String name, String location, Instant date, String hostId, String hostname, Playlist playlist, boolean open) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.hostId = hostId;
        this.hostname = hostname;
        this.playlist = playlist;
        this.open = open;
    }

    public Event(String name, String location, Instant date, String hostId) {
        this.id = null;
        this.name = name;
        this.hostId = hostId;
        this.hostname = null;
        this.location = location;
        this.date = date;
        this.playlist = new Playlist();
        this.open = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
