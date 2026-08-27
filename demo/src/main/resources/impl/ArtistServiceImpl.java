package com.example.services.impl;

import com.example.model.Artist;
import com.example.repository.IArtistRepository;
import com.example.services.IArtistService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ArtistServiceImpl implements IArtistService {

    private final IArtistRepository artistRepository;

    public ArtistServiceImpl(IArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    @Override
    public Artist createArtist(String name, String nationality) {
        if (artistRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("An artist named '" + name + "' already exists");
        }
        Artist artist = new Artist(null, name, nationality);
        return artistRepository.create(artist);
    }

    @Override
    public Artist getArtistWithTracksByName(String name) {
        return artistRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("No artist named '" + name + "'"));
    }

    @Override
    public void deleteArtist(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No artist with id " + id));

        new ArrayList<>(artist.getTracks()).forEach(artist::removeTrack);
        artistRepository.deleteById(id);
    }
}