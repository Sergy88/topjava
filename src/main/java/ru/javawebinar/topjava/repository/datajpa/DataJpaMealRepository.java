package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository crudRepository;

    private final CrudUserRepository crudUserRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository crudUserRepository) {
        this.crudRepository = crudRepository;
        this.crudUserRepository = crudUserRepository;
    }

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setUser(crudUserRepository.getOne(userId));
            return crudRepository.saveAndFlush(meal);
        }
        Meal mealDb = crudRepository.findById(meal.getId()).orElse(null);
        meal.setUser(mealDb.getUser());
        return mealDb != null && mealDb.getUser().getId() == userId ? crudRepository.saveAndFlush(meal) : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.deleteById(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal one = crudRepository.findById(id).orElse(null);
        return one != null && one.getUser().getId() == userId ? one : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findAll(userId, Sort.by(Sort.Direction.DESC, "dateTime"));
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.getBetweenHalfOpen(startDateTime, endDateTime, userId);
    }

    @Override
    public Meal getMealWithUser(int id, int UserId) {
        return crudRepository.getMealById(id);
    }
}
