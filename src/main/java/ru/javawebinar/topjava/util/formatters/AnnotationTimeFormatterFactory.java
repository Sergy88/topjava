package ru.javawebinar.topjava.util.formatters;

import org.springframework.context.support.EmbeddedValueResolutionSupport;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;


public class AnnotationTimeFormatterFactory extends EmbeddedValueResolutionSupport
        implements AnnotationFormatterFactory<TimeCustomFormatter> {

    @Override
    public Set<Class<?>> getFieldTypes() {
        Set<Class<?>> fieldTypes = new HashSet<>();
        fieldTypes.add(LocalTime.class);
        return Collections.unmodifiableSet(fieldTypes);
    }

    @Override
    public Printer<LocalTime> getPrinter(TimeCustomFormatter annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation);
    }

    @Override
    public Parser<LocalTime> getParser(TimeCustomFormatter annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation);
    }

    private Formatter<LocalTime> configureFormatterFrom(TimeCustomFormatter annotation) {
        return new Formatter<LocalTime>() {

            @Override
            public String print(LocalTime date, Locale locale) {
                return date.toString();
            }

            @Override
            public LocalTime parse(String text, Locale locale) throws ParseException {
                if (text.equals("null")) {
                    return null;
                }
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                return LocalTime.parse(text, dateTimeFormatter);
            }
        };
    }
}