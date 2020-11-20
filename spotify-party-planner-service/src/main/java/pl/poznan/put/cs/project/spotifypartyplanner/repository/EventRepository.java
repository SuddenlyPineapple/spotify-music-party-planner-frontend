package pl.poznan.put.cs.project.spotifypartyplanner.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.poznan.put.cs.project.spotifypartyplanner.model.event.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> findByHostId(String hostId);

    Optional<Event> findById(String Id);
}
