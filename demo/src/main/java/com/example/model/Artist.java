package com.example.model;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Collections;

public class Artist {
    private Long id;
    private String name;
    private String nationality;

    private final Set<Track> tracks = new LinkedHashSet<>();

    public Artist() {
    }

    public Artist(Long id, String name, String nationality) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
    }

    
    public void addTrack(Track track) {
        if (track == null) return;
        boolean added = this.tracks.add(track);
        if (added) {
            track.linkArtist(this); // keep the other side in sync
        }
    }

    public void removeTrack(Track track) {
        if (track == null) return;
        boolean removed = this.tracks.remove(track);
        if (removed) {
            track.unlinkArtist(this);
        }
    }

    
    void linkFromTrack(Track track) {
        this.tracks.add(track);
    }

    void unlinkFromTrack(Track track) {
        this.tracks.remove(track);
    }

    public Set<Track> getTracks() {
        return Collections.unmodifiableSet(tracks); // read-only outside
    }

    

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artist)) return false;
        Artist artist = (Artist) o;
        return id != null && id.equals(artist.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}