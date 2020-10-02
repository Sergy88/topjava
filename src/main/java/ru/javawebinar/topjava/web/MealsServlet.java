package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.DAO.BasicMealCrud;
import ru.javawebinar.topjava.DAO.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;


public class MealsServlet extends HttpServlet {
    private static BasicMealCrud dao;
    private static final Logger log = getLogger(MealsServlet.class);

    @Override
    public void init() throws ServletException {
        dao = new MealDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("forward to Meals");
        String action = req.getParameter("action");

        if (action != null) {
            switch (action) {
                case "delete":
                    dao.deleteMeal(Integer.parseInt(req.getParameter("id")));
                    break;
                case "update":
                    int mealId = Integer.parseInt(req.getParameter("id"));
                    req.setAttribute("meal", dao.getMealById(mealId));
                    req.getRequestDispatcher("/editForm.jsp").forward(req, resp);
                    break;
            }

        }
        List<MealTo> mealsTo = MealsUtil.filteredByStreams(dao.getAllMeals(), LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY);
        req.setAttribute("mealsAttrubute", mealsTo);
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        log.debug("doPost servlet method");
        String id = req.getParameter("id");
        String description = req.getParameter("description");
        String dateTime = req.getParameter("dateTime");
        String calories = req.getParameter("cal");
        log.debug("id:{} des:{} dateTime:{} calories:{}", id, description, dateTime, calories);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTimeParsed = LocalDateTime.parse(dateTime, formatter);
        Meal meal = new Meal();
        meal.setId(Integer.parseInt(id));
        meal.setDateTime(dateTimeParsed);
        meal.setCalories(Integer.parseInt(calories));
        meal.setDescription(description);
        dao.updateMeal(meal);
        resp.sendRedirect("meals");
    }
}
