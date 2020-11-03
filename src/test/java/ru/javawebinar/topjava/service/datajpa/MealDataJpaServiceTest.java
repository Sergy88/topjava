package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealBaseServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;


@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
public class MealDataJpaServiceTest extends MealBaseServiceTest {

    @Test
    public void getMealWithUser() {
        Meal mealWithUser = service.getWithUser(MEAL1_ID, USER_ID);
        MEAL_MATCHER.assertMatch(mealWithUser, meal1);
    }

    @Test
    public void getMealNotOwn() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, ADMIN_ID));
    }


}