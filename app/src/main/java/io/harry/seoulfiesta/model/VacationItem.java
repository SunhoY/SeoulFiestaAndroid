package io.harry.seoulfiesta.model;

public class VacationItem {
    private String vacationDate;
    private String vacationStatus;
    private String vacationType;

    public VacationItem(String vacationType, String vacationStatus, String vacationDate) {
        this.vacationDate = vacationDate;
        this.vacationStatus = vacationStatus;
        this.vacationType = vacationType;
    }

    public String getVacationDate() {
        return vacationDate;
    }

    public String getVacationStatus() {
        return vacationStatus;
    }

    public String getVacationType() {
        return vacationType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VacationItem that = (VacationItem) o;

        if (vacationDate != null ? !vacationDate.equals(that.vacationDate) : that.vacationDate != null)
            return false;
        if (vacationStatus != null ? !vacationStatus.equals(that.vacationStatus) : that.vacationStatus != null)
            return false;
        return vacationType != null ? vacationType.equals(that.vacationType) : that.vacationType == null;

    }

    @Override
    public int hashCode() {
        int result = vacationDate != null ? vacationDate.hashCode() : 0;
        result = 31 * result + (vacationStatus != null ? vacationStatus.hashCode() : 0);
        result = 31 * result + (vacationType != null ? vacationType.hashCode() : 0);
        return result;
    }
}
