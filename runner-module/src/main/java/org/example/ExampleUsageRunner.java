package org.example;

import org.example.holder.JavaClassesHolder;
import org.example.loader.JarLibraryClassLoader;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ExampleUsageRunner {
    public void run() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        JavaClassesHolder holder = new JavaClassesHolder();
        JarLibraryClassLoader loader = new JarLibraryClassLoader(holder);
        File file = new File(Runner.class.getClassLoader().getResource("example-client-1.0-SNAPSHOT.jar").getFile());
        /**
         * MultipartFile fileFromSpring;
         * File file;
         * fileFromSpring.transferTo(file);
         * */
        loader.loadLibrary(file);
        var classes = holder.getClasses();
//        String targetClass = "org.example.MyCustomClass";
        var targetClazz = classes
                .stream()
                .filter(el -> Arrays.asList(el.getInterfaces()).contains(AbstractCommand.class))
                .toList()
                .getFirst();
        AbstractCommand command = (AbstractCommand) targetClazz.getConstructors()[0].newInstance();
        Method getHelloMethod = targetClazz.getMethod("getHello");
        Method sayHelloMethod = targetClazz.getMethod("sayHello");
        String result = (String) getHelloMethod.invoke(command);
        sayHelloMethod.invoke(command);
        command.execute();
    }

    private void parseStackTrace(RuntimeException ex) {
//        RuntimeException nested = new RuntimeException("nested exception");
//        RuntimeException ex = new RuntimeException("test exception", nested);
        String finalExString = Arrays
                .stream(ex.getStackTrace())
                .map(element -> System.lineSeparator() + element)
                .collect(Collectors.joining());
        System.out.println(finalExString);
    }
}
