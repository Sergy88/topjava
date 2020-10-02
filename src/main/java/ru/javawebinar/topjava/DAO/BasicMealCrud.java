package ru.javawebinar.topjava.DAO;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public interface BasicMealCrud {
    void deleteMeal(int mealId);

    void updateMeal(Meal meal);

    List<Meal> getAllMeals();

    Meal getMealById(int mealId);
}
