package ru.javawebinar.topjava.service.mealtest;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;


@ActiveProfiles({Profiles.DATAJPA, Profiles.HSQL_DB})
public class MealDataJpaServiceTest extends MealBaseServiceTest {

    @Test
    public void getMealWithUser() {
        Meal mealWithUser = service.getMealWithUser(MEAL1_ID, USER_ID);
        MEAL_MATCHER.assertMatch(mealWithUser, meal1);
    }

}