package com.casiano.builder;


import java.io.PrintWriter;
import java.util.List;
import java.util.stream.IntStream;

public record TargetClassInfo(String builderPackageName, String builderName, String packageName, String targetName,
                              PrintWriter writer,
                              Builder builderAnnotation, List<FieldInfo> fields) {

    public String fullName() {
        return packageName + "." + targetName;
    }

    public String variableName() {
        return Character.toLowerCase(targetName.charAt(0)) + targetName.substring(1);
    }

    public boolean isModeSetter() {
        return builderAnnotation.mode() == BuilderMode.SETTER;
    }

    public void lineBreak() {
        writer.println();
    }

    public void addIndentation(int level) {
        IntStream.range(0, level).forEach(l -> writer.print("  "));
    }

}
