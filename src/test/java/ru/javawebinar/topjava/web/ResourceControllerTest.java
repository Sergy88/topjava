package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResourceControllerTest extends AbstractControllerTest {

    @Test
    void getStyles() throws Exception {
        MvcResult mvcResult = perform(get("/resources/css/style.css"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("text/css;charset=UTF-8", mvcResult.getResponse().getContentType());
    }

}
