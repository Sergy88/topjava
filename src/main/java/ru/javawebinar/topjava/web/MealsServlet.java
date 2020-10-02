package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;



public class MealsServlet extends HttpServlet {
    private static List<Meal> meals;
    private static final Logger log = getLogger(MealsServlet.class);

    @Override
    public void init() throws ServletException {
        meals = MealsUtil.getMealsMock();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("forward to Meals");
        List<MealTo> mealsTo = MealsUtil.filteredByStreams(meals, LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY);
        req.setAttribute("mealsAttrubute", mealsTo);
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }
}
