package com.casiano.builder;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record FieldInfo(String name, String type) {

    private static final Pattern SETTER_SETTER = Pattern.compile("set(.*)\\((.*)\\)");

    public static List<FieldInfo> extractFields(Element element) {
        return element.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.FIELD)
                .map(e -> new FieldInfo(e.getSimpleName().toString(), e.asType().toString()))
                .toList();
    }

    public static List<FieldInfo> extractSetters(Element element) {
        return element.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.METHOD)
                .filter(e -> e.getSimpleName().toString().startsWith("set"))
                .flatMap(setter -> buildWithSetter(setter).stream())
                .toList();
    }

    private static Optional<FieldInfo> buildWithSetter(Element setter) {
        Matcher matcher = SETTER_SETTER.matcher(setter.toString());
        if (matcher.find()) {
            String setterName = matcher.group(1);
            String name = Character.toLowerCase(setterName.charAt(0)) + setterName.substring(1);
            String type = matcher.group(2);
            return Optional.of(new FieldInfo(name, type));
        }
        return Optional.empty();
    }
}


