package ru.javawebinar.topjava.service.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.UserBaseServiceTest;

@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
public class UserJpaServiceTest extends UserBaseServiceTest {
}
