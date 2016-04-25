package io.harry.seoulfiesta.model;

import org.joda.time.DateTime;

public class Vacation {
    int userId;
    String type;
    DateTime start;
    DateTime end;
    String reason;

    public Vacation(int userId, String type, DateTime startDate, DateTime endDate, String reason) {
        this.userId = userId;
        this.type = type;
        this.start = startDate;
        this.end = endDate;
        this.reason = reason;
    }

    public int getUserId() {
        return userId;
    }

    public String getType() {
        return type;
    }

    public DateTime getStart() {
        return start;
    }

    public DateTime getEnd() {
        return end;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vacation vacation = (Vacation) o;

        if (userId != vacation.userId) return false;
        if (type != null ? !type.equals(vacation.type) : vacation.type != null) return false;
        if (start != null ? !start.equals(vacation.start) : vacation.start != null) return false;
        if (end != null ? !end.equals(vacation.end) : vacation.end != null) return false;
        return !(reason != null ? !reason.equals(vacation.reason) : vacation.reason != null);

    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (start != null ? start.hashCode() : 0);
        result = 31 * result + (end != null ? end.hashCode() : 0);
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        return result;
    }
}
