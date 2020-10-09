package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

public interface MealRepository {
    // null if not found, when updated
    Meal save(Meal meal, Integer userId);

    boolean delete(int id, Integer userId);

    // null if not found
    Meal get(int id, Integer userId);

    Collection<Meal> getAll(Integer userId);

    List<Meal> getAllFiltered(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, Integer userId);
}
