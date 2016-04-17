package io.harry.seoulfiesta.model.json;

public class UserJson {
    public String password;
    public String userName;
    public String department;
    public String rank;
    public String email;
    public int id;
    public int daysOffPerYear;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserJson userJson = (UserJson) o;

        if (id != userJson.id) return false;
        if (daysOffPerYear != userJson.daysOffPerYear) return false;
        if (password != null ? !password.equals(userJson.password) : userJson.password != null)
            return false;
        if (userName != null ? !userName.equals(userJson.userName) : userJson.userName != null)
            return false;
        if (department != null ? !department.equals(userJson.department) : userJson.department != null)
            return false;
        if (rank != null ? !rank.equals(userJson.rank) : userJson.rank != null) return false;
        return !(email != null ? !email.equals(userJson.email) : userJson.email != null);

    }

    @Override
    public int hashCode() {
        int result = password != null ? password.hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (department != null ? department.hashCode() : 0);
        result = 31 * result + (rank != null ? rank.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + id;
        result = 31 * result + daysOffPerYear;
        return result;
    }
}
