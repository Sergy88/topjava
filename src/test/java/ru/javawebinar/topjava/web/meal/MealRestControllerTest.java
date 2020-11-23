package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;
import static ru.javawebinar.topjava.web.json.JacksonObjectMapper.getMapper;

public class MealRestControllerTest extends AbstractControllerTest {

    @Autowired
    private MealService mealService;

    @Test
    void getAll() throws Exception {
        perform(get("/rest/meals"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(getTos(meals, DEFAULT_CALORIES_PER_DAY)));
    }

    @Test
    void getById() throws Exception {
        perform(get("/rest/meals/" + meal5.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meal5));
    }

    @Test
    void update() throws Exception {
        Meal updated = getUpdated();
        getMapper().writerFor(Meal.class);
        String json = getMapper().writerFor(Meal.class).writeValueAsString(updated);
        perform(put("/rest/meals/" + updated.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNoContent());
        assertEquals(mealService.get(MEAL1_ID, SecurityUtil.authUserId()), getUpdated());
    }

    @Test
    void create() throws Exception {
        Meal created = getNew();
        getMapper().writerFor(Meal.class);
        String json = getMapper().writerFor(Meal.class).writeValueAsString(created);
        perform(put("/rest/meals/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNoContent());
        Meal createdFromService = mealService.getAll(100000).stream()
                .filter(m -> m.getDescription().equals("Созданный ужин"))
                .findFirst()
                .orElse(new Meal());
        assertThat(created).usingRecursiveComparison()
                .ignoringFields("id", "user")
                .isEqualTo(createdFromService);
    }

    @Test
    void getBetweenFilter() throws Exception {
        perform(get("/rest/meals/filter")
                .param("startDate", "2020-01-30")
                .param("endDate", "2020-01-31")
                .param("startTime", "13:00")
                .param("endTime", "20:00"))
                .andExpect(status().isOk())
                .andExpect(MEAL_TO_MATCHER_NOEXCESS.contentJson(getTos(List.of(meal6, meal2), DEFAULT_CALORIES_PER_DAY)));

    }
}
