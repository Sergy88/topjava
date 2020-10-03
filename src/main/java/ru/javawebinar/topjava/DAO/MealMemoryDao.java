package ru.javawebinar.topjava.DAO;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class MealMemoryDao implements BasicMealCrud {
    private static final Logger log = getLogger(MealMemoryDao.class);
    private static AtomicInteger counter = new AtomicInteger(0);
    private Map<Integer, Meal> mealsRepo = new ConcurrentHashMap<>();

    {
        MealsUtil.getMealsMock().forEach(this::create);
    }

    public void delete(int mealId) {
        log.debug("delete meal with id{}", mealId);
        mealsRepo.remove(mealId);
    }

    public Meal update(Meal meal) {
        log.debug("update meal {}", meal);
        if (!meal.isNew()) {
            return mealsRepo.put(meal.getId(), meal);
        }
        return null;
    }

    @Override
    public Meal create(Meal meal) {
        log.debug("create meal {}", meal);
        if (meal.isNew()) {
            log.debug("create meal {}", meal);
            meal.setId(counter.incrementAndGet());
            return mealsRepo.put(meal.getId(), meal);
        }
        return null;
    }

    public List<Meal> getAll() {
        log.debug("get all meals from dao");
        List<Meal> mealsAll = new ArrayList<>(mealsRepo.values());
        return mealsAll;
    }

    public Meal getById(int mealId) {
        log.debug("getMealbyId{}", mealId);
        return mealsRepo.get(mealId);
    }

}
