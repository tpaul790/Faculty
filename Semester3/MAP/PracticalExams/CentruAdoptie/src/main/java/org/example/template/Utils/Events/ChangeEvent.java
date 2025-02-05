package org.example.template.Utils.Events;

import org.example.template.Domain.Animal;

/**
 * Am bevoie dde locatie pentru a-i notifica doar pe aia din aceeasi locatie cu mine
 * Am nevoie de numele centrului care o facut request ul pentru a putea completa labelul din notificare
 */
public class ChangeEvent implements Event {
    private EventType eventType;
    private String locatie;
    private Animal data;
    private String numeCentru;

    public ChangeEvent(EventType eventType,String locatie, Animal data) {
        this.eventType = eventType;
        this.locatie = locatie;
        this.data = data;
    }

    public String getNumeCentru(){
        return numeCentru;
    }

    public void setNumeCentru(String numeCentru){
        this.numeCentru = numeCentru;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Animal getData() {
        return data;
    }

    public String getLocatie() {
        return locatie;
    }
}
