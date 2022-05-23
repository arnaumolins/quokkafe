package github.com.arnaumolins.quokkafe.Model;

import java.util.List;

public class User {
    public String userId;
    public String userName;
    public String userEmail;
    public String userPassword;
    public int userAge;
    public int userPoints;
    public List<String> interestedIn;

    public User() {}

    public User (String userName, String userEmail, int userAge, List<String> interestedIn) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userAge = userAge;
        this.userPoints = 0;
        this.interestedIn = interestedIn;
    }

    public User(String userId, String userName, String userEmail, String userPassword, int userAge, List<String> interestedIn) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userAge = userAge;
        this.userPoints = 0;
        this.interestedIn = interestedIn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public int getUserPoints() {
        return userPoints;
    }

    public void setUserPoints(int userPoints) {
        this.userPoints = userPoints;
    }

    public List<String> getInterestedIn() {
        return interestedIn;
    }

    public void setInterestedIn(List<String> interestedIn) {
        this.interestedIn = interestedIn;
    }

}
