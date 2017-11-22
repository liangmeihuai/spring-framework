package ac;

import org.springframework.stereotype.Component;

import java.lang.reflect.Array;

/**
 * Created by tend on 2017/11/10.
 */
@Component
public class Ah {
    private  int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println("ac.Ah".contains("ah"));
        System.out.println(Boolean[].class.getName());
        System.out.println(Array.newInstance(Integer.class, 0).getClass());
    }
}
