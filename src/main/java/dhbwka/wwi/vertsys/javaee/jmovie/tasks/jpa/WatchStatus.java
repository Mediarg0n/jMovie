/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.jmovie.tasks.jpa;

/**
 *
 * @author D070429
 */
public enum WatchStatus {
    
    
    NOT_WATCHED,NOT_COMPLETLY_WATCHED, WATCHED,STOPPED_WATCHING;

    /**
     * Bezeichnung ermitteln
     *
     * @return Bezeichnung
     */
    public String getLabel() {
        switch (this) {
            case NOT_WATCHED:
                return "Noch nicht angeschaut";
            case NOT_COMPLETLY_WATCHED:
                return "Nocht nicht ganz angeschaut";
            case WATCHED:
                return "Vollständig angeschaut";
            case STOPPED_WATCHING:
                return "Aufgehört zu schauen";
            default:
                return this.toString();
        }
    }
    
}
