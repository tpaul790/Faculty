package org.example.template.Domain;

import java.time.LocalDate;

public class FiltruDto {
    private String from;
    private String to;
    private LocalDate landingDate;
    private int pageSize;
    private int pageNumber;

    public FiltruDto(String from, String to,LocalDate day, int pageSize, int pageNumber) {
        this.from = from;
        this.to = to;
        this.landingDate = day;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public LocalDate getLandingDate() {
        return landingDate;
    }

    public void setLandingDate(LocalDate landingDate) {
        this.landingDate = landingDate;
    }
}
