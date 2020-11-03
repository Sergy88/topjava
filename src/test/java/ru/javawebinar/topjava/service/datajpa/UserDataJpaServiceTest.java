package ru.javawebinar.topjava.service.datajpa;


import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.TestMatcher;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserBaseServiceTest;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.MealTestData.meals;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
public class UserDataJpaServiceTest extends UserBaseServiceTest {

    @Test
    public void getUserWithMeals() {
        User actualUser = service.getWithMeals(USER_ID);
        USER_MATCHER.assertMatch(actualUser, user);
        List<Meal> meals = actualUser.getMeals();
        if (!meals.isEmpty()) {
            USER_MATCHER.assertMatch(actualUser, meals.get(0).getUser());
            Collections.sort(meals, Comparator.comparing(Meal::getDateTime));
            Collections.reverse(meals);
            MEAL_MATCHER.assertMatch(actualUser.getMeals(), MealTestData.meals);
        }
    }


}
