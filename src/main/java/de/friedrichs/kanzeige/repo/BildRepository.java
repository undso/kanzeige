package de.friedrichs.kanzeige.repo;

import de.friedrichs.kanzeige.model.Bild;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BildRepository extends JpaRepository<Bild, Long> {
}