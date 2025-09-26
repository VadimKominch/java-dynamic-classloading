package org.example;

public class MyCustomClass implements AbstractCommand {
    public void sayHello() {
        System.out.println(getHello());
    }

    public String getHello() {
        return "Hello!";
    }

    @Override
    public void execute() {
        sayHello();
    }
}
