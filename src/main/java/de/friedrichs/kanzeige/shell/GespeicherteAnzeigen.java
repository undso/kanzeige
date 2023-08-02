package de.friedrichs.kanzeige.shell;

import de.friedrichs.kanzeige.model.Anzeige;
import de.friedrichs.kanzeige.repo.AnzeigeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;

@Slf4j
@ShellComponent
public class GespeicherteAnzeigen {

    @Autowired
    private AnzeigeRepository repository;

    @ShellMethod(value = "Alle gespeicherten Anzeigen auflisten", key = "alleGespeichertenAnzeigen")
    public void alleGespeichertenAnzeigen() {
        List<Anzeige> alleAnzeigen = repository.findAll(Sort.by(Sort.Order.asc("ablaufdatum")));
        log.info("{} Anzeigen gefunden", alleAnzeigen.size());
        alleAnzeigen.forEach(anzeige -> log.info("{}: {} - {}", anzeige.getId(), anzeige.getPreis(), anzeige.getTitel()));
    }
}
