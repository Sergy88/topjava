package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(this::initValues);
    }

    private void initValues(Meal meal) {
        this.save(meal, 1);
    }

    @Override
    public Meal save(Meal meal, Integer authId) {
        repository.computeIfAbsent(authId, HashMap::new);
        Map<Integer, Meal> personalrep = repository.get(authId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            personalrep.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return personalrep.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, Integer userId) {
        Map<Integer, Meal> personalRepo = repository.getOrDefault(userId, new HashMap<>());
        return personalRepo.remove(id) != null;
    }

    @Override
    public Meal get(int id, Integer userId) {
        Map<Integer, Meal> personalRepo = repository.getOrDefault(userId, new HashMap<>());
        return personalRepo.get(id);
    }

    @Override
    public List<Meal> getAll(Integer userId) {
        Map<Integer, Meal> personalRepo = repository.getOrDefault(userId, new HashMap<>());
        ArrayList<Meal> meals = new ArrayList(personalRepo.values());
        return meals.stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAllFiltered(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, Integer userId) {
        List<Meal> meals = getAll(userId);
        List<Meal> result = meals.stream()
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalDate(), startDate, endDate))
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());

        return result;
    }
}

