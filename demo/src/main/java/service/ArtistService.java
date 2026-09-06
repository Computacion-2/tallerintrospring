package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import model.Artist;
import model.Track;
import repository.ArtistRepository;
import repository.TrackRepository;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private TrackRepository trackRepository;

    public List<Artist> getArtists(){
        return artistRepository.getArtists();
    }

    public void addArtist(String id, String name, String nationality) {

        Artist artist = new Artist(id, name, nationality);
        artistRepository.addArtist(artist);

    }

    public Artist getArtistByName(String name) {
        for (Artist artist : artistRepository.getArtists()) {
            if (artist.getName().equalsIgnoreCase(name)) {
                return artist;
            }
        }
        return null;
    }

    @PostConstruct
    public void initData() {
        for (int i = 1; i <= 10; i++) {
            Artist artist = new Artist("ART-" + i, "Artista " + i, "Colombia");
            artistRepository.addArtist(artist);

            for (int j = 1; j <= 5; j++) {
                int trackNum = ((i - 1) * 5) + j;
                Track track = new Track("TRK-" + trackNum, "Cancion " + trackNum, "Rock", "3:30", "Album " + i);
                artist.getTracks().add(track);
                track.getArtists().add(artist);
                trackRepository.addTrack(track);
            }
        }
    }

    public boolean removeArtist(String id) {
        Artist artist = artistRepository.getArtistById(id);
        if (artist != null) {
            for (Track t : artist.getTracks()) {
                t.getArtists().remove(artist);
            }
        }
        return artistRepository.removeById(id);
    }

}