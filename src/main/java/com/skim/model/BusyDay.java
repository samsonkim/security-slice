package com.skim.model;

import org.joda.time.LocalDate;

public class BusyDay {
    private final LocalDate date;
    private final Long volume;

    public BusyDay(LocalDate date, Long volume) {
        this.date = date;
        this.volume = volume;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getVolume() {
        return volume;
    }

    @Override
    public String toString() {
        return "BusyDay{" +
                "date=" + date +
                ", volume=" + volume +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BusyDay busyDay = (BusyDay) o;

        if (date != null ? !date.equals(busyDay.date) : busyDay.date != null) return false;
        return volume != null ? volume.equals(busyDay.volume) : busyDay.volume == null;
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (volume != null ? volume.hashCode() : 0);
        return result;
    }
}
