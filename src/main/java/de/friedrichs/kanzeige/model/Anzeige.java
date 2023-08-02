package de.friedrichs.kanzeige.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@ToString
public class Anzeige {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    private Long id;
    @Column
    @Getter
    @Setter
    private String titel;
    @Column
    @Getter
    @Setter
    private Long vorgaenger;
    @Column
    @Getter
    @Setter
    private int preis;
    @Column
    @Getter
    @Setter
    private String beschreibung;
    @OneToMany
    @Setter
    private List<Bild> bilder;
    @Temporal(TemporalType.DATE)
    @Getter
    @Setter
    private LocalDate ablaufdatum;
    @Getter
    @Setter
    private String url;
    @Getter
    @Setter
    private boolean verhandlungsbasis;
    @Getter
    @Setter
    private boolean versand;
    @Setter
    @ElementCollection
    private List<String> breadcrumps;
    @Setter
    @ElementCollection
    private Map<String, String> details;

    public List<Bild> getBilder() {
        if (this.bilder == null) {
            this.bilder = new ArrayList<>();
        }
        return bilder;
    }

    public List<String> getBreadcrumps() {
        if (this.breadcrumps == null) {
            this.breadcrumps = new ArrayList<>();
        }
        return breadcrumps;
    }

    public Map<String, String> getDetails() {
        if (this.details == null) {
            this.details = new HashMap<>();
        }
        return details;
    }
}