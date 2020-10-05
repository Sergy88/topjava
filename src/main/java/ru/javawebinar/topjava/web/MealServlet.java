package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.BasicMealCrud;
import ru.javawebinar.topjava.dao.MealMemoryDao;
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
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;


public class MealServlet extends HttpServlet {
    private BasicMealCrud mealDao;
    private static final Logger log = getLogger(MealServlet.class);

    @Override
    public void init() throws ServletException {
        mealDao = new MealMemoryDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("doGet method of MealsServlet");
        String action = req.getParameter("action");

        if (action != null) {
            log.debug("action parameter found");
            switch (action) {
                case "delete":
                    log.debug("action parameter equals delete. deleting meal with id {}", getIdFromRequest(req));
                    mealDao.delete(getIdFromRequest(req));
                    log.debug("sending redirect to meals");
                    resp.sendRedirect("meals");
                    break;
                case "update":
                    log.debug("action parameter equals update. updating meal with id {}", getIdFromRequest(req));
                    int mealId = getIdFromRequest(req);
                    req.setAttribute("meal", mealDao.getById(mealId));
                    log.debug("forward to edit form");
                    req.getRequestDispatcher("/editForm.jsp").forward(req, resp);
                    break;
                default:
                    log.debug("not valid action parameter... redirect to meals");
                    resp.sendRedirect("meals");
            }
        } else {
            log.debug("no action parameters found... getting TO`s and forwarding to /meals.jsp");
            List<MealTo> mealsTo = MealsUtil.filteredByStreams(mealDao.getAll(), LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY);
            req.setAttribute("mealsAttrubute", mealsTo);
            req.getRequestDispatcher("/meals.jsp").forward(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        log.debug("at doPost method");
        if (req.getParameter("id") == null) {
            log.debug("parameter id is absent - forward to edit form");
            req.getRequestDispatcher("/editForm.jsp").forward(req, resp);
        } else {
            log.debug("parameter id is present - creating meal object");
            Meal meal = new Meal(getIdFromRequest(req),
                    LocalDateTime.parse(req.getParameter("dateTime")),
                    req.getParameter("description"),
                    Integer.parseInt(req.getParameter("cal")));
            log.debug("meal obj created {}", meal);
            if (meal.isNew()) {
                log.debug("meal is new - calling create method");
                mealDao.create(meal);
            } else {
                log.debug("meal is not new - calling update method");
                mealDao.update(meal);
            }
            resp.sendRedirect("meals");
        }
    }

    private static int getIdFromRequest(HttpServletRequest req) {
        return Integer.parseInt(req.getParameter("id"));
    }
}
