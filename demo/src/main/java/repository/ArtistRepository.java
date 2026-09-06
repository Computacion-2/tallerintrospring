package repository;

import model.Artist;

import java.util.ArrayList;
import java.util.List;

public class ArtistRepository {
    
    private List<Artist> artists = new ArrayList<>();

    public void addArtist(Artist artist) {
        artists.add(artist);
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public Artist getArtistById(String id) {
        if (id == null) return null;
        for (Artist artist : artists) {
            if (artist.getId().equalsIgnoreCase(id.trim())) {
                return artist;
            }
        }
        return null;
    }

    public boolean removeById(String id) {
        if (id == null) return false;
        return artists.removeIf(artist -> artist.getId().equalsIgnoreCase(id.trim()));
    }

}
