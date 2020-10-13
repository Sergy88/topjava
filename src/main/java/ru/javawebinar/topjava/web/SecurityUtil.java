package ru.javawebinar.topjava.web;

public class SecurityUtil {

    private static int DEFAULT_CALORIES_PER_DAY =2000;
    private static int loggedUser;

    public static int authUserId() {
        return loggedUser;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }

    public static void setLoggedUser(int loggedUser) {
        SecurityUtil.loggedUser = loggedUser;
    }

}