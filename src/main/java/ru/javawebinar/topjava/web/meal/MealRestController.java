package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private MealService service;
    private final static Logger log = LoggerFactory.getLogger(MealRestController.class);

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        log.debug("getAll");
        return service.getAll(SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealTo> getAllFiltered(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        log.debug("getAllFiltered");
        startDate = startDate == null ? LocalDate.MIN : startDate;
        endDate = endDate == null ? LocalDate.MAX : endDate;
        startTime = startTime == null ? LocalTime.MIN : startTime;
        endTime = endTime == null ? LocalTime.MAX : endTime;
        return service.getAllFiltered(startDate, startTime, endDate, endTime, SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay());
    }

    public Meal get(int id) {
        log.debug("get");
        return service.get(id, SecurityUtil.authUserId());
    }

    public Meal create(Meal meal) {
        log.debug("create");
        checkNew(meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        log.debug("create");
        service.delete(id, SecurityUtil.authUserId());
    }

    public Meal update(Meal meal, int id) {
        log.debug("update");
        assureIdConsistent(meal, id);
        return service.update(meal, SecurityUtil.authUserId());
    }
}