package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface BasicMealCrud {
    void delete(int mealId);

    Meal update(Meal meal);

    Meal create(Meal meal);

    List<Meal> getAll();

    Meal getById(int mealId);
}
