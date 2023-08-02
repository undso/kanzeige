package de.friedrichs.kanzeige.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Bild {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    private Long id;
    @Column
    @Getter
    @Setter
    private byte[] content;
    @Column
    @Getter
    @Setter
    private String url;

    public Bild() {
    }

    public Bild(String url) {
        this.url = url;
    }
}
