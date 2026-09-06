package service;

import java.util.List;

import model.Artist;
import model.Track;
import repository.ArtistRepository;
import repository.TrackRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrackService {
   
    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private ArtistRepository artistRepository;

    public List<Track> getTracks(){
        return trackRepository.getTracks();
    }

    public void addTrack(String id, String title, String genre, String duration, String albumTitle, List<String> artistIds) {

        Track track = new Track(id, title, genre, duration, albumTitle);
        if (artistIds != null) {
            for (String artistId : artistIds) {
                if (artistId != null && !artistId.trim().isEmpty()) {
                    Artist artist = artistRepository.getArtistById(artistId.trim());

                    if (artist != null) {
                        track.getArtists().add(artist);
                        artist.getTracks().add(track);
                    }
                }
            }
        }
        trackRepository.addTrack(track);
    }

    public boolean removeTrack(String id) {
        Track track = trackRepository.getTrackById(id);
        if (track != null) {
            for (Artist a : track.getArtists()) {
                a.getTracks().remove(track);
            }
        }
        return trackRepository.removeById(id);
    }

}