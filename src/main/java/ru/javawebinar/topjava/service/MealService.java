package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    public Meal update(Meal meal, int userId) {
        return checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public Meal get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public List<MealTo> getAll(int userId, int calories) {
        return MealsUtil.getTos(repository.getAll(userId), calories);
    }

    public List<MealTo> getAllFiltered(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, int userId, int calories) {
        return MealsUtil.getTos(repository.getAllFilteredByDates(startDate, endDate, userId), calories).stream()
                .filter(m -> DateTimeUtil.isBetweenHalfOpen(m.getDateTime().toLocalTime(), startTime, endTime))
                .collect(Collectors.toList());
    }
}