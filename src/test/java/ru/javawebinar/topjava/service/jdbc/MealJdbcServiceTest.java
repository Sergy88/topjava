package ru.javawebinar.topjava.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.MealBaseServiceTest;


@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
public class MealJdbcServiceTest extends MealBaseServiceTest {
}