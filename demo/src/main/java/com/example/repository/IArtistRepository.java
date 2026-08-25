package com.example.repository;

import com.example.model.Artist;
import java.util.List;
import java.util.Optional;

public interface IArtistRepository {
    Artist create(Artist artist);
    List<Artist> findAll();
    Optional<Artist> findById(Long id);
    Optional<Artist> findByName(String name);
    void deleteById(Long id);
}