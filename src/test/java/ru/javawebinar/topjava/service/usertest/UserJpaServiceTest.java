package ru.javawebinar.topjava.service.usertest;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles({Profiles.DATAJPA, Profiles.HSQL_DB})
public class UserJpaServiceTest extends UserBaseServiceTest {
}
