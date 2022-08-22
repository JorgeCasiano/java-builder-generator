package com.casiano.builder;

import java.util.Optional;

public class BuilderHelper {

    private BuilderHelper() {

    }

    public static Optional<String> extractPackageName(String builderAnnotationName) {
        if (!builderAnnotationName.isBlank() && builderAnnotationName.contains(".")) {
            return Optional.of(builderAnnotationName.substring(0, builderAnnotationName.lastIndexOf(".")));
        }

        return Optional.empty();
    }

    public static Optional<String> extractBuilderName(String builderAnnotationName) {
        if (builderAnnotationName.isBlank()) {
            return Optional.empty();
        }

        return Optional.of(builderAnnotationName.contains(".") ?
                builderAnnotationName.substring(builderAnnotationName.lastIndexOf(".") + 1) : builderAnnotationName);
    }

}
