package com.example;

import com.example.model.Artist;
import com.example.model.Track;
import com.example.services.IArtistService;
import com.example.services.ITrackService;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.ArrayList;
import java.util.List;

// NOTE: no @WebListener here on purpose — registered explicitly in web.xml
// instead, so we can guarantee it runs AFTER ContextLoaderListener.
public class DataInitializerListener implements ServletContextListener {

    private static final String[] NATIONALITIES = {"Colombia", "USA", "UK", "Argentina", "Spain"};
    private static final String[] GENRES = {"Rock", "Pop", "Jazz", "Reggaeton", "Classical"};

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        WebApplicationContext ctx =
                WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());

        if (ctx == null) {
            throw new IllegalStateException(
                "Spring WebApplicationContext not found. Check web.xml listener order: " +
                "ContextLoaderListener must be declared before DataInitializerListener.");
        }

        IArtistService artistService = ctx.getBean(IArtistService.class);
        ITrackService trackService = ctx.getBean(ITrackService.class);

        // 1. Create 10 artists, keep direct references (not just ids) so we can
        // link extra co-authors below without needing a getById on the service.
        List<Artist> artists = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            String nationality = NATIONALITIES[i % NATIONALITIES.length];
            artists.add(artistService.createArtist("Artist " + i, nationality));
        }

        // 2. Create 50 tracks, 5 per artist as sole primary author
        List<Track> tracks = new ArrayList<>();
        int trackCounter = 1;
        for (Artist artist : artists) {
            for (int j = 1; j <= 5; j++) {
                String genre = GENRES[trackCounter % GENRES.length];
                Track track = trackService.createTrack(
                        "Track " + trackCounter,
                        genre,
                        180 + (trackCounter % 60),
                        "Album " + trackCounter,
                        List.of(artist.getId())
                );
                tracks.add(track);
                trackCounter++;
            }
        }

        // 3. Demonstrate many-to-many: link 5 tracks to a SECOND artist as
        // co-author, on top of their existing primary artist. Using the model's
        // addTrack() directly here (not through the service) is fine — this is
        // bootstrap/seed code with direct access to already-created objects,
        // not a normal request-handling path.
        addCoAuthor(tracks.get(4), artists.get(2));   // Track 5  -> also Artist 3
        addCoAuthor(tracks.get(14), artists.get(7));  // Track 15 -> also Artist 8
        addCoAuthor(tracks.get(24), artists.get(0));  // Track 25 -> also Artist 1
        addCoAuthor(tracks.get(34), artists.get(9));  // Track 35 -> also Artist 10
        addCoAuthor(tracks.get(44), artists.get(4));  // Track 45 -> also Artist 5

        sce.getServletContext().log(
            "Seed data initialized: 10 artists, 50 tracks, 5 tracks with co-authors.");
    }

    private void addCoAuthor(Track track, Artist extraArtist) {
        extraArtist.addTrack(track); // keeps both sides of the relationship in sync
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // no cleanup needed for in-memory data
    }
}