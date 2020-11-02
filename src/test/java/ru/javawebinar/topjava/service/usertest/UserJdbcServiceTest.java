package ru.javawebinar.topjava.service.usertest;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles({Profiles.JDBC, Profiles.HSQL_DB})
public class UserJdbcServiceTest extends UserBaseServiceTest {
}
