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

        List<UserMealWithExcess> mealsTo = filteredByStreamsCustomCollector(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println("\n");
        filteredByCyclesOneCycle(meals, LocalTime.of(9, 0), LocalTime.of(21, 0), 2000).forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMeal> filteredByTime = new ArrayList<>();
        Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<>();

        for (UserMeal meal:meals){
            caloriesPerDayMap.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), (integer, integer2) -> integer + integer2);
             if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)){
                 filteredByTime.add(meal);
             }
        }

        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal meal:filteredByTime){
            result.add(new UserMealWithExcess(meal.getDateTime(),
                    meal.getDescription(),
                    meal.getCalories(),
                    new AtomicBoolean (caloriesPerDayMap.get(meal.getDateTime().toLocalDate())>caloriesPerDay))); //excess check
        }


        return result;
    }

    public static List<UserMealWithExcess> filteredByCyclesOneCycle(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay){
        Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<LocalDate, Integer>();
        Map<LocalDate, AtomicBoolean> excessMap = new HashMap<LocalDate, AtomicBoolean>();
        List<UserMealWithExcess> result = new ArrayList<>();

        for (UserMeal meal: meals){
            caloriesPerDayMap.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), (v1, v2)-> v1+v2);
            AtomicBoolean bool = excessMap.getOrDefault(meal.getDateTime().toLocalDate(), new AtomicBoolean());
            bool.set(caloriesPerDayMap.get(meal.getDateTime().toLocalDate())>caloriesPerDay);
            excessMap.put(meal.getDateTime().toLocalDate(), bool);
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)){
                UserMealWithExcess um = new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        new AtomicBoolean(false));
                um.setExcess(excessMap.get(meal.getDateTime().toLocalDate()));
                result.add(um);

            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDayMap = meals.stream().collect(Collectors.groupingBy(userMeal -> userMeal.getDateTime().toLocalDate(),
                Collectors.summingInt(UserMeal::getCalories)));

        List<UserMealWithExcess> filteredUserMealsWithExcess = meals.stream()
                .filter(m-> TimeUtil.isBetweenHalfOpen(m.getDateTime().toLocalTime(), startTime, endTime))
                .map(m-> new UserMealWithExcess(m.getDateTime(), m.getDescription(), m.getCalories(),
                         new AtomicBoolean (caloriesPerDayMap.get(m.getDateTime().toLocalDate())>caloriesPerDay)))
                .collect(Collectors.toList());
        return filteredUserMealsWithExcess;

    }

    public static List<UserMealWithExcess> filteredByStreamsCustomCollector(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> userMeals = meals.stream().collect(
                new Collector<UserMeal, List<UserMealWithExcess>, List<UserMealWithExcess>>() {
                    Map<LocalDate, AtomicBoolean> excessMap= new HashMap<>();
                    Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<>();

                    @Override
                    public Supplier<List<UserMealWithExcess>> supplier() {

                        return () -> {
                            return new ArrayList<UserMealWithExcess>();
                        };
                    }

                    @Override
                    public BiConsumer<List<UserMealWithExcess>, UserMeal> accumulator() {
                        System.out.println("accumulator");
                        return (list, userMeal) -> {
                            caloriesPerDayMap.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), (um, um2)->um+um2);
                            AtomicBoolean bool = excessMap.getOrDefault(userMeal.getDateTime().toLocalDate(), new AtomicBoolean());
                            bool.set(caloriesPerDayMap.get(userMeal.getDateTime().toLocalDate())>caloriesPerDay);
                            excessMap.put(userMeal.getDateTime().toLocalDate(), bool);
                            UserMealWithExcess ume = new UserMealWithExcess(userMeal.getDateTime(),userMeal.getDescription(),userMeal.getCalories(), new AtomicBoolean());
                            ume.setExcess(excessMap.get(userMeal.getDateTime().toLocalDate()));
                            list.add(ume);
                        };
                    }

                    @Override
                    public BinaryOperator<List<UserMealWithExcess>> combiner() {
                        System.out.println("combiner");
                        return (list1, list2) -> {
                             list1.addAll(list2);
                            return list1;
                        };
                    }

                    @Override
                    public Function<List<UserMealWithExcess>, List<UserMealWithExcess>> finisher() {
                        return Function.identity();
                    };

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
