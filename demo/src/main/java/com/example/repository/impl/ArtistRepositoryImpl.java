package com.example.repository.impl;

import com.example.model.Artist;
import com.example.repository.IArtistRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArtistRepositoryImpl implements IArtistRepository {

    private final List<Artist> artists = new ArrayList<>();
    private long nextId = 1;

    @Override
    public Artist create(Artist artist) {
        if (artist == null) {
            return null;
        }
        artist.setId(nextId);
        nextId++;
        artists.add(artist);
        return artist;
    }

    @Override
    public List<Artist> findAll() {
        return List.copyOf(artists);
    }

    @Override
    public Optional<Artist> findById(Long id) {
        return artists.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Artist> findByName(String name) {
        return artists.stream()
                .filter(a -> a.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public void deleteById(Long id) {
        artists.removeIf(a -> a.getId().equals(id));
    }
}