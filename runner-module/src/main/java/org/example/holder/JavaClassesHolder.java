package org.example.holder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class JavaClassesHolder {
    private final CopyOnWriteArrayList<Class<?>> context;

    public JavaClassesHolder() {
        context = new CopyOnWriteArrayList<>();
    }

    public void addNewClass(Class<?> classToAdd) {
        context.add(classToAdd);
    }

    public boolean isPresent(Class<?> classToAdd) {
        return context.contains(classToAdd);
    }

    public List<Class<?>> getClasses() {
        return context;
    }

}
