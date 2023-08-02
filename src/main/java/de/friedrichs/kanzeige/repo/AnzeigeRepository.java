package de.friedrichs.kanzeige.repo;

import de.friedrichs.kanzeige.model.Anzeige;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnzeigeRepository extends JpaRepository<Anzeige, Long> {
}