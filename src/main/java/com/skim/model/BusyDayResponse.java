package com.skim.model;

import java.util.List;

public class BusyDayResponse {
    private final Long averageVolume;
    private final List<BusyDay> days;

    public BusyDayResponse(Long averageVolume, List<BusyDay> days) {
        this.averageVolume = averageVolume;
        this.days = days;
    }

    public Long getAverageVolume() {
        return averageVolume;
    }

    public List<BusyDay> getDays() {
        return days;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BusyDayResponse that = (BusyDayResponse) o;

        if (averageVolume != null ? !averageVolume.equals(that.averageVolume) : that.averageVolume != null)
            return false;
        return days != null ? days.equals(that.days) : that.days == null;
    }

    @Override
    public int hashCode() {
        int result = averageVolume != null ? averageVolume.hashCode() : 0;
        result = 31 * result + (days != null ? days.hashCode() : 0);
        return result;
    }
}
