package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> this.save(meal, 1));
        MealsUtil.meals1.forEach(meal -> this.save(meal, 2));
    }

    @Override
    public Meal save(Meal meal, int authId) {
        Map<Integer, Meal> personalrep = repository.computeIfAbsent(authId, HashMap::new);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            personalrep.put(meal.getId(), meal);
        }
        // handle case: update, but not present in storage
        return personalrep.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return repository.computeIfAbsent(userId, HashMap::new).remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        return repository.computeIfAbsent(userId, HashMap::new).get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getFilteredByPredicate(m -> true, userId);
    }

    @Override
    public List<Meal> getAllFilteredByDates(LocalDate startDate, LocalDate endDate, int userId) {
        return getFilteredByPredicate(m -> DateTimeUtil.isBetweenClosed(m.getDate(), startDate, endDate), userId);
    }

    private List<Meal> getFilteredByPredicate(Predicate<Meal> pred, int userId) {
        return repository.computeIfAbsent(userId, HashMap::new).values().stream()
                .filter(pred)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

