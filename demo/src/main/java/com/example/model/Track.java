package com.example.model;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Collections;

public class Track {
    private Long id;
    private String title;
    private String genre;
    private int duration; // seconds
    private String albumTitle;

    private final Set<Artist> artists = new LinkedHashSet<>();

    public Track() {
    }

    public Track(Long id, String title, String genre, int duration, String albumTitle) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.albumTitle = albumTitle;
    }

    
    public void addArtist(Artist artist) {
        if (artist == null) return;
        boolean added = this.artists.add(artist);
        if (added) {
            artist.linkFromTrack(this);
        }
    }

    public void removeArtist(Artist artist) {
        if (artist == null) return;
        boolean removed = this.artists.remove(artist);
        if (removed) {
            artist.unlinkFromTrack(this);
        }
    }

    
    void linkArtist(Artist artist) {
        this.artists.add(artist);
    }

    void unlinkArtist(Artist artist) {
        this.artists.remove(artist);
    }

    public Set<Artist> getArtists() {
        return Collections.unmodifiableSet(artists);
    }

    

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getAlbumTitle() { return albumTitle; }
    public void setAlbumTitle(String albumTitle) { this.albumTitle = albumTitle; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Track)) return false;
        Track track = (Track) o;
        return id != null && id.equals(track.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}