package ru.javawebinar.topjava.util.formatters;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        super.addFormatters(registry);
        registry.addFormatterForFieldAnnotation(new AnnotationTimeFormatterFactory());
        registry.addFormatterForFieldAnnotation(new AnnotationDateFormatterFactory());
    }
}
