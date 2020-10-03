package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.DAO.BasicMealCrud;
import ru.javawebinar.topjava.DAO.MealMemoryDao;
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
import static ru.javawebinar.topjava.util.MealsUtil.getIdFromRequest;


public class MealsServlet extends HttpServlet {
    private static BasicMealCrud mealDao;
    private static final Logger log = getLogger(MealsServlet.class);

    @Override
    public void init() throws ServletException {
        mealDao = new MealMemoryDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("forward to Meals");
        String action = req.getParameter("action");

        if (action != null) {
            switch (action) {
                case "delete":
                    mealDao.delete(getIdFromRequest(req));
                    resp.sendRedirect("meals");
                    break;
                case "update":
                    int mealId = getIdFromRequest(req);
                    req.setAttribute("meal", mealDao.getById(mealId));
                    req.getRequestDispatcher("/editForm.jsp").forward(req, resp);
                    break;
            }
        } else {
            List<MealTo> mealsTo = MealsUtil.filteredByStreams(mealDao.getAll(), LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY);
            req.setAttribute("mealsAttrubute", mealsTo);
            req.getRequestDispatcher("/meals.jsp").forward(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        log.debug("doPost servlet method");
        int reqId = getIdFromRequest(req);
        String description = req.getParameter("description");
        String dateTime = req.getParameter("dateTime");
        String calories = req.getParameter("cal");
        log.debug("id:{} des:{} dateTime:{} calories:{}", reqId, description, dateTime, calories);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTimeParsed = LocalDateTime.parse(dateTime, formatter);
        Meal meal = new Meal();
        meal.setId(reqId);
        meal.setDateTime(dateTimeParsed);
        meal.setCalories(Integer.parseInt(calories));
        meal.setDescription(description);
        if (meal.isNew()) mealDao.create(meal);
        else mealDao.update(meal);
        resp.sendRedirect("meals");
    }


}
