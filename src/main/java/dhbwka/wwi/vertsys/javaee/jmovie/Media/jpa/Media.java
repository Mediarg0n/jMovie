/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.jmovie.Media.jpa;

import dhbwka.wwi.vertsys.javaee.jmovie.common.jpa.User;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author D070429
 */
@Entity
public class Media implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "media_ids")
    @TableGenerator(name = "media_ids", initialValue = 0, allocationSize = 50)
    private long id;

    @ManyToOne
    @NotNull(message = "Die Media muss einem Benutzer zugeordnet werden.")
    private User owner;

    @ManyToMany
    private List<Genere> genere;

    @Column(length = 50)
    @NotNull(message = "Der Titel darf nicht leer sein.")
    @Size(min = 1, max = 30, message = "Der Titel muss zwischen ein und 30 Zeichen lang sein.")
    private String title;

    @Lob
    @NotNull
    private String description;
    
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private WatchStatus status = WatchStatus.NOT_WATCHED;
    
    
    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public Media() {
    }

    public Media(User owner, List<Genere> genere, String title, String description) {
        this.owner = owner;
        this.genere = genere;
        this.title = title;
        this.description = description;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setter und Getter">
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Genere> getGenere() {
        return genere;
    }

    public void setGenere(List<Genere> genere) {
        this.genere = genere;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    public WatchStatus getStatus() {
        return status;
    }

    public void setStatus(WatchStatus status) {
        this.status = status;
    }
    //</editor-fold>
    
}
