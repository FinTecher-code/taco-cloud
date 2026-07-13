package org.example.tacocloud.chqs;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class Person {
    public String name;
    private int age;
    public Person(String name, int age){
        this.name = name;
        this.age = age;
    }
    private void hello(){
        System.out.println("Hello, " + this.name + "!");
    }
}

public class TestPerson{
    public static void main(String[] args) throws Exception{
        Class<?> cls = Class.forName("org.example.tacocloud.chqs.Person");
        Constructor<?> constructor = cls.getDeclaredConstructor(String.class, int.class);
        constructor.setAccessible(true);
        Object obj = constructor.newInstance("Alice",25);
        Field field = cls.getDeclaredField("age");
        field.setInt(obj, -1);
        Method method = cls.getDeclaredMethod("hello");
        method.invoke(obj);
    }
}