package com.casiano.builder;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("com.casiano.builder.Builder")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class BuilderProcessor extends AbstractProcessor {

    public static final String PRIVATE = "private ";
    public static final String THIS = "this.";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Builder.class);

        elements.forEach(this::generateClass);

        return false;
    }

    private void generateClass(Element element) {
        try {
            Builder builderAnnotation = element.getAnnotation(Builder.class);

            String builderAnnotationName = builderAnnotation.name();

            String builderPackage = BuilderHelper.extractPackageName(builderAnnotationName).orElseGet(() -> element.getEnclosingElement().toString());

            String targetPackage = element.getEnclosingElement().toString();

            String builderName = BuilderHelper.extractBuilderName(builderAnnotationName).orElseGet(() -> element.getSimpleName() + "Builder");

            String fullBuilderName = builderPackage + "." + builderName;

            JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(fullBuilderName);
            PrintWriter out = new PrintWriter(builderFile.openWriter());
            try (out) {
                boolean modeSetter = builderAnnotation.mode() == BuilderMode.SETTER;
                List<FieldInfo> fields = modeSetter ? FieldInfo.extractSetters(element) : FieldInfo.extractFields(element);

                TargetClassInfo classInfo =
                        new TargetClassInfo(builderPackage, builderName, targetPackage, element.getSimpleName().toString(), out, builderAnnotation, fields);

                declareClass(classInfo);
                addFields(classInfo);

                if (classInfo.hasCopyBuilder()) {
                    generateCopyBuilder(classInfo);
                }

                addConstructor(classInfo);
                addNewBuilder(classInfo);
                addBuildMethod(classInfo);
                addMethods(classInfo);

                if (!modeSetter) {
                    addHelperSetField(classInfo);
                }

                classInfo.writer().println("}");
            }
        } catch (Exception e) {
            error("Error: " + e, element);
        }
    }

    private void generateCopyBuilder(TargetClassInfo classInfo) {
        generateFieldsCopyBuilder(classInfo);
        generateMethodCopyBuilder(classInfo);
    }

    private void generateFieldsCopyBuilder(TargetClassInfo classInfo) {
        classInfo.fields().forEach(field -> {
            classInfo.addIndentation(1);
            classInfo.writer().println("private boolean init" + field.name() + ";");
            classInfo.lineBreak();
        });


    }

    private void generateMethodCopyBuilder(TargetClassInfo classInfo) {
        classInfo.addIndentation(1);
        classInfo.writer().println("public static " + classInfo.builderName() + " copyBuilder(" +
                classInfo.fullName() + " " + classInfo.variableName() + ") {");
        classInfo.addIndentation(2);
        classInfo.writer().println("return new " + classInfo.builderName() + "(" + classInfo.variableName() + ", true);");
        classInfo.addIndentation(1);
        classInfo.writer().println("}");
        classInfo.lineBreak();
    }

    private void addConstructor(TargetClassInfo classInfo) {
        classInfo.addIndentation(1);
        classInfo.writer().println(PRIVATE + classInfo.builderName() + "(" +
                classInfo.fullName() + " " + classInfo.variableName() + ", boolean generateWithCopy) {");
        classInfo.addIndentation(2);
        classInfo.writer().println(THIS + classInfo.variableName() + " = " + classInfo.variableName() + ";");
        classInfo.addIndentation(2);
        classInfo.writer().println("this.generateWithCopy = generateWithCopy;");
        classInfo.addIndentation(1);
        classInfo.writer().println("}");
        classInfo.lineBreak();
    }

    private void addNewBuilder(TargetClassInfo classInfo) {
        classInfo.addIndentation(1);
        classInfo.writer().println("public static " + classInfo.builderName() + " newBuilder() {");
        classInfo.addIndentation(2);
        classInfo.writer().println("return new " + classInfo.builderName() + "(new " + classInfo.fullName() + "(), false);");
        classInfo.addIndentation(1);
        classInfo.writer().println("}");
        classInfo.lineBreak();
    }

    private void declareClass(TargetClassInfo classInfo) {
        classInfo.writer().println("package " + classInfo.builderPackageName() + ";");
        classInfo.lineBreak();
        classInfo.writer().println("public class " + classInfo.builderName() + "{");
        classInfo.lineBreak();
    }

    private void addBuildMethod(TargetClassInfo classInfo) {
        String fullName = classInfo.fullName();
        String variable = THIS + classInfo.variableName();
        classInfo.addIndentation(1);
        classInfo.writer().println("public " + fullName + " build() {");
        if (classInfo.isModeSetter()) {
            for (FieldInfo field : classInfo.fields()) {
                String setter = "set" + Character.toUpperCase(field.name().charAt(0)) + field.name().substring(1);
                String setterCall = variable + "." + setter + "(this." + field.name() + ");";
                addConditionSetField(classInfo, field.name(), setterCall, 2);
            }
            classInfo.addIndentation(2);
            classInfo.writer().println("return " + variable + ";");
        } else {
            classInfo.addIndentation(2);
            classInfo.writer().println("try {");
            classInfo.addIndentation(3);
            classInfo.writer().println("java.lang.reflect.Field[] fields = " + variable + ".getClass().getDeclaredFields();");
            for (int i = 0; i < classInfo.fields().size(); i++) {
                String setterCall = "setField(" + variable + ", fields[" + i + "], this." + classInfo.fields().get(i).name() + ");";
                addConditionSetField(classInfo, classInfo.fields().get(i).name(), setterCall, 3);
            }
            classInfo.addIndentation(3);
            classInfo.writer().println("return " + variable + ";");
            classInfo.addIndentation(2);
            classInfo.writer().println("} catch (IllegalAccessException e) {");
            classInfo.addIndentation(3);
            classInfo.writer().println("throw new RuntimeException(e);");
            classInfo.addIndentation(2);
            classInfo.writer().println("}");
        }

        classInfo.addIndentation(1);
        classInfo.writer().println("}");

        classInfo.lineBreak();
    }

    private void addConditionSetField(TargetClassInfo classInfo, String attribute, String setter, int indentationLevel) {
        classInfo.addIndentation(indentationLevel);
        if (classInfo.hasCopyBuilder()) {
            classInfo.writer().println("if (!this.generateWithCopy || this.init" + attribute + ") {");
            classInfo.addIndentation(indentationLevel + 1);
        }
        classInfo.writer().println(setter);
        if (classInfo.hasCopyBuilder()) {
            classInfo.addIndentation(indentationLevel);
            classInfo.writer().println("}");
        }
    }

    private void addFields(TargetClassInfo classInfo) {
        classInfo.fields().forEach(field -> {
            classInfo.addIndentation(1);
            classInfo.writer().println(PRIVATE + field.type() + " " + field.name() + ";");
            classInfo.lineBreak();
        });
        classInfo.lineBreak();

        classInfo.addIndentation(1);
        classInfo.writer().println(PRIVATE + classInfo.fullName() + " " + classInfo.variableName() + ";");
        classInfo.lineBreak();

        classInfo.addIndentation(1);
        classInfo.writer().println("private boolean generateWithCopy;");
        classInfo.lineBreak();
    }

    private void addMethods(TargetClassInfo classInfo) {
        classInfo.fields().forEach(field -> this.addMethod(field, classInfo));
        classInfo.lineBreak();
    }

    private void addMethod(FieldInfo field, TargetClassInfo classInfo) {
        classInfo.addIndentation(1);
        classInfo.writer().println("public " + classInfo.builderName() + " " + field.name() + "(" + field.type() + " " + field.name() + ") {");

        if (classInfo.hasCopyBuilder()) {
            classInfo.addIndentation(2);
            classInfo.writer().println("this.init" + field.name() + " = this.generateWithCopy;");
        }

        classInfo.addIndentation(2);
        classInfo.writer().println(THIS + field.name() + " = " + field.name() + ";");
        classInfo.addIndentation(2);
        classInfo.writer().println("return this;");

        classInfo.addIndentation(1);
        classInfo.writer().println("}");
    }

    private void addHelperSetField(TargetClassInfo classInfo) {
        classInfo.addIndentation(1);
        classInfo.writer().println("private void setField(" + classInfo.fullName() + " " + classInfo.variableName() + " , java.lang.reflect.Field field, Object value) throws IllegalAccessException {");
        classInfo.addIndentation(2);
        classInfo.writer().println("field.setAccessible(true);");
        classInfo.addIndentation(2);
        classInfo.writer().println("field.set(" + classInfo.variableName() + ", value);");
        classInfo.addIndentation(1);
        classInfo.writer().println("}");
    }

    private void error(String msg, Element e) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, e);
    }


}