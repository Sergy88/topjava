package ru.javawebinar.topjava.web.meal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.formatters.ConvertNull;
import ru.javawebinar.topjava.util.formatters.DateCustomFormatter;
import ru.javawebinar.topjava.util.formatters.TimeCustomFormatter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/meals", produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController extends AbstractMealController {

    @GetMapping
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Integer id) {
        super.delete(id);
    }

    @GetMapping("/{id}")
    public Meal getById(@PathVariable int id) {
        return super.get(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Meal meal, @PathVariable Integer id) {
        super.update(meal, id);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Meal create(@RequestBody Meal meal) {
        super.create(meal);
        return meal;
    }

    @GetMapping("/filter")
    public List<MealTo> getBetweenFilter(@RequestParam(defaultValue = "null") @DateCustomFormatter(convert = ConvertNull.MIN) LocalDate startDate,
                                         @RequestParam(defaultValue = "null") @TimeCustomFormatter LocalTime startTime,
                                         @RequestParam(defaultValue = "null") @DateCustomFormatter(convert = ConvertNull.MAX) LocalDate endDate,
                                         @RequestParam(defaultValue = "null") @TimeCustomFormatter LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }
}