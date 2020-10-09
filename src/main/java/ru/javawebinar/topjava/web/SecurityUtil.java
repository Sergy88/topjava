package ru.javawebinar.topjava.web;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {

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