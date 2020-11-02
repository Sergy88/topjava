package ru.javawebinar.topjava.service.usertest;


import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.TestMatcher;
import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.user;

@ActiveProfiles({Profiles.DATAJPA, Profiles.HSQL_DB})
public class UserJpaDataServiceTest extends UserBaseServiceTest {

    @Test
    public void getUserWithMeals() {
        User actualUser = service.gerUserWithMeals(USER_ID);
        TestMatcher.usingIgnoringFieldsComparator("registered", "roles", "meals").assertMatch(actualUser, user);
    }

}
