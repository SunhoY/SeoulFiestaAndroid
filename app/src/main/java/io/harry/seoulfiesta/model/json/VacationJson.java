package io.harry.seoulfiesta.model.json;

import org.joda.time.DateTime;

public class VacationJson {
    public String type;
    public int userId;
    public String startDate;
    public String endDate;
    public String reason;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VacationJson that = (VacationJson) o;

        if (userId != that.userId) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null)
            return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        return !(reason != null ? !reason.equals(that.reason) : that.reason != null);

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + userId;
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        return result;
    }
}
