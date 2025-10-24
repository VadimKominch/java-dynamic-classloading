package org.example;

import org.example.holder.JavaClassesHolder;
import org.example.loader.JarLibraryClassLoader;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ExampleUsageRunner {
    public void run() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        JavaClassesHolder holder = new JavaClassesHolder();
        JarLibraryClassLoader loader = new JarLibraryClassLoader(holder);
        File file = new File(Runner.class.getClassLoader().getResource("example-client-1.0-SNAPSHOT.jar").getFile());
        loader.loadLibrary(file);
        var classes = holder.getClasses();
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

    static void copyArrayList(List<?> src, Object[] dst) throws IllegalAccessException, NoSuchFieldException {
        var arrayListElementData = MethodHandles.privateLookupIn(ArrayList.class, MethodHandles.lookup())
                .findVarHandle(ArrayList.class, "elementData", Object[].class);
        var listElements = (Object[]) arrayListElementData.get(src);
        System.arraycopy(listElements, 0, dst, 0, listElements.length);
    }

    public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException {

        List<Integer> list = new ArrayList<>(0) {{
            add(1);
            add(2);
            add(3);
        }};
        var secondList = List.of(1,2,3);
        Object[] dst = new Object[list.size()];
        copyArrayList(list, dst);

        System.out.println(dst);
    }
}
