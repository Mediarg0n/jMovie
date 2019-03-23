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

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author bpall
 */
@Entity
//@IdClass(SeasonId.class)
public class Season implements Serializable {
    
    
    @Id
    @GeneratedValue
    private long id;
    
    @Size(min = 1, max = 50, message = "Eine Serien kann höchstens 50 Episoden haben.")
    private int nr;
    
    @ManyToOne
    @NotNull(message = "Die Episode muss einer Season zugeordet werden")
    private Serie serie;
    
    @Column(length = 50)
    @NotNull(message = "Der Titel darf nicht leer sein.")
    @Size(min = 1, max = 30, message = "Der Titel muss zwischen ein und 30 Zeichen lang sein.")
    private String title;
    
    @NotNull(message = "Das Erscheinungsdatum der Episode darf nicht leer sein.")
    private Date releaseDate;
    
    
    @OneToMany(mappedBy = "season",fetch = FetchType.LAZY)
    private List<Episode> episodes;
    
    
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private WatchStatus status = WatchStatus.NOT_WATCHED;
   

    
    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public Season(){
        
    }

    public Season(long id, int nr, Serie serie, List<Episode> episodes) {
        this.id = id;
        this.nr = nr;
        this.serie = serie;
        this.episodes = episodes;
    }

    
    
    
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setter und Getter">

   public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }
    
     public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }
    
     public WatchStatus getStatus() {
        return status;
    }

    public void setStatus(WatchStatus status) {
        this.status = status;
    }
    
    
    //</editor-fold>

    
}
