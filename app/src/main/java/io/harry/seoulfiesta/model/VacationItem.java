package io.harry.seoulfiesta.model;

import org.joda.time.DateTime;

public class VacationItem {
    int vacationId;
    String status;
    DateTime date;
    String type;

    public VacationItem(int vacationId, String status, DateTime date, String type) {
        this.vacationId = vacationId;
        this.status = status;
        this.date = date;
        this.type = type;
    }

    public String getDateString() {
        return date.toString("yyyy-MM-dd");
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }
}
