package com.casiano.builder;

import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@SupportedAnnotationTypes("com.casiano.builder.Builder")
@AutoService(Processor.class)
public class BuilderProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Builder.class);

        elements.forEach(this::generateClass);

        return false;
    }

    private void generateClass(Element element) {
        try {
            Builder builderAnnotation = element.getAnnotation(Builder.class);
            String pkg = element.getEnclosingElement().toString();

            String builderName = element.getSimpleName() + "Builder";

            JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(pkg + "." + builderName);
            PrintWriter out = new PrintWriter(builderFile.openWriter());
            try (out) {
                declareClass(out, pkg, builderName);
                boolean modeSetter = builderAnnotation.mode() == BuilderMode.SETTER;
                List<FieldInfo> fields = modeSetter ? FieldInfo.extractSetters(element) : FieldInfo.extractFields(element);
                addFields(fields, out);

                addNewBuilder(out, builderName);
                addBuildMethod(out, pkg, element.getSimpleName().toString(), fields, modeSetter);
                addMethods(fields, builderName, out);

                if (!modeSetter) {
                    addHelperSetField(out, pkg, element.getSimpleName().toString());
                }

                out.println("}");
            }
        } catch (Exception e) {
            error("Error: " + e, element);
        }
    }

    private void addNewBuilder(PrintWriter out, String builderName) {
        addIndentation(out, 1);
        out.println("public static " + builderName + " newBuilder() {");
        addIndentation(out, 2);
        out.println("return new " + builderName + "();");
        addIndentation(out, 1);
        out.println("}");
        lineBreak(out);
    }

    private void declareClass(PrintWriter out, String pkg, String builderName) {
        out.println("package " + pkg + ";");
        lineBreak(out);
        out.println("public class " + builderName + "{");
        lineBreak(out);
    }

    private void addBuildMethod(PrintWriter out, String pkg, String clsName, List<FieldInfo> fields, boolean modeSetter) {
        String fullName = pkg + "." + clsName;
        String variable = clsName.toLowerCase();
        addIndentation(out, 1);
        out.println("public " + fullName + " build() {");
        addIndentation(out, 2);
        if (modeSetter) {
            out.println(fullName + " " + variable + " = new " + fullName + "();");
            for (FieldInfo field : fields) {
                addIndentation(out, 2);
                String setter = "set" + Character.toUpperCase(field.name().charAt(0)) + field.name().substring(1);
                out.println(variable + "." + setter + "(this." + field.name() + ");");
            }
            addIndentation(out, 2);
            out.println("return " + variable + ";");
        } else {
            out.println("try {");
            addIndentation(out, 3);
            out.println(fullName + " " + variable + " = new " + fullName + "();");
            addIndentation(out, 3);
            out.println("java.lang.reflect.Field[] fields = " + fullName + ".class.getDeclaredFields();");
            for (int i = 0; i < fields.size(); i++) {
                addIndentation(out, 3);
                out.println("setField(" + variable + ", fields[" + i + "], this." + fields.get(i).name() + ");");
            }
            addIndentation(out, 3);
            out.println("return " + variable + ";");
            addIndentation(out, 2);
            out.println("} catch (IllegalAccessException e) {");
            addIndentation(out, 3);
            out.println("throw new RuntimeException(e);");
            addIndentation(out, 2);
            out.println("}");
        }

        addIndentation(out, 1);
        out.println("}");

        lineBreak(out);
    }

    private void addFields(List<FieldInfo> fields, PrintWriter out) {
        fields.forEach(field -> {
            addIndentation(out, 1);
            out.println("private " + field.type() + " " + field.name() + ";");
            lineBreak(out);
        });
        lineBreak(out);
    }

    private void addMethods(List<FieldInfo> fields, String builderName, PrintWriter out) {
        fields.forEach(field -> this.addMethod(field, builderName, out));
        lineBreak(out);
    }

    private void addMethod(FieldInfo field, String builderName, PrintWriter out) {
        addIndentation(out, 1);
        out.println("public " + builderName + " " + field.name() + "(" + field.type() + " " + field.name() + ") {");

        addIndentation(out, 2);
        out.println("this." + field.name() + " = " + field.name() + ";");
        addIndentation(out, 2);
        out.println("return this;");

        addIndentation(out, 1);
        out.println("}");
    }

    private void lineBreak(PrintWriter out) {
        out.println();
    }

    private void addIndentation(PrintWriter out, int level) {
        IntStream.range(0, level).forEach(l -> out.print("  "));
    }

    private void addHelperSetField(PrintWriter out, String pkg, String clsName) {
        String fullName = pkg + "." + clsName;
        String parameter = clsName.toLowerCase();
        addIndentation(out, 1);
        out.println("private void setField(" + fullName + " " + parameter + " , java.lang.reflect.Field field, Object value) throws IllegalAccessException {");
        addIndentation(out, 2);
        out.println("field.setAccessible(true);");
        addIndentation(out, 2);
        out.println("field.set(" + parameter + ", value);");
        addIndentation(out, 1);
        out.println("}");
    }

    private void error(String msg, Element e) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, e);
    }


}