package com.example;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

import java.io.IOException;
import java.time.LocalDate;

public class ClassInfoExample {
    public static void main(String[] args) throws IOException {
        var a = LocalDate.parse("2025-10-21").toEpochDay();
        var b = LocalDate.ofEpochDay(20130);
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()          // Enable full metadata scanning -> can be split to enableAnnotationInfo and enableMethodInfo
                .acceptPackages("com.example") // Only scan your app packages
                .scan()) {

            // Iterate through all discovered classes
            for (ClassInfo classInfo : scanResult.getAllClasses()) {
                System.out.println("Class: " + classInfo.getName());
                System.out.println("  Superclass: " + classInfo.getSuperclass());
                System.out.println("  Interfaces: " + classInfo.getInterfaces());
                System.out.println("  Annotations: " + classInfo.getAnnotations());
                System.out.println("  Methods: " + classInfo.getMethodInfo());
                System.out.println("  Fields: " + classInfo.getFieldInfo());
                System.out.println("------------------------------------");
            }
        }
        Runtime.getRuntime().exec("java Parameter.java");
    }
}
