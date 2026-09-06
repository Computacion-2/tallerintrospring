package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import repository.ArtistRepository;
import repository.TrackRepository;
import service.ArtistService;
import service.TrackService;

@Configuration
public class AppConfig {

    @Bean
    public ArtistRepository artistRepository() {
        return new ArtistRepository();
    }

    @Bean
    public TrackRepository trackRepository() {
        return new TrackRepository();
    }

    @Bean(initMethod = "initData")
    public ArtistService artistService(ArtistRepository artistRepository, TrackRepository trackRepository) {
        ArtistService service = new ArtistService();
        service.setArtistRepository(artistRepository);
        service.setTrackRepository(trackRepository);
        return service;
    }

    @Bean
    public TrackService trackService(TrackRepository trackRepository, ArtistRepository artistRepository) {
        TrackService service = new TrackService();
        service.setTrackRepository(trackRepository);
        service.setArtistRepository(artistRepository);
        return service;
    }

}
