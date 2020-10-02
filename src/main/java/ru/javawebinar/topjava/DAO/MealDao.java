package ru.javawebinar.topjava.DAO;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class MealDao implements BasicMealCrud {
    private static final Logger log = getLogger(MealDao.class);
    private static AtomicInteger counter = new AtomicInteger(0);
    private ConcurrentHashMap<Integer, Meal> mealsRepo = new ConcurrentHashMap<>();
    {
        MealsUtil.getMealsMock().forEach(this::updateMeal);
    }
    public void deleteMeal(int mealId) {
        log.debug("delete meal with id{}", mealId);
        mealsRepo.remove(mealId);
    }

    public void updateMeal(Meal meal) {
        log.debug("update meal {}", meal);
        if (meal.getId() == 0) {
            meal.setId(counter.incrementAndGet());
        }
        mealsRepo.put(meal.getId(), meal);
    }

    public List<Meal> getAllMeals() {
        log.debug("get all meals from dao");
        List<Meal> mealsAll = (new ArrayList(mealsRepo.values()));
        Collections.sort(mealsAll, Comparator.comparing(Meal::getDateTime));
        Collections.reverse(mealsAll);
        return mealsAll;
    }

    public Meal getMealById(int mealId) {
        log.debug("getMealbyId{}", mealId);
        return mealsRepo.get(mealId);
    }

}
