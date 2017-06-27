package meihuai_context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.meihuai.model.Person;

public class AnnotationTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Person person=context.getBean("person", Person.class);
        System.out.println(person);
    }
}