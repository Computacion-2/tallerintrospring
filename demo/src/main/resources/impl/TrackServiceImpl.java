package com.example.services.impl;

import com.example.model.Artist;
import com.example.model.Track;
import com.example.repository.IArtistRepository;
import com.example.repository.ITrackRepository;
import com.example.services.ITrackService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class TrackServiceImpl implements ITrackService {

    private final ITrackRepository trackRepository;
    private final IArtistRepository artistRepository;

    public TrackServiceImpl(ITrackRepository trackRepository, IArtistRepository artistRepository) {
        this.trackRepository = trackRepository;
        this.artistRepository = artistRepository;
    }

    @Override
    public List<Track> getAllTracks() {
        return trackRepository.findAll();
    }

    @Override
    public Track createTrack(String title, String genre, int durationSeconds, String albumTitle, List<Long> artistIds) {
        if (artistIds == null || artistIds.isEmpty()) {
            throw new IllegalArgumentException("A track needs at least one author artist id");
        }

        Track track = new Track(null, title, genre, durationSeconds, albumTitle);
        Track created = trackRepository.create(track);

        for (Long artistId : artistIds) {
            Artist artist = artistRepository.findById(artistId)
                    .orElseThrow(() -> new NoSuchElementException("No artist with id " + artistId));
            artist.addTrack(created);
        }

        return created;
    }

    @Override
    public void deleteTrack(Long id) {
        Track track = trackRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No track with id " + id));

        new ArrayList<>(track.getArtists()).forEach(track::removeArtist);
        trackRepository.deleteById(id);
    }
}