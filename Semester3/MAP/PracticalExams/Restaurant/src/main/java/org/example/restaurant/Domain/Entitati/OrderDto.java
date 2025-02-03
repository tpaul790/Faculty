package org.example.restaurant.Domain.Entitati;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class OrderDto implements Comparable<OrderDto> {
    private int idMasa;
    private String numeProdus;
    private LocalDateTime date;

    public OrderDto(int idMasa, String numeProdus, LocalDateTime date) {
        this.idMasa = idMasa;
        this.numeProdus = numeProdus;
        this.date = date;
    }

    public String getNumeProdus() {
        return numeProdus;
    }

    public void setNumeProdus(String numeProdus) {
        this.numeProdus = numeProdus;
    }

    public int getIdMasa() {
        return idMasa;
    }

    public void setIdMasa(int idMasa) {
        this.idMasa = idMasa;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public int compareTo(@NotNull OrderDto o) {
        return this.date.compareTo(o.getDate());
    }
}
