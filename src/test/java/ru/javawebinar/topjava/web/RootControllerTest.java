package ru.javawebinar.topjava.web;

import org.assertj.core.matcher.AssertionMatcher;
import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;

class RootControllerTest extends AbstractControllerTest {

    @Test
    void getUsers() throws Exception {
        perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"))
                .andExpect(model().attribute("users",
                        new AssertionMatcher<List<User>>() {
                            @Override
                            public void assertion(List<User> actual) throws AssertionError {
                                USER_MATCHER.assertMatch(actual, admin, user);
                            }
                        }
                ));
    }

    @Test
    void getFilter() throws Exception {
        perform(get("/meals/filter"))
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(model().attribute("meals",
                        new AssertionMatcher<List<MealTo>>() {
                            @Override
                            public void assertion(List<MealTo> actual) throws AssertionError {
                                assertEquals(actual, getTos(meals, DEFAULT_CALORIES_PER_DAY));
                            }
                        }));
    }

    @Test
    void deleteMeal() throws Exception {
        perform(get("/meals/delete?id=100003"))
                .andExpect(view().name("redirect:/meals"));
        perform(get("/meals/filter"))
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(model().attribute("meals",
                        new AssertionMatcher<List<MealTo>>() {
                            @Override
                            public void assertion(List<MealTo> actual) throws AssertionError {
                                assertEquals(actual, getTos(List.of(meal7, meal6, meal5, meal4, meal3, meal1), DEFAULT_CALORIES_PER_DAY));
                            }
                        }));
    }

    @Test
    void update() throws Exception {
        perform(get("/meals/update?id=100003"))
                .andExpect(status().isOk())
                .andExpect(view().name("mealForm"));
    }

    @Test
    void create() throws Exception {
        perform(get("/meals/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("mealForm"))
                .andExpect(model().attribute("meal", new AssertionMatcher<Meal>() {
                    @Override
                    public void assertion(Meal actual) throws AssertionError {
                        MEAL_MATCHER.assertMatch(actual, new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), "", 1000));
                    }
                }));
    }

    @Test
    void updatePost() throws Exception {
        Meal newMeal3 = new Meal(meal3.getId(), meal3.getDateTime(), "Измененный завтрак", 777);
        perform(post("/meals")
                .param("dateTime", meal3.getDateTime().toString())
                .param("description", newMeal3.getDescription())
                .param("calories", String.valueOf(newMeal3.getCalories()))
                .param("id", String.valueOf(meal3.getId()))
        )
                .andExpect(view().name("redirect:/meals"));

        perform(get("/meals/filter"))
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(model().attribute("meals",
                        new AssertionMatcher<List<MealTo>>() {
                            @Override
                            public void assertion(List<MealTo> actual) throws AssertionError {
                                assertEquals(actual, getTos(List.of(meal7, meal6, meal5, meal4, newMeal3, meal2, meal1), DEFAULT_CALORIES_PER_DAY));
                            }
                        }));
    }

    @Test
    void createPost() throws Exception {
        Meal newMeal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), "Новый завтрак", 888);
        perform(post("/meals")
                .param("dateTime", newMeal.getDateTime().toString())
                .param("description", newMeal.getDescription())
                .param("calories", String.valueOf(newMeal.getCalories()))
                .param("id", "")
        )
                .andExpect(view().name("redirect:/meals"));
        newMeal.setId(100011);
        perform(get("/meals/filter"))
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(model().attribute("meals",
                        new AssertionMatcher<List<MealTo>>() {
                            @Override
                            public void assertion(List<MealTo> actual) throws AssertionError {
                                assertEquals(actual, getTos(List.of(newMeal, meal7, meal6, meal5, meal4, meal3, meal2, meal1), DEFAULT_CALORIES_PER_DAY));
                            }
                        }));
    }

    @Test
    void getBetween() throws Exception {
        perform(get("/meals/filter")
                .param("startDate", "2020-01-30")
                .param("endDate", "2020-01-31")
                .param("startTime", "13:00:00")
                .param("endTime", "20:00:00"))
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(model().attribute("meals",
                        new AssertionMatcher<List<MealTo>>() {
                            @Override
                            public void assertion(List<MealTo> actual) throws AssertionError {
                                MEAL_TO_MATCHER_NOEXCESS.assertMatch(actual, getTos(List.of(meal6, meal2), DEFAULT_CALORIES_PER_DAY));
                            }
                        }
                ));
    }
}