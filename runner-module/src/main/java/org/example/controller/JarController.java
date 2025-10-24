package org.example.controller;

import org.example.AbstractCommand;
import org.example.holder.JavaClassesHolder;
import org.example.loader.JarLibraryClassLoader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

@RestController
@RequestMapping("jar")
public class JarController {


    private static final ThreadLocal<String> entityLibraryName = ThreadLocal.withInitial(String::new);
    private JarLibraryClassLoader loader;

    private JavaClassesHolder holder;

    public JarController(JarLibraryClassLoader loader, JavaClassesHolder holder) {
        this.loader = loader;
        this.holder = holder;
    }

    @PostMapping
    public String loadJar(@RequestParam("file") MultipartFile jarLibrary) throws IOException {
        File file = new File(Objects.requireNonNull(jarLibrary.getOriginalFilename()));
        jarLibrary.transferTo(file);
        loader.loadLibrary(file);
        return "ok";
    }

    @PostMapping("execute")
    public String execute() {
        try {
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
            return "ok";
        } catch (NoSuchMethodException| InvocationTargetException | InstantiationException | IllegalAccessException e) {
            System.out.println("Errors during loading and running library classes");
            return "fail";
        }
    }
}
