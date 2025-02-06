package org.example.template.Domain;

import java.time.LocalDateTime;

public class Show extends Entity<Integer> {
    private String name;
    private int durationMinutes;
    private LocalDateTime startDate;
    private int nrOfSeats;
    private LocalDateTime creationDate;

    public Show(String name, int durationMinutes, LocalDateTime startDate, int nrOfSeats, LocalDateTime creationDate) {
        this.name = name;
        this.durationMinutes = durationMinutes;
        this.startDate = startDate;
        this.nrOfSeats = nrOfSeats;
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public int getNrOfSeats() {
        return nrOfSeats;
    }

    public void setNrOfSeats(int nrOfSeats) {
        this.nrOfSeats = nrOfSeats;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return name+" | "+durationMinutes+" minutes"+" | "+startDate+" | "+nrOfSeats+" seats";
    }
}
