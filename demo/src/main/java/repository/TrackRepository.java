package repository;

import model.Track;

import java.util.ArrayList;
import java.util.List;

public class TrackRepository {
    
    private List<Track> tracks = new ArrayList<>();

    public void addTrack(Track track) {
        tracks.add(track);
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public Track getTrackById(String id) {
        if (id == null) return null;
        for (Track track : tracks) {
            if (track.getId().equalsIgnoreCase(id.trim())) {
                return track;
            }
        }
        return null;
    }

    public boolean removeById(String id) {
        if (id == null) return false;
        return tracks.removeIf(track -> track.getId().equalsIgnoreCase(id.trim()));
    }

}
