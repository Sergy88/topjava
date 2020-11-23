package ru.javawebinar.topjava.util.formatters;

import org.springframework.context.support.EmbeddedValueResolutionSupport;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class AnnotationDateFormatterFactory extends EmbeddedValueResolutionSupport
        implements AnnotationFormatterFactory<DateCustomFormatter> {
    private static final LocalDateTime MIN_DATE = LocalDateTime.of(1, 1, 1, 0, 0);
    private static final LocalDateTime MAX_DATE = LocalDateTime.of(3000, 1, 1, 0, 0);

    @Override
    public Set<Class<?>> getFieldTypes() {
        Set<Class<?>> fieldTypes = new HashSet<>();
        fieldTypes.add(LocalDate.class);
        return Collections.unmodifiableSet(fieldTypes);
    }

    @Override
    public Printer<LocalDate> getPrinter(DateCustomFormatter annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation);
    }

    @Override
    public Parser<LocalDate> getParser(DateCustomFormatter annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation);
    }

    private Formatter<LocalDate> configureFormatterFrom(DateCustomFormatter annotation) {
        return new Formatter<LocalDate>() {

            @Override
            public String print(LocalDate date, Locale locale) {
                return date.toString();
            }

            @Override
            public LocalDate parse(String text, Locale locale) throws ParseException {
                if (text.equals("null")) {
                    return annotation.convert().equals(ConvertNull.MIN) ? LocalDate.MIN : LocalDate.MAX;
                }
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                return LocalDate.parse(text, dateTimeFormatter);
            }
        };
    }
}