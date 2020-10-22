package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final int MEAL1_ID = 100002;
    public static final int ADMIN_MEAL1_ID = 100008;
    public static final Meal MEAL1 = new Meal(MEAL1_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 100);
    public static final Meal MEAL2 = new Meal(MEAL1_ID + 1, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal MEAL3 = new Meal(MEAL1_ID + 2, LocalDateTime.of(2020, Month.JANUARY, 30, 0, 0), "Завтрак", 100);
    public static final Meal MEAL4 = new Meal(MEAL1_ID + 3, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 1000);
    public static final Meal MEAL5 = new Meal(MEAL1_ID + 4, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Завтрак", 500);
    public static final Meal MEAL6 = new Meal(MEAL1_ID + 5, LocalDateTime.of(2020, Month.JANUARY, 31, 12, 0), "Завтрак", 410);
    public static final Meal ADMIN_MEAL1 = new Meal(ADMIN_MEAL1_ID, LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Завтрак", 410);
    public static final Meal ADMIN_MEAL2 = new Meal(ADMIN_MEAL1_ID + 1, LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "Завтрак", 410);

    public static final List<Meal> MEALS = Arrays.asList(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);

    public static Meal getCreated(){
        return new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 21, 0), "Obed", 2000);
    }
    public static Meal getUpdated(){
        return new Meal(MEAL1_ID, MEAL1.getDateTime(), "Обновленный завтрак", 200);
    }

    public static void assertMatch(Meal actual, Meal expected){
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal ... expected){
        assertMatch(actual,  Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected){
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }


}
