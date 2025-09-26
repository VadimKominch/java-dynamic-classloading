package org.example.loader;

import org.example.holder.JavaClassesHolder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

public class JarLibraryClassLoader {

    private JavaClassesHolder holder;
    private URLClassLoader classLoader;

    public JarLibraryClassLoader(JavaClassesHolder holder) {
        this.holder = holder;
    }

    public void loadLibrary(File library) {
        try(JarFile jarFile = new JarFile(library)) {
            URL jarUrl = library.toURI().toURL();
            this.classLoader = new URLClassLoader(new URL[]{jarUrl}, Thread.currentThread().getContextClassLoader());
            var entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                var element = entries.nextElement();
                if (element.isDirectory() || !element.getName().endsWith(".class")) {
                    continue;
                }
                String className = element.getName().substring(0, element.getName().length() - 6);
                className = className.replace('/', '.');
                Class<?> clazz = this.classLoader.loadClass(className);
                holder.addNewClass(clazz);
                System.out.println("Class " + clazz.getSimpleName() + " loaded into memory");
            }
        } catch (ClassNotFoundException | IOException exception) {
            System.out.println("Error occurred while library loading");
            throw new RuntimeException(exception);
        }
    }
}
