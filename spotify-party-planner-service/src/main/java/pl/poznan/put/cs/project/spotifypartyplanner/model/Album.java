package pl.poznan.put.cs.project.spotifypartyplanner.model;


import java.util.List;

public class Album {
    private String id;
    private String name;
    private String artist;
    private List<CoverImage> img;

    public Album() {
    }

    public Album(String id, String name, String artist, List<CoverImage> img) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.img = img;
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

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public List<CoverImage> getImg() {
        return img;
    }

    public void setImg(List<CoverImage> img) {
        this.img = img;
    }
}
