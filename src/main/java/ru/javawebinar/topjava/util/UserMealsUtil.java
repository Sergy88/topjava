package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
        List<UserMealWithExcess> mealsTo = filteredByStreamsCustomCollector(meals, LocalTime.of(1, 0), LocalTime.of(11, 0), 2000);
        mealsTo.forEach(System.out::println);
//        System.out.println("\n");
//        filteredByOneCycle(meals, LocalTime.of(9, 0), LocalTime.of(21, 0), 2000).forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<>();
        for (UserMeal meal : meals) {
            caloriesPerDayMap.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
        }
        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                result.add(new UserMealWithExcess(meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        caloriesPerDayMap.get(meal.getDateTime().toLocalDate()) > caloriesPerDay)); //excess check
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByOneCycle(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<>();
        Map<LocalDate, AtomicBoolean> excessMap = new HashMap<>();
        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal meal : meals) {
            caloriesPerDayMap.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
            excessMap.computeIfAbsent(meal.getDateTime().toLocalDate(), k -> new AtomicBoolean()).set(caloriesPerDayMap.get(meal.getDateTime().toLocalDate()) > caloriesPerDay);
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                UserMealWithExcess um = new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        excessMap.get(meal.getDateTime().toLocalDate()));
                result.add(um);
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDayMap = meals.stream()
                .collect(Collectors.groupingBy(um -> um.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));
        List<UserMealWithExcess> filteredUserMealsWithExcess = meals.stream()
                .filter(um -> TimeUtil.isBetweenHalfOpen(um.getDateTime().toLocalTime(), startTime, endTime))
                .map(um -> new UserMealWithExcess(um.getDateTime(), um.getDescription(), um.getCalories(),
                        caloriesPerDayMap.get(um.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
        return filteredUserMealsWithExcess;

    }

    public static List<UserMealWithExcess> filteredByStreamsCustomCollector(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> userMeals = meals.stream().collect(
                new Collector<UserMeal, List<UserMealWithExcess>, List<UserMealWithExcess>>() {
                    Map<LocalDate, AtomicBoolean> excessMap = new HashMap<>();
                    Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<>();

                    @Override
                    public Supplier<List<UserMealWithExcess>> supplier() {
                        return ArrayList::new;
                    }

                    @Override
                    public BiConsumer<List<UserMealWithExcess>, UserMeal> accumulator() {
                        return (list, userMeal) -> {
                            caloriesPerDayMap.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum);
                            excessMap.computeIfAbsent(userMeal.getDateTime().toLocalDate(), (k) -> new AtomicBoolean()).set(caloriesPerDayMap.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay);
                            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                                UserMealWithExcess ume = new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(),
                                        userMeal.getCalories(), excessMap.get(userMeal.getDateTime().toLocalDate()));
                                list.add(ume);
                            }
                        };
                    }

                    @Override
                    public BinaryOperator<List<UserMealWithExcess>> combiner() {
                        return (list1, list2) -> {
                            list1.addAll(list2);
                            return list1;
                        };
                    }

                    @Override
                    public Function<List<UserMealWithExcess>, List<UserMealWithExcess>> finisher() {
                        return Function.identity();
                    }

                    @Override
                    public Set<Characteristics> characteristics() {
                        Set<Characteristics> characteristics = new HashSet<>();
                        characteristics.add(Characteristics.UNORDERED);
                        return characteristics;
                    }
                });

        return userMeals;
    }
}
