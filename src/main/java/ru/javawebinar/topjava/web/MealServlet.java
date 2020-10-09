package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");

    private MealRestController mealRestController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        mealRestController = appCtx.getBean(MealRestController.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        if (meal.isNew()) {
            log.info("Create {}", meal);
            mealRestController.create(meal);
        } else {
            log.info("Update {}", meal);
            mealRestController.update(meal);
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals",
                        getFilteredTosByParams(request.getParameter("startDate"),
                                request.getParameter("startTime"),
                                request.getParameter("endDate"),
                                request.getParameter("endTime")));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private List<MealTo> getFilteredTosByParams(String startDate, String startTime, String endDate, String endTime) {
        LocalDate startLocalDate = startDate == null ? LocalDate.MIN : !startDate.equals("") ? LocalDate.parse(startDate) : LocalDate.MIN;
        LocalDate endLocalDate = endDate == null ? LocalDate.MAX : !endDate.equals("") ? LocalDate.parse(endDate) : LocalDate.MAX;
        LocalTime startLocalTime = startTime == null ? LocalTime.MIN : !startTime.equals("") ? LocalTime.parse(startTime) : LocalTime.MIN;
        LocalTime endLocalTime = endTime == null ? LocalTime.MAX : !endTime.equals("") ? LocalTime.parse(endTime) : LocalTime.MAX;
        return mealRestController.getAllFiltered(startLocalDate, startLocalTime, endLocalDate, endLocalTime);
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
