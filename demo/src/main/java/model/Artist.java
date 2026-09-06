package model;

import java.util.ArrayList;
import java.util.List;

public class Artist {

    private String id;
    private String name;
    private String nationality;

    private List<Track> tracks = new ArrayList<>();

    public Artist(String id, String name, String nationality) {

        this.id = id;
        this.name = name;
        this.nationality = nationality;

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

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

}