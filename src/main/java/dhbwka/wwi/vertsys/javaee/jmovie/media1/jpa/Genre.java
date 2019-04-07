/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.jmovie.media.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author D070429
 */
@Entity
public class Genre implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "genre_ids")
    @TableGenerator(name = "genre_ids", initialValue = 0, allocationSize = 50)
    private long id;

    @Column(length = 30)
    @NotNull(message = "Der Name darf nicht leer sein.")
    @Size(min = 3, max = 30, message = "Der Name muss zwischen drei und 30 Zeichen lang sein.")
    private String name;

    @ManyToMany(mappedBy = "genres",fetch = FetchType.LAZY)
    List<Media> medias = new ArrayList<>();
    
    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public Genre() {
    }

    public Genre(String name) {
        this.name = name;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Setter und Getter">
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Media> getMedias() {
        return medias;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }
    //</editor-fold>
    
    
}
